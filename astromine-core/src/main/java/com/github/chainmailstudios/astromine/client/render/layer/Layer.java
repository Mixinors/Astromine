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

package com.github.chainmailstudios.astromine.client.render.layer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class Layer extends RenderType {
	private static final Map<ResourceLocation, RenderType> CACHE = new HashMap<>();

	private static final RenderType HOLOGRAPHIC_BRIDGE = create("holographic_bridge", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, 7, 256, false, true, RenderType.CompositeState.builder().setCullState(NO_CULL).setLightmapState(LIGHTMAP).setShadeModelState(SMOOTH_SHADE).setTransparencyState(
		TRANSLUCENT_TRANSPARENCY).setAlphaState(DEFAULT_ALPHA).setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(false));

	private static final RenderType GAS = create("gas", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, 7, 2097152, true, true, RenderType.CompositeState.builder().setCullState(NO_CULL).setLayeringState(VIEW_OFFSET_Z_LAYERING).setShadeModelState(SMOOTH_SHADE).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
		.createCompositeState(true));

	/** Instantiates a {@link Layer}. */
	public Layer(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	/** Returns the {@link RenderType} for the given texture. */
	public static RenderType get(ResourceLocation texture) {
		CACHE.computeIfAbsent(texture, (key) -> create("entity_cutout", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, true, true, RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			.setDiffuseLightingState(NO_DIFFUSE_LIGHTING).setAlphaState(DEFAULT_ALPHA).setLightmapState(NO_LIGHTMAP).setOverlayState(NO_OVERLAY).createCompositeState(true)));
		return CACHE.get(texture);
	}

	/** Returns the Holographic Bridge {@link RenderType}. */
	public static RenderType getHolographicBridge() {
		return HOLOGRAPHIC_BRIDGE;
	}

	/** Returns the Gas {@link RenderType}. */
	public static RenderType getGas() {
		return GAS;
	}
}
