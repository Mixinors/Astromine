package com.github.mixinors.astromine.client.util;

import com.github.mixinors.astromine.common.util.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class DrawingUtil {
	public static final int DEFAULT_LIGHT = LightTexture.FULL_BRIGHT;
	public static final int DEFAULT_OVERLAY = OverlayTexture.NO_OVERLAY;
	
	public static net.minecraft.client.renderer.entity.ItemRenderer getItemRenderer() {
		return Minecraft.getInstance().getItemRenderer();
	}
	
	public static net.minecraft.client.renderer.texture.TextureManager getTextureManager() {
		return Minecraft.getInstance().getTextureManager();
	}
	
	public static void drawLine(PoseStack matrices, MultiBufferSource provider, float x1, float y1, float z1, float x2, float y2, float z2, Color color, RenderType layer) {
		var consumer = provider.getBuffer(layer);
		var pose = matrices.last();
		
		consumer.addVertex(pose, x1, y1, z1)
				.setColor(color.getR(), color.getG(), color.getB(), color.getA())
				.setNormal(pose, 0.0F, 1.0F, 0.0F);
		consumer.addVertex(pose, x2, y2, z2)
				.setColor(color.getR(), color.getG(), color.getB(), color.getA())
				.setNormal(pose, 0.0F, 1.0F, 0.0F);
		
		consumer.addVertex(pose, x1, y1, z1)
				.setColor(color.getR(), color.getG(), color.getB(), color.getA())
				.setNormal(pose, 1.0F, 0.0F, 0.0F);
		consumer.addVertex(pose, x2, y2, z2)
				.setColor(color.getR(), color.getG(), color.getB(), color.getA())
				.setNormal(pose, 1.0F, 0.0F, 0.0F);
	}
	
	public static void drawSquare(PoseStack matrices, MultiBufferSource provider, float x, float y, float z, float width, float height, Color color, RenderType layer) {
		drawLine(matrices, provider, x, y, z, x + width, y, z, color, layer);
		drawLine(matrices, provider, x, y + height, z, x + width, y + height, z, color, layer);
		drawLine(matrices, provider, x, y, z, x, y + height, z, color, layer);
		drawLine(matrices, provider, x + width, y, z, x + width, y + height, z, color, layer);
	}
	
	public static void drawBody(MultiBufferSource provider, float x, float y, float z, float width, float height, float depth, float angle, boolean tidalLocked, Color color, RenderType layerUp, RenderType layerDown, RenderType layerNorth, RenderType layerSouth, RenderType layerEast, RenderType layerWest) {
		drawBody(new PoseStack(), provider, x, y, z, width, height, depth, angle, tidalLocked, color, layerUp, layerDown, layerNorth, layerSouth, layerEast, layerWest);
	}
	
	public static void drawBody(PoseStack matrices, MultiBufferSource provider, float x, float y, float z, float width, float height, float depth, float angle, boolean tidalLocked, Color color, RenderType layerUp, RenderType layerDown, RenderType layerNorth, RenderType layerSouth, RenderType layerEast, RenderType layerWest) {
		matrices.pushPose();
		matrices.translate(x, y, z);
		matrices.mulPose(Axis.XP.rotationDegrees(45.0F));
		
		if (tidalLocked) {
			matrices.mulPose(Axis.ZP.rotationDegrees(45.0F - 360.0F + (float) Math.toDegrees(angle)));
		} else {
			matrices.mulPose(Axis.ZP.rotationDegrees(45.0F - (float) Math.toDegrees(angle)));
		}
		
		var pose = matrices.last();
		var halfWidth = width / 2.0F;
		var halfHeight = height / 2.0F;
		var halfDepth = depth / 2.0F;
		
		face(provider.getBuffer(layerDown), pose, color,
				halfWidth, -halfHeight, halfDepth, 0.0F, 0.0F,
				-halfWidth, -halfHeight, halfDepth, 1.0F, 0.0F,
				-halfWidth, -halfHeight, -halfDepth, 1.0F, 1.0F,
				halfWidth, -halfHeight, -halfDepth, 0.0F, 1.0F,
				0.0F, -1.0F, 0.0F);
		
		face(provider.getBuffer(layerUp), pose, color,
				-halfWidth, halfHeight, -halfDepth, 0.0F, 0.0F,
				-halfWidth, halfHeight, halfDepth, 1.0F, 0.0F,
				halfWidth, halfHeight, halfDepth, 1.0F, 1.0F,
				halfWidth, halfHeight, -halfDepth, 0.0F, 1.0F,
				0.0F, 1.0F, 0.0F);
		
		face(provider.getBuffer(layerNorth), pose, color,
				-halfWidth, -halfHeight, -halfDepth, 0.0F, 0.0F,
				-halfWidth, halfHeight, -halfDepth, 1.0F, 0.0F,
				halfWidth, halfHeight, -halfDepth, 1.0F, 1.0F,
				halfWidth, -halfHeight, -halfDepth, 0.0F, 1.0F,
				0.0F, 0.0F, -1.0F);
		
		face(provider.getBuffer(layerSouth), pose, color,
				-halfWidth, -halfHeight, halfDepth, 0.0F, 0.0F,
				halfWidth, -halfHeight, halfDepth, 1.0F, 0.0F,
				halfWidth, halfHeight, halfDepth, 1.0F, 1.0F,
				-halfWidth, halfHeight, halfDepth, 0.0F, 1.0F,
				0.0F, 0.0F, 1.0F);
		
		face(provider.getBuffer(layerWest), pose, color,
				-halfWidth, halfHeight, halfDepth, 0.0F, 0.0F,
				-halfWidth, halfHeight, -halfDepth, 1.0F, 0.0F,
				-halfWidth, -halfHeight, -halfDepth, 1.0F, 1.0F,
				-halfWidth, -halfHeight, halfDepth, 0.0F, 1.0F,
				-1.0F, 0.0F, 0.0F);
		
		face(provider.getBuffer(layerEast), pose, color,
				halfWidth, -halfHeight, -halfDepth, 0.0F, 0.0F,
				halfWidth, halfHeight, -halfDepth, 1.0F, 0.0F,
				halfWidth, halfHeight, halfDepth, 1.0F, 1.0F,
				halfWidth, -halfHeight, halfDepth, 0.0F, 1.0F,
				1.0F, 0.0F, 0.0F);
		
		matrices.popPose();
	}
	
	private static void face(VertexConsumer consumer, PoseStack.Pose pose, Color color, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3, float x4, float y4, float z4, float u4, float v4, float normalX, float normalY, float normalZ) {
		vertex(consumer, pose, color, x1, y1, z1, u1, v1, normalX, normalY, normalZ);
		vertex(consumer, pose, color, x2, y2, z2, u2, v2, normalX, normalY, normalZ);
		vertex(consumer, pose, color, x3, y3, z3, u3, v3, normalX, normalY, normalZ);
		vertex(consumer, pose, color, x4, y4, z4, u4, v4, normalX, normalY, normalZ);
	}
	
	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Color color, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ) {
		consumer.addVertex(pose, x, y, z)
				.setColor(color.getR(), color.getG(), color.getB(), color.getA())
				.setUv(u, v)
				.setOverlay(DEFAULT_OVERLAY)
				.setLight(DEFAULT_LIGHT)
				.setNormal(pose, normalX, normalY, normalZ);
	}
}
