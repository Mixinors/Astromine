package com.github.chainmailstudios.astromine.client.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeProjectorBlockEntity;
import spinnery.client.render.layer.SpinneryLayers;
import spinnery.widget.api.Color;

import java.util.ArrayList;
import java.util.Collection;

public class HolographicBridgeBlockEntityRenderer extends BlockEntityRenderer<HolographicBridgeProjectorBlockEntity> {
	public HolographicBridgeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(HolographicBridgeProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		if (entity.hasChild()) {
			Vec3i pA = entity.getPos();

			Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING);

			int oX = direction == Direction.NORTH ? 1 : 0;
			int oZ = direction == Direction.WEST ? 1 : 0;

			if (entity.segments == null || entity.segments.isEmpty()) {
				return;
			}

			Vector3f start = entity.segments.get(0);
			Vector3f end = entity.segments.get(entity.segments.size() - 1);

			VertexConsumer consumer = provider.getBuffer(SpinneryLayers.getFlat());

			matrices.push();

			float xA = start.getX() - pA.getX();
			float xB = end.getX() - pA.getX();

			float yA = start.getY() - pA.getY();
			float yB = end.getY() - pA.getY();

			float zA = start.getZ() - pA.getZ();
			float zB = end.getZ() - pA.getZ();

			consumer.vertex(matrices.peek().getModel(), xA, yA, zA).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xB, yB, zB).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xB + oX, yB, zB + oZ).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xA + oX, yA, zA + oZ).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();

			matrices.pop();
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox(HolographicBridgeProjectorBlockEntity blockEntity) {
		return true;
	}
}
