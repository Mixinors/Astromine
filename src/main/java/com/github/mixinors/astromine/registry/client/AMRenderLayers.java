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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class AMRenderLayers {
	private static final Map<ResourceLocation, RenderType> CACHE = new HashMap<>();
	private static final RenderType HOLOGRAPHIC_BRIDGE = RenderType.create(
			"astromine_holographic_bridge",
			DefaultVertexFormat.POSITION_COLOR,
			VertexFormat.Mode.QUADS,
			1536,
			false,
			true,
			RenderType.CompositeState.builder()
					.setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setCullState(RenderStateShard.NO_CULL)
					.setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
					.createCompositeState(false)
	);
	
	public static void init(IEventBus modBus) {
		modBus.addListener(AMRenderLayers::registerBlockLayers);
	}
	
	private static void registerBlockLayers(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			register(AMBlocks.AIRLOCK.get(), RenderType.translucent());
			
			register(AMBlocks.SPACE_SLIME_BLOCK.get(), RenderType.translucent());
		});
	}
	
	public static <T extends Block> T register(T block, RenderType renderLayer) {
		ItemBlockRenderTypes.setRenderLayer(block, renderLayer);
		return block;
	}
	
	public static RenderType get(ResourceLocation texture) {
		CACHE.computeIfAbsent(texture, RenderType::entityTranslucent);
		return CACHE.get(texture);
	}
	
	public static RenderType getBody(ResourceLocation texture) {
		CACHE.computeIfAbsent(texture, RenderType::entityTranslucent);
		return CACHE.get(texture);
	}
	
	public static RenderType getHolographicBridge() {
		return HOLOGRAPHIC_BRIDGE;
	}
	
	public static RenderType getPumpTube() {
		return RenderType.translucent();
	}
}
