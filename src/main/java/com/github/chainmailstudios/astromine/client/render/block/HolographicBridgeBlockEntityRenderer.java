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
		BlockState b = entity.getWorld().getBlockState(entity.getPos());

		if (entity.hasChild() && b.getBlock() instanceof HorizontalFacingBlock) {
			Vec3i pA = entity.getPos();

			Direction d = b.get(HorizontalFacingBlock.FACING);

			int oX = d == Direction.NORTH ? 1 : 0;
			int oZ = d == Direction.WEST ? 1 : 0;

			Collection<Vector3f> s = entity.segments;

			if (s.size() == 0) return;

			Vector3f o = s.iterator().next();
			Vector3f p = o;

			matrices.push();

			VertexConsumer c = provider.getBuffer(InterfaceLayer.getInterface());

			final Color cS = Color.of("0x7e2Fd3da");
			final Color cE = Color.of("0x7e2Fd3da");

			for (Vector3f v : s) {
				if (v != o) {
					float xA = v.getX() - pA.getX();
					float xB = p.getX() - pA.getX();

					float yA = v.getY() - pA.getY();
					float yB = p.getY() - pA.getY();

					float zA = v.getZ() - pA.getZ();
					float zB = p.getZ() - pA.getZ();

					c.vertex(matrices.peek().getModel(), xA, yA, zA).color(cS.R, cS.G, cS.B, cS.A).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xB, yB, zB).color(cS.R, cS.G, cS.B, cS.A).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xB + oX, yB, zB + oZ).color(cE.R, cE.G, cE.B, cE.A).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xA + oX, yA, zA + oZ).color(cE.R, cE.G, cE.B, cE.A).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
				}
				p = v;
			}

			matrices.pop();
		}
	}
}
