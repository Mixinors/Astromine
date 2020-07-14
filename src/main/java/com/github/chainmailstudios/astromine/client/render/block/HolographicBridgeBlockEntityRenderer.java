package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.client.render.layer.Layers;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
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
		BlockState state = entity.getWorld().getBlockState(entity.getPos());

		if (!(state.getBlock() instanceof HolographicBridgeProjectorBlock)) {
			return;
		}

		if (entity.hasChild()) {
			Vec3i pA = entity.getPos();

			Direction direction = state.get(HorizontalFacingBlock.FACING);

			int offsetX = direction == Direction.NORTH ? 1 : 0;
			int offsetZ = direction == Direction.WEST ? 1 : 0;

			if (entity.segments == null || entity.segments.size() == 0) {
				return;
			}

			Vector3f start = entity.segments.get(0);
			Vector3f end = entity.segments.get(entity.segments.size() - 1);

			matrices.push();

			VertexConsumer consumer = provider.getBuffer(Layers.getHolographicBridge());

			float xA = end.getX() - pA.getX();
			float xB = start.getX() - pA.getX();

			float yA = end.getY() - pA.getY();
			float yB = start.getY() - pA.getY();

			float zA = end.getZ() - pA.getZ();
			float zB = start.getZ() - pA.getZ();

			consumer.vertex(matrices.peek().getModel(), xA, yA, zA).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xB, yB, zB).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xB + offsetX, yB, zB + offsetZ).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
			consumer.vertex(matrices.peek().getModel(), xA + offsetX, yA, zA + offsetZ).color(entity.color.R, entity.color.G, entity.color.B, entity.color.A).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();

			matrices.pop();
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox(HolographicBridgeProjectorBlockEntity blockEntity) {
		return true;
	}
}
