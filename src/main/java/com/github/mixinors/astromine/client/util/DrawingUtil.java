package com.github.mixinors.astromine.client.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import static dev.vini2003.hammer.core.api.client.util.DrawingUtil.DEFAULT_LIGHT;
import static dev.vini2003.hammer.core.api.client.util.DrawingUtil.DEFAULT_OVERLAY;

public class DrawingUtil {
	public static void drawCube(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			float x, float y, float z,
			float width, float height, float depth,
			Color color,
			RenderLayer layerUp,
			RenderLayer layerDown,
			RenderLayer layerNorth,
			RenderLayer layerSouth,
			RenderLayer layerEast,
			RenderLayer layerWest
	) {
		var consumer = provider.getBuffer(layerDown);
		
		var peek = matrices.peek();
		
		// Down
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerUp);
		
		// Up
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerNorth);
		
		// North
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerSouth);
		
		// South
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerWest);
		
		// West
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x - (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerEast);
		
		// East
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z - (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y + (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), x + (width / 2.0F), y - (height / 2.0F), z + (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
	}
}

