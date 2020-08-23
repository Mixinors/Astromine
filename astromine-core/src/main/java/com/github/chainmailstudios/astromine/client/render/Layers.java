package com.github.chainmailstudios.astromine.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class Layers extends RenderLayer {
	public static final RenderLayer FLAT_NO_CUTOUT = of("flat_no_cutout", VertexFormats.POSITION_COLOR_LIGHT, 7, 256, RenderLayer.MultiPhaseParameters.builder()
			.texture(NO_TEXTURE)
			.cull(DISABLE_CULLING)
			.lightmap(ENABLE_LIGHTMAP)
			.shadeModel(SMOOTH_SHADE_MODEL)
			.depthTest(ALWAYS_DEPTH_TEST)
			.transparency(TRANSLUCENT_TRANSPARENCY)
			.alpha(ONE_TENTH_ALPHA)
			.layering(VIEW_OFFSET_Z_LAYERING).build(false)) ;

	public Layers(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}
}
