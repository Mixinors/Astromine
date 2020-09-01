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

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class Layer extends RenderLayer {
	private static final RenderLayer HOLOGRAPHIC_BRIDGE = RenderLayer.of("holographic_bridge", VertexFormats.POSITION_COLOR_LIGHT, 7, 256, false, true, RenderLayer.MultiPhaseParameters.builder().cull(RenderPhase.DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).shadeModel(
		RenderLayer.SMOOTH_SHADE_MODEL).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).alpha(RenderLayer.ONE_TENTH_ALPHA).layering(RenderLayer.VIEW_OFFSET_Z_LAYERING).build(false));

	private static final RenderLayer FLAT_NO_CUTOUT = of("flat_no_cutout", VertexFormats.POSITION_COLOR_LIGHT, 7, 256, RenderLayer.MultiPhaseParameters.builder().texture(NO_TEXTURE).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).shadeModel(SMOOTH_SHADE_MODEL).depthTest(
		ALWAYS_DEPTH_TEST).transparency(TRANSLUCENT_TRANSPARENCY).alpha(ONE_TENTH_ALPHA).layering(VIEW_OFFSET_Z_LAYERING).build(false));

	public Layer(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer get(Identifier texture) {
		RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(DISABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).lightmap(
			DISABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).build(true);
		return of("entity_cutout", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, 7, 256, true, true, multiPhaseParameters);
	}

	public static RenderLayer getHolographicBridge() {
		return HOLOGRAPHIC_BRIDGE;
	}

	public static RenderLayer getFlatNoCutout() {
		return FLAT_NO_CUTOUT;
	}
}
