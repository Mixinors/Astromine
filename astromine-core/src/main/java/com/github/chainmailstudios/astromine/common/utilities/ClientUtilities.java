/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chainmailstudios.astromine.common.utilities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import com.github.chainmailstudios.astromine.AstromineCommon;

import java.util.function.Function;

public class ClientUtilities {
	public static void playSound(BlockPos position, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
		MinecraftClient.getInstance().world.playSoundFromEntity(MinecraftClient.getInstance().player, MinecraftClient.getInstance().player, sound, category, volume, pitch);
	}

	@Environment(EnvType.CLIENT)
	public static void buildClient(String name, int tint, Fluid still, Fluid flowing) {
		final Identifier stillSpriteIdentifier = new Identifier("block/water_still");
		final Identifier flowingSpriteIdentifier = new Identifier("block/water_flow");
		final Identifier listenerIdentifier = AstromineCommon.identifier(name + "_reload_listener");

		final Sprite[] fluidSprites = { null, null };

		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			registry.register(stillSpriteIdentifier);
			registry.register(flowingSpriteIdentifier);
		});

		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return listenerIdentifier;
			}

			@Override
			public void apply(ResourceManager resourceManager) {
				final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
				fluidSprites[0] = atlas.apply(stillSpriteIdentifier);
				fluidSprites[1] = atlas.apply(flowingSpriteIdentifier);
			}
		});

		final FluidRenderHandler handler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return fluidSprites;
			}

			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				return tint;
			}
		};

		FluidRenderHandlerRegistry.INSTANCE.register(still, handler);
		FluidRenderHandlerRegistry.INSTANCE.register(flowing, handler);
	}
}
