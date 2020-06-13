package com.github.chainmailstudios.astromine.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.widget.api.Color;

public class BaseRenderer extends spinnery.client.render.BaseRenderer {
	public static void drawGradientQuadExtended(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, float startZ, float endZ, Color colorStart, Color colorEnd) {
		drawGradientQuadExtended(matrices, provider, startX, startY, endX, endY, startZ, endZ, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuadExtended(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, float startZ, float endZ, int light, Color colorStart, Color colorEnd) {
		drawGradientQuadExtended(matrices, provider, startX, startY, endX, endY, startZ, endZ, 0, 0, 1, 1, light, colorStart, colorEnd, false);
	}

	public static void drawGradientQuadExtended(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, float startZ, float endZ, float uS, float vS, float uE, float vE, int light, Color colorStart, Color colorEnd, boolean textured) {
		if (!textured) RenderSystem.disableTexture();

		matrices.push();

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		VertexConsumer consumer = provider.getBuffer(RenderLayer.getLines());

		consumer.vertex(matrices.peek().getModel(), startX, startY, endZ).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vS).light(light).overlay(OverlayTexture.DEFAULT_UV).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, endY, startZ).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vE).light(light).overlay(OverlayTexture.DEFAULT_UV).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), endX, endY, startZ).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vS).light(light).overlay(OverlayTexture.DEFAULT_UV).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), endX, startY, endZ).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vE).light(light).overlay(OverlayTexture.DEFAULT_UV).normal(matrices.peek().getNormal(), 0, 1, 0).next();

		RenderSystem.disableBlend();

		if (!textured) RenderSystem.enableTexture();

		matrices.pop();
	}
}
