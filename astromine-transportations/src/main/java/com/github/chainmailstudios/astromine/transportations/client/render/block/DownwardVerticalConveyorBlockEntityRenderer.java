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

package com.github.chainmailstudios.astromine.transportations.client.render.block;

import com.github.chainmailstudios.astromine.transportations.common.block.entity.DownVerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DownwardVerticalConveyorBlockEntityRenderer extends BlockEntityRenderer<DownVerticalConveyorBlockEntity> implements ConveyorRenderer<DownVerticalConveyorBlockEntity> {
	public DownwardVerticalConveyorBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(DownVerticalConveyorBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int i1) {
		int speed = ((Conveyor) blockEntity.getBlockState().getBlock()).getSpeed();
		ConveyorTypes type = ((Conveyor) blockEntity.getBlockState().getBlock()).getType();
		boolean conveyor = blockEntity.getBlockState().getValue(ConveyorProperties.CONVEYOR);
		boolean front = blockEntity.getBlockState().getValue(ConveyorProperties.FRONT);

		Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

		if (conveyor && blockEntity.getItemComponent().isEmpty()) {
			matrixStack.pushPose();
			renderSupport(blockEntity, type, -1, 16, 0, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		} else if (conveyor && !front && !blockEntity.getItemComponent().isEmpty() && blockEntity.getPosition() > speed) {
			float position = (blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks);

			matrixStack.pushPose();
			if (direction == Direction.NORTH) {
				matrixStack.translate(0, 0, (position / speed) - 2);
			} else if (direction == Direction.SOUTH) {
				matrixStack.translate(0, 0, -(position / speed) + 2);
			} else if (direction == Direction.EAST) {
				matrixStack.translate(-(position / speed) + 2, 0, 0);
			} else if (direction == Direction.WEST) {
				matrixStack.translate((position / speed) - 2, 0, 0);
			}
			renderSupport(blockEntity, type, -1, 16, 0, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		} else if (conveyor && front && !blockEntity.getItemComponent().isEmpty() && blockEntity.getHorizontalPosition() > 0) {
			float horizontalPosition = (blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks);

			matrixStack.pushPose();
			if (direction == Direction.NORTH) {
				matrixStack.translate(0, 0, (horizontalPosition / speed) - 1);
			} else if (direction == Direction.SOUTH) {
				matrixStack.translate(0, 0, -(horizontalPosition / speed) + 1);
			} else if (direction == Direction.EAST) {
				matrixStack.translate(-(horizontalPosition / speed) + 1, 0, 0);
			} else if (direction == Direction.WEST) {
				matrixStack.translate((horizontalPosition / speed) - 1, 0, 0);
			}
			renderSupport(blockEntity, type, -1, 16, 0, matrixStack, vertexConsumerProvider);
			matrixStack.popPose();
		}

		if (!blockEntity.getLevel().getBlockState(blockEntity.getBlockPos()).isAir() && !blockEntity.getItemComponent().isEmpty()) {
			ItemStack stack = blockEntity.getItemComponent().getFirst();
			float position = -(blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks);
			float horizontalPosition = (blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks);

			if (front && horizontalPosition > 0) {} else {
				renderSupport(blockEntity, type, position, speed, horizontalPosition, matrixStack, vertexConsumerProvider);
			}

			renderItem(blockEntity, stack, position, speed, horizontalPosition, type, matrixStack, vertexConsumerProvider);
		}
	}
}
