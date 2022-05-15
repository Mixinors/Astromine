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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.rendering.fabric.RenderTypeRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class AMRenderLayers {
	private static final Map<Identifier, RenderLayer> CACHE = new HashMap<>();
	
	private static final RenderLayer HOLOGRAPHIC_BRIDGE = RenderLayer.of(
			"holographic_bridge",
			VertexFormats.POSITION_COLOR_LIGHT,
			VertexFormat.DrawMode.QUADS,
			256,
			false,
			true,
			RenderLayer.MultiPhaseParameters.builder()
											.cull(RenderPhase.DISABLE_CULLING)
											.lightmap(RenderPhase.ENABLE_LIGHTMAP)
											.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
											.layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
											.shader(new RenderPhase.Shader(GameRenderer::getPositionColorLightmapShader))
											.build(false));
	
	private static final RenderLayer PUMP_TUBE = RenderLayer.of(
			"pump_tube",
			VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
			VertexFormat.DrawMode.QUADS,
			256,
			true,
			false,
			RenderLayer.MultiPhaseParameters.builder()
											.shader(new RenderPhase.Shader(GameRenderer::getPositionColorTexLightmapShader))
											.texture(new RenderPhase.Texture(AMCommon.id("textures/block/pump_tube.png"), false, false))
											.cull(RenderPhase.DISABLE_CULLING)
											.build(true)
	);
	
	public static void init() {
		ClientLifecycleEvent.CLIENT_SETUP.register(client -> {
			register(AMBlocks.AIRLOCK.get(), RenderLayer.getTranslucent());
			
			register(AMBlocks.SPACE_SLIME_BLOCK.get(), RenderLayer.getTranslucent());
		});
	}
	
	public static <T extends Block> T register(T block, RenderLayer renderLayer) {
		RenderTypeRegistryImpl.register(renderLayer, block);
		return block;
	}
	
	public static RenderLayer get(Identifier texture) {
		CACHE.computeIfAbsent(texture, (key) -> RenderLayer.of(
				"entity_cutout",
				VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
				VertexFormat.DrawMode.QUADS,
				256,
				true,
				true,
				RenderLayer.MultiPhaseParameters.builder()
												.texture(new RenderPhase.Texture(texture, false, false))
												.transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY)
												.lightmap(RenderPhase.DISABLE_LIGHTMAP)
												.overlay(RenderPhase.DISABLE_OVERLAY_COLOR)
												.build(true)));
		
		return CACHE.get(texture);
	}
	
	public static RenderLayer getHolographicBridge() {
		return HOLOGRAPHIC_BRIDGE;
	}
	
	public static RenderLayer getPumpTube() {
		return PUMP_TUBE;
	}
}
