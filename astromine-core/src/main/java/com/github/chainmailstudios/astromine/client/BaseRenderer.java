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

package com.github.chainmailstudios.astromine.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import com.github.vini2003.blade.client.utilities.Layers;
import com.github.vini2003.blade.common.miscellaneous.Color;

public class BaseRenderer {
	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color color) {
		matrices.push();
		drawQuad(matrices, provider, layer, x, y, sX, sY, 0x00f000f0, color);
		matrices.pop();
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color color) {
		matrices.push();
		drawQuad(matrices, provider, Layers.Companion.flat(), x, y, sX, sY, 0x00f000f0, color);
		matrices.pop();
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color color) {
		matrices.push();
		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), x, y, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x, y + sY, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).light(light).next();

		if (provider instanceof VertexConsumerProvider.Immediate) {
			((VertexConsumerProvider.Immediate) provider).draw();
		}

		matrices.pop();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, Color colorStart, Color colorEnd) {
		matrices.push();
		drawGradientQuad(matrices, provider, Layers.Companion.flat(), startX, startY, endX, endY, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
		matrices.pop();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, int light, Color colorStart, Color colorEnd) {
		matrices.push();
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, 0, 0, 1, 1, light, colorStart, colorEnd, false);
		matrices.pop();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, int light, Color colorStart, Color colorEnd) {
		matrices.push();
		drawGradientQuad(matrices, provider, Layers.Companion.flat(), startX, startY, endX, endY, 0, 0, 1, 1, light, colorStart, colorEnd, false);
		matrices.pop();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, float uS, float vS, float uE, float vE, int light, Color colorStart, Color colorEnd, boolean textured) {
		matrices.push();
		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), endX, startY, 0).color(colorStart.getR(), colorStart.getG(), colorStart.getB(), colorStart.getA()).texture(uS, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, startY, 0).color(colorStart.getR(), colorStart.getG(), colorStart.getB(), colorStart.getA()).texture(uS, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, endY, 0).color(colorEnd.getR(), colorEnd.getG(), colorEnd.getB(), colorEnd.getA()).texture(uE, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), endX, endY, 0).color(colorEnd.getR(), colorEnd.getG(), colorEnd.getB(), colorEnd.getA()).texture(uE, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();

		if (provider instanceof VertexConsumerProvider.Immediate) {
			((VertexConsumerProvider.Immediate) provider).draw();
		}

		matrices.pop();
	}

	public static void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height, Color shadowStart, Color shadowEnd, Color backgroundStart, Color backgroundEnd, Color outlineStart, Color outlineEnd) {
		matrices.push();
		drawTooltip(matrices, provider, Layers.Companion.tooltip(), x, y, width, height, shadowStart, shadowEnd, backgroundStart, backgroundEnd, outlineStart, outlineEnd);
		matrices.pop();
	}

	public static void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float width, float height, Color shadowStart, Color shadowEnd, Color backgroundStart, Color backgroundEnd, Color outlineStart, Color outlineEnd) {
		matrices.push();
		drawGradientQuad(matrices, provider, layer, x - 3, y - 4, x + width + 3, y - 3, 0, shadowStart, shadowStart); // Border - top
		drawGradientQuad(matrices, provider, layer, x - 3, y + height + 3, x + width + 3, y + height + 4, 0, shadowEnd, shadowEnd); // Border - bottom
		drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y + height + 3, 0, backgroundStart, backgroundEnd); // Body
		drawGradientQuad(matrices, provider, layer, x - 4, y - 3, x - 3, y + height + 3, 0, shadowStart, shadowEnd); // Border - left
		drawGradientQuad(matrices, provider, layer, x + width + 3, y - 3, x + width + 4, y + height + 3, 0, shadowStart, shadowEnd); // Border - right

		drawGradientQuad(matrices, provider, layer, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, 0, outlineStart, outlineEnd); // Outline - left
		drawGradientQuad(matrices, provider, layer, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, 0, outlineStart, outlineEnd); // Outline - right
		drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y - 3 + 1, 0, outlineStart, outlineStart); // Outline - top
		drawGradientQuad(matrices, provider, layer, x - 3, y + height + 2, x + width + 3, y + height + 3, 0, outlineEnd, outlineEnd); // Outline - bottom
		matrices.pop();
	}

	public static void drawPanel(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color shadow, Color panel, Color hilight, Color outline) {
		matrices.push();
		drawPanel(matrices, provider, Layers.Companion.flat(), x, y, sX, sY, shadow, panel, hilight, outline);
		matrices.pop();
	}

	public static void drawPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color shadow, Color panel, Color hilight, Color outline) {
		matrices.push();
		drawQuad(matrices, provider, layer, x + 3, y + 3, sX - 6, sY - 6, 0x00f000f0, panel);

		drawQuad(matrices, provider, layer, x + 2, y + 1, sX - 4, 2, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 3, sX - 4, 2, 0x00f000f0, shadow);
		drawQuad(matrices, provider, layer, x + 1, y + 2, 2, sY - 4, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + sX - 3, y + 2, 2, sY - 4, 0x00f000f0, shadow);
		drawQuad(matrices, provider, layer, x + sX - 3, y + 2, 1, 1, 0x00f000f0, panel);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 3, 1, 1, 0x00f000f0, panel);
		drawQuad(matrices, provider, layer, x + 3, y + 3, 1, 1, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + sX - 4, y + sY - 4, 1, 1, 0x00f000f0, shadow);

		drawQuad(matrices, provider, layer, x + 2, y, sX - 4, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x, y + 2, 1, sY - 4, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 1, y + 2, 1, sY - 4, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 1, sX - 4, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 1, y + 1, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 1, y + sY - 2, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 2, y + 1, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 2, y + sY - 2, 1, 1, 0x00f000f0, outline);
		matrices.pop();
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		matrices.push();
		drawBeveledPanel(matrices, provider, layer, x, y, sX, sY, 0x00f000f0, topleft, panel, bottomright);
		matrices.pop();
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		matrices.push();
		drawBeveledPanel(matrices, provider, Layers.Companion.flat(), x, y, sX, sY, 0x00f000f0, topleft, panel, bottomright);
		matrices.pop();
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color topleft, Color panel, Color bottomright) {
		matrices.push();
		drawQuad(matrices, provider, layer, x, y, sX, sY, light, panel);
		drawQuad(matrices, provider, layer, x, y, sX, 1, light, topleft);
		drawQuad(matrices, provider, layer, x, y + 1, 1, sY - 1, light, topleft);
		drawQuad(matrices, provider, layer, x + sX - 1, y + 1, 1, sY - 1, light, bottomright);
		drawQuad(matrices, provider, layer, x, y + sY - 1, sX - 1, 1, light, bottomright);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.Companion.of(0xFFFFFFFF), texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, Layers.Companion.get(texture), x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.Companion.of(0xFFFFFFFF), texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color color, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color color, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, Layers.Companion.get(texture), x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color color, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, light, color, texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, int light, Color color, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, Layers.Companion.get(texture), x, y, sX, sY, 0, 0, 1, 1, light, color, texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		matrices.push();
		drawTexturedQuad(matrices, provider, Layers.Companion.get(texture), x, y, sX, sY, u0, v0, u1, v1, light, color, texture);
		matrices.pop();
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		matrices.push();

		getTextureManager().bindTexture(texture);

		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), x, y + sY, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x, y, 0).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();

		matrices.pop();
	}

	public static TextureManager getTextureManager() {
		return MinecraftClient.getInstance().getTextureManager();
	}

	public static ItemRenderer getDefaultItemRenderer() {
		return MinecraftClient.getInstance().getItemRenderer();
	}

	public static TextRenderer getDefaultTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}
}
