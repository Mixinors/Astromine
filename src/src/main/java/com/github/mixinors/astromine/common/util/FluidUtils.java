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
import com.github.mixinors.astromine.registry.common.AMFluids;
import dev.vini2003.hammer.core.api.client.util.InstanceUtils;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.Random;

public class FluidUtils {
	private static final int[] UNIVITE_COLORS = new int[] {
			0x7EF52DFF,
			0x7EFF2DD4,
			0x7EFF2D9F,
			0x7EFF2D6A,
			0x7EFF2D35,
			0x7EFF5A2D,
			0x7EFF8F2D,
			0x7EFFC42D,
			0x7EFFF92D,
			0x7ED0FF2D,
			0x7E9CFF2D,
			0x7E67FF2D,
			0x7E32FF2D,
			0x7E2DFF5D,
			0x7E2DFF92,
			0x7E2DFFC7,
			0x7E2DFFFC,
			0x7E2DCDFF,
			0x7E2DFFFC,
			0x7E2DFFC7,
			0x7E2DFF92,
			0x7E2DFF5D,
			0x7E32FF2D,
			0x7E67FF2D,
			0x7E9CFF2D,
			0x7ED0FF2D,
			0x7EFFF92D,
			0x7EFFC42D,
			0x7EFF8F2D,
			0x7EFF5A2D,
			0x7EFF2D35,
			0x7EFF2D6A,
			0x7EFF2D9F,
			0x7EFF2DD4,
			0x7EF52DFF
	};
	
	private static long LAST_TIME_MS = System.currentTimeMillis();
	
	private static int LAST_COLOR_INDEX = 0;
	
	public static void registerSimpleFluid(String name, int tint, Fluid still, Fluid flowing, boolean customSprite, boolean customHandler) {
		var stillSpriteId = new Identifier("block/water_still");
		var flowingSpriteId = new Identifier("block/water_flow");
		
		var listenerIdentifier = AMCommon.id(name + "_reload_listener");
		
		var fluidSprites = new Sprite[] { null, null };
		
		if (!customSprite) {
			ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
				registry.register(stillSpriteId);
				registry.register(flowingSpriteId);
			});
			
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
				@Override
				public Identifier getFabricId() {
					return listenerIdentifier;
				}
				
				@Override
				public void reload(ResourceManager resourceManager) {
					var client = InstanceUtils.getClient();
					
					var atlas = client.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
					
					fluidSprites[0] = atlas.apply(stillSpriteId);
					fluidSprites[1] = atlas.apply(flowingSpriteId);
				}
			});
		}
		
		if (!customHandler) {
			var handler = new FluidRenderHandler() {
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
	
	public static void registerUniviteFluid() {
		var stillSpriteId = new Identifier("block/water_still");
		var flowingSpriteId = new Identifier("block/water_flow");
		
		var listenerIdentifier = AMCommon.id("molten_univite_reload_listener");
		
		var fluidSprites = new Sprite[] { null, null };
		
		ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			registry.register(stillSpriteId);
			registry.register(flowingSpriteId);
		});
		
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return listenerIdentifier;
			}
			
			@Override
			public void reload(ResourceManager resourceManager) {
				var client = InstanceUtils.getClient();
				
				var atlas = client.getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
				
				fluidSprites[0] = atlas.apply(stillSpriteId);
				fluidSprites[1] = atlas.apply(flowingSpriteId);
			}
		});
		
		var handler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state) {
				return fluidSprites;
			}
			
			@Override
			public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state) {
				var client = InstanceUtils.getClient();
				
<<<<<<< HEAD
				if (client == null || client.world == null) {
					return UNIVITE_COLORS[0];
				}
				
				if (System.currentTimeMillis() - LAST_TIME_MS > 50) {
					LAST_COLOR_INDEX = client.world.random.nextInt(UNIVITE_COLORS.length);
				}
				
				return UNIVITE_COLORS[LAST_COLOR_INDEX];
=======
				return UNIVITE_COLORS[new Random().nextInt(UNIVITE_COLORS.length)];
>>>>>>> e44ea96eaf52114839e655b7993abed36a91a4b1
			}
		};
		
		FluidRenderHandlerRegistry.INSTANCE.register(AMFluids.MOLTEN_UNIVITE.getStill(), handler);
		FluidRenderHandlerRegistry.INSTANCE.register(AMFluids.MOLTEN_UNIVITE.getFlowing(), handler);
	}
}
