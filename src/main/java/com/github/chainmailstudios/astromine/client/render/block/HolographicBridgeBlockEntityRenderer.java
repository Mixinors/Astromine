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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.common.block.entity.HolographicBridgeProjectorBlockEntity;
import spinnery.client.render.layer.SpinneryLayers;
import spinnery.widget.api.Color;

import java.util.Collection;

public class HolographicBridgeBlockEntityRenderer extends BlockEntityRenderer<HolographicBridgeProjectorBlockEntity> {
	public HolographicBridgeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(HolographicBridgeProjectorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay) {
		BlockState b = entity.getWorld().getBlockState(entity.getPos());

		if(!(b.getBlock() instanceof HolographicBridgeProjectorBlock))  {
			AstromineCommon.LOGGER.debug("Holo Bridge Projector BE exists in spot where it shouldn't! "+entity.getPos());
			return;
		}

		if (entity.hasChild()) {
			Vec3i pA = entity.getPos();

			Direction d = b.get(HorizontalFacingBlock.FACING);

			int oX = d == Direction.NORTH ? 1 : 0;
			int oZ = d == Direction.WEST ? 1 : 0;

			Collection<Vector3f> s = entity.segments;

			if (s == null || s.size() == 0) {
				return;
			}

			Vector3f o = s.iterator().next();
			Vector3f p = o;

			matrices.push();

			VertexConsumer c = provider.getBuffer(SpinneryLayers.getInterface());

			Color bC = entity.color;

			for (Vector3f v : s) {
				if (v != o) {
					float xA = v.getX() - pA.getX();
					float xB = p.getX() - pA.getX();

					float yA = v.getY() - pA.getY();
					float yB = p.getY() - pA.getY();

					float zA = v.getZ() - pA.getZ();
					float zB = p.getZ() - pA.getZ();

					c.vertex(matrices.peek().getModel(), xA, yA, zA).color(bC.R, bC.G, bC.B, bC.A).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xB, yB, zB).color(bC.R, bC.G, bC.B, bC.A).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xB + oX, yB, zB + oZ).color(bC.R, bC.G, bC.B, bC.A).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
					c.vertex(matrices.peek().getModel(), xA + oX, yA, zA + oZ).color(bC.R, bC.G, bC.B, bC.A).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(0x00f000f0).normal(matrices.peek().getNormal(), 0, 1, 0).next();
				}
				p = v;
			}

			matrices.pop();
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox(HolographicBridgeProjectorBlockEntity blockEntity) {
		return true;
	}
}
