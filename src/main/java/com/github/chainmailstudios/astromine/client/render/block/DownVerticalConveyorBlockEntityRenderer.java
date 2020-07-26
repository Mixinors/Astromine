package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.conveyor.ConveyorProperties;
import com.github.chainmailstudios.astromine.common.block.conveyor.entity.DownVerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyor;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.ConveyorType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class DownVerticalConveyorBlockEntityRenderer extends BlockEntityRenderer<DownVerticalConveyorBlockEntity> implements ConveyorRenderer<DownVerticalConveyorBlockEntity> {
    public DownVerticalConveyorBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(DownVerticalConveyorBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
        int speed = ((Conveyor) blockEntity.getCachedState().getBlock()).getSpeed();
        ConveyorType type = ((Conveyor) blockEntity.getCachedState().getBlock()).getType();
        boolean conveyor = blockEntity.getCachedState().get(ConveyorProperties.CONVEYOR);
        boolean front = blockEntity.getCachedState().get(ConveyorProperties.FRONT);

		Direction direction = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);

        if (conveyor && blockEntity.isEmpty()) {
            matrixStack.push();
            renderSupport(blockEntity, type, -1, 16, 0, matrixStack, vertexConsumerProvider);
            matrixStack.pop();
        } else if (conveyor && !front && !blockEntity.isEmpty() && blockEntity.getPosition() > speed) {
            float position = (blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks);

            matrixStack.push();
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
            matrixStack.pop();
        } else if (conveyor && front && !blockEntity.isEmpty() && blockEntity.getHorizontalPosition() > 0) {
            float horizontalPosition = (blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks);

            matrixStack.push();
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
            matrixStack.pop();
        }

        if (!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isAir() && !blockEntity.isEmpty()) {
            ItemStack stack = blockEntity.getStack();
            float position = -(blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks);
            float horizontalPosition = (blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks);

            if (front && horizontalPosition > 0) {

            } else {
                renderSupport(blockEntity, type, position, speed, horizontalPosition, matrixStack, vertexConsumerProvider);
            }

            renderItem(blockEntity, stack, position, speed, horizontalPosition, type, matrixStack, vertexConsumerProvider);
        }
    }
}
