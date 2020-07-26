package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.conveyor.entity.DoubleMachineBlockEntity;
import com.github.chainmailstudios.astromine.common.conveyor.ConveyorType;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class DoubleMachineEntityRenderer extends BlockEntityRenderer<DoubleMachineBlockEntity> implements ConveyorRenderer<DoubleMachineBlockEntity> {
    public DoubleMachineEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(DoubleMachineBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
        int speed = 16;
        Direction direction = blockEntity.getCachedState().get(HorizontalFacingBlock.FACING);

        if (!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isAir()) {
        	if (!blockEntity.getLeftStack().isEmpty()) {
				ItemStack leftStack = blockEntity.getLeftStack();

				float leftPosition = blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks;

				renderItem(blockEntity, direction.rotateYCounterclockwise(), leftStack, leftPosition, speed, 0, ConveyorType.NORMAL, matrixStack, vertexConsumerProvider);
			}

        	if (!blockEntity.getRightStack().isEmpty()) {
				ItemStack rightStack = blockEntity.getRightStack();

				float rightPosition = blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks;

				renderItem(blockEntity, direction.rotateYClockwise(), rightStack, rightPosition, speed, 0, ConveyorType.NORMAL, matrixStack, vertexConsumerProvider);
			}
        }
    }
}
