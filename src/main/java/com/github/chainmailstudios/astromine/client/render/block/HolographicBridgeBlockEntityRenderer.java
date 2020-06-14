package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeBlockEntity;
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
import spinnery.client.render.layer.InterfaceLayer;
import spinnery.widget.api.Color;

import java.util.Collection;

public class HolographicBridgeBlockEntityRenderer extends BlockEntityRenderer<HolographicBridgeBlockEntity> {
	public HolographicBridgeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public boolean rendersOutsideBoundingBox(HolographicBridgeBlockEntity blockEntity) {
		return true;
	}

	@Override
	public void render(HolographicBridgeBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		BlockState blockState = entity.getWorld().getBlockState(entity.getPos());

		if (entity.hasChild() && blockState.getBlock() instanceof HorizontalFacingBlock) {
			Vec3i posA = entity.getPos();
			Vec3i posB = entity.getChild().getPos();

			Direction direction = blockState.get(HorizontalFacingBlock.FACING);

			int oX = direction == Direction.NORTH ? 1 : 0;
			int oZ = direction == Direction.WEST ? 1 : 0;

			Collection<Vector3f> segments = entity.segments;

			if (segments.size() == 0) return;

			Vector3f origin = segments.iterator().next();
			Vector3f previous = origin;

			matrices.push();

			VertexConsumer consumer = provider.getBuffer(InterfaceLayer.getInterface());

			final Color colorStart = Color.of("0x7E2FD3DA");
			final Color colorEnd = Color.of("0x7E2FD3DA");

			for (Vector3f vector : segments) {
				if (vector != origin) {
					float xA = vector.getX() - posA.getX();
					float xB = previous.getX() - posA.getX();

					float yA = vector.getY() - posA.getY();
					float yB = previous.getY() - posA.getY();

					float zA = vector.getZ() - posA.getZ();
					float zB = previous.getZ() - posA.getZ();

					consumer.vertex(matrices.peek().getModel(), xA, yA, zA).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					consumer.vertex(matrices.peek().getModel(), xB, yB, zB).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					consumer.vertex(matrices.peek().getModel(), xB + oX, yB, zB + oZ).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					consumer.vertex(matrices.peek().getModel(), xA + oX, yA, zA + oZ).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
				}
				previous = vector;
			}

			matrices.pop();
		}
	}
}
