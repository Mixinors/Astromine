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

package com.github.chainmailstudios.astromine.technologies.client.render.block;

import com.github.chainmailstudios.astromine.client.render.layer.Layer;
import com.github.chainmailstudios.astromine.technologies.common.block.HolographicBridgeProjectorBlock;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.HolographicBridgeProjectorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HolographicBridgeBlockEntityRenderer extends BlockEntityRenderer<HolographicBridgeProjectorBlockEntity> {
	public HolographicBridgeBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(HolographicBridgeProjectorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource provider, int light, int overlay) {
		BlockState state = entity.getLevel().getBlockState(entity.getBlockPos());

		if (!(state.getBlock() instanceof HolographicBridgeProjectorBlock)) {
			return;
		}

		if (entity.hasChild()) {
			Vec3i pA = entity.getBlockPos();

			Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);

			int offsetX = direction == Direction.NORTH ? 1 : 0;
			int offsetZ = direction == Direction.WEST ? 1 : 0;

			if (entity.segments == null || entity.segments.size() == 0) {
				return;
			}

			Vector3f start = entity.segments.get(0);
			Vector3f end = entity.segments.get(entity.segments.size() - 1);

			matrices.pushPose();

			VertexConsumer consumer = provider.getBuffer(Layer.getHolographicBridge());

			float xA = end.x() - pA.getX();
			float xB = start.x() - pA.getX();

			float yA = end.y() - pA.getY();
			float yB = start.y() - pA.getY();

			float zA = end.z() - pA.getZ();
			float zB = start.z() - pA.getZ();

			consumer.vertex(matrices.last().pose(), xA, yA, zA).color(entity.color.getR(), entity.color.getG(), entity.color.getB(), entity.color.getA()).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x00f000f0).normal(matrices.last().normal(), 0, 1, 0).endVertex();
			consumer.vertex(matrices.last().pose(), xB, yB, zB).color(entity.color.getR(), entity.color.getG(), entity.color.getB(), entity.color.getA()).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x00f000f0).normal(matrices.last().normal(), 0, 1, 0).endVertex();
			consumer.vertex(matrices.last().pose(), xB + offsetX, yB, zB + offsetZ).color(entity.color.getR(), entity.color.getG(), entity.color.getB(), entity.color.getA()).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x00f000f0).normal(matrices.last().normal(),
				0, 1, 0).endVertex();
			consumer.vertex(matrices.last().pose(), xA + offsetX, yA, zA + offsetZ).color(entity.color.getR(), entity.color.getG(), entity.color.getB(), entity.color.getA()).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0x00f000f0).normal(matrices.last().normal(),
				0, 1, 0).endVertex();

			matrices.popPose();
		}
	}

	@Override
	public boolean shouldRenderOffScreen(HolographicBridgeProjectorBlockEntity blockEntity) {
		return true;
	}
}
