package com.github.mixinors.astromine.client.render.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ExtendedCubeMapRenderer {
	private final ResourceLocation[] faces = new ResourceLocation[6];
	
	public void swapTextures(SkyboxTextures textures) {
		faces[0] = textures.north();
		faces[1] = textures.west();
		faces[2] = textures.south();
		faces[3] = textures.east();
		faces[4] = textures.up();
		faces[5] = textures.down();
	}
	
	public void render(Camera camera) {
		var minecraft = Minecraft.getInstance();
		var tesselator = Tesselator.getInstance();
		var projection = new Matrix4f().setPerspective(1.4835298F, (float) minecraft.getWindow().getWidth() / (float) minecraft.getWindow().getHeight(), 0.05F, 10.0F);
		var modelViewStack = RenderSystem.getModelViewStack();
		
		RenderSystem.backupProjectionMatrix();
		RenderSystem.setProjectionMatrix(projection, VertexSorting.DISTANCE_TO_ORIGIN);
		
		modelViewStack.pushMatrix();
		modelViewStack.rotationX((float) Math.PI);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.depthMask(false);
		
		for (var pass = 0; pass < 4; ++pass) {
			modelViewStack.pushMatrix();
			modelViewStack.translate(((float) (pass % 2) / 2.0F - 0.5F) / 256.0F, ((float) (pass / 2) / 2.0F - 0.5F) / 256.0F, 0.0F);
			modelViewStack.rotateX(camera.getXRot() * (float) (Math.PI / 180.0));
			modelViewStack.rotateY(-camera.getYRot() * (float) (Math.PI / 180.0));
			RenderSystem.applyModelViewMatrix();
			
			for (var face = 0; face < 6; ++face) {
				RenderSystem.setShaderTexture(0, faces[face]);
				
				var buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				var alpha = Math.round(255.0F) / (pass + 1);
				
				if (face == 0) {
					buffer.addVertex(-1.0F, -1.0F, 1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, -1.0F, 1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				} else if (face == 1) {
					buffer.addVertex(1.0F, -1.0F, 1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, 1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, -1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, -1.0F, -1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				} else if (face == 2) {
					buffer.addVertex(1.0F, -1.0F, -1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, -1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, 1.0F, -1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, -1.0F, -1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				} else if (face == 3) {
					buffer.addVertex(-1.0F, -1.0F, -1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, 1.0F, -1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, 1.0F, 1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, -1.0F, 1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				} else if (face == 4) {
					buffer.addVertex(-1.0F, -1.0F, -1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, -1.0F, 1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, -1.0F, 1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, -1.0F, -1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				} else {
					buffer.addVertex(-1.0F, 1.0F, 1.0F).setUv(0.0F, 0.0F).setWhiteAlpha(alpha);
					buffer.addVertex(-1.0F, 1.0F, -1.0F).setUv(0.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, -1.0F).setUv(1.0F, 1.0F).setWhiteAlpha(alpha);
					buffer.addVertex(1.0F, 1.0F, 1.0F).setUv(1.0F, 0.0F).setWhiteAlpha(alpha);
				}
				
				BufferUploader.drawWithShader(buffer.buildOrThrow());
			}
			
			modelViewStack.popMatrix();
			RenderSystem.colorMask(true, true, true, false);
		}
		
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.restoreProjectionMatrix();
		modelViewStack.popMatrix();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.depthMask(true);
		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
	}
}
