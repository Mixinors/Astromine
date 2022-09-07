package com.github.mixinors.astromine.client.util;

import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

import static dev.vini2003.hammer.core.api.client.util.DrawingUtil.DEFAULT_LIGHT;
import static dev.vini2003.hammer.core.api.client.util.DrawingUtil.DEFAULT_OVERLAY;

public class DrawingUtil {
	public static void drawLine(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			Color color,
			RenderLayer layer
	) {
		var consumer = provider.getBuffer(layer);
		
		consumer.vertex(x1, y1, z1).color(color.getR(), color.getG(), color.getB(), color.getA()).normal(0.0F, 1.0F, 0.0F).next();
		consumer.vertex(x2, y2, z1).color(color.getR(), color.getG(), color.getB(), color.getA()).normal(0.0F, 1.0F, 0.0F).next();
		
		consumer.vertex(x1, y1, z1).color(color.getR(), color.getG(), color.getB(), color.getA()).normal(1.0F, 0.0F, 0.0F).next();
		consumer.vertex(x2, y2, z1).color(color.getR(), color.getG(), color.getB(), color.getA()).normal(1.0F, 0.0F, 0.0F).next();
	}
	
	public static void drawSquare(
			MatrixStack matrices,
			VertexConsumerProvider provider,
			float x, float y, float z,
			float width, float height,
			Color color,
			RenderLayer layer
	) {
		DrawingUtil.drawLine(matrices, provider, x, y, z, x + width, y, z, color, layer);
		DrawingUtil.drawLine(matrices, provider, x, y + height, z, x + width, y + height, z, color, layer);
		DrawingUtil.drawLine(matrices, provider, x, y, z, x, y + height, z, color, layer);
		DrawingUtil.drawLine(matrices, provider, x + width, y, z, x + width, y + height, z, color, layer);
	}
	
	public static void drawBody(
			VertexConsumerProvider provider,
			float x, float y, float z,
			float width, float height, float depth,
			float angle,
			boolean tidalLocked,
			Color color,
			RenderLayer layerUp,
			RenderLayer layerDown,
			RenderLayer layerNorth,
			RenderLayer layerSouth,
			RenderLayer layerEast,
			RenderLayer layerWest
	) {
		var consumer = provider.getBuffer(layerDown);
		
		var stack = new MatrixStack();
		
		stack.translate(x, y, z);
		
		stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
		
		if (tidalLocked) {
			stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - 360.0F + (float) Math.toDegrees(angle)));
		} else {
			stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45.0F - (float) Math.toDegrees(angle)));
		}
		
		var peek = stack.peek();
		
		// Down
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerUp);
		
		// Up
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerNorth);
		
		// North
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerSouth);
		
		// South
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerWest);
		
		// West
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(), -(width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		
		consumer = provider.getBuffer(layerEast);
		
		// East
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F), -(depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 0.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F),  (height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(1.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
		consumer.vertex(peek.getPositionMatrix(),  (width / 2.0F), -(height / 2.0F),  (depth / 2.0F)).color(color.getR(), color.getG(), color.getB(), color.getA()).texture(0.0F, 1.0F).overlay(DEFAULT_OVERLAY).light(DEFAULT_LIGHT).normal(0.0F, 0.0F, 0.0F).next();
	}
}

