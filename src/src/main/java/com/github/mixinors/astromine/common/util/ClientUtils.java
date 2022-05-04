/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.util;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.fluid.ExtendedFluid;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

public class ClientUtils {
	/**
	 * Returns Minecraft's client's instance.
	 */
	public static MinecraftClient getInstance() {
		return MinecraftClient.getInstance();
	}

	/**
	 * Returns Minecraft's client's player.
	 */
	public static ClientPlayerEntity getPlayer() {
		return getInstance().player;
	}

	/**
	 * Returns Minecraft's client's world.
	 **/
	public static ClientWorld getWorld() {
		return getInstance().world;
	}

	/**
	 * Registers the necessary data for an {@link ExtendedFluid} on the client side.
	 */
	@Environment(EnvType.CLIENT)
	public static void registerExtendedFluid(String name, int tint, Fluid still, Fluid flowing) {
		var stillSpriteIdentifier = new Identifier("block/water_still");
		var flowingSpriteIdentifier = new Identifier("block/water_flow");
		var listenerIdentifier = AMCommon.id(name + "_reload_listener");
		
		var fluidSprites = new Sprite[] { null, null };

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
			public void reload(ResourceManager resourceManager) {
				final var atlas = ClientUtils.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
				fluidSprites[0] = atlas.apply(stillSpriteIdentifier);
				fluidSprites[1] = atlas.apply(flowingSpriteIdentifier);
			}
		});

		final var handler = new FluidRenderHandler() {
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