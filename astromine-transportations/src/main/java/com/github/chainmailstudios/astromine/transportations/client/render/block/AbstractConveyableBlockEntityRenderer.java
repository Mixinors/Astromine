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

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.transportations.common.block.entity.base.AbstractConveyableBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

public class AbstractConveyableBlockEntityRenderer extends BlockEntityRenderer<AbstractConveyableBlockEntity> implements ConveyorRenderer<AbstractConveyableBlockEntity> {
	public AbstractConveyableBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
	}

	@Override
	public void render(AbstractConveyableBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
		int speed = 16;
		Direction direction = blockEntity.getCachedState().get(HorizontalFacingBlock.FACING);

		if (!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isAir()) {
			if (!blockEntity.getLeftStack().isEmpty()) {
				ItemStack leftStack = blockEntity.getLeftStack();

				float leftPosition = blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks;

				renderItem(blockEntity, direction.rotateYCounterclockwise(), leftStack, leftPosition, speed, 0, ConveyorTypes.NORMAL, matrixStack, vertexConsumerProvider);
			}

			if (!blockEntity.getRightStack().isEmpty()) {
				ItemStack rightStack = blockEntity.getRightStack();

				float rightPosition = blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks;

				renderItem(blockEntity, direction.rotateYClockwise(), rightStack, rightPosition, speed, 0, ConveyorTypes.NORMAL, matrixStack, vertexConsumerProvider);
			}
		}
	}
}
