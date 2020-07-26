package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.conveyor.entity.VerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyor;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.ConveyorType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class VerticalConveyorBlockEntityRenderer extends BlockEntityRenderer<VerticalConveyorBlockEntity> implements ConveyorRenderer<VerticalConveyorBlockEntity> {
    public VerticalConveyorBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(VerticalConveyorBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
        int speed = ((Conveyor) blockEntity.getCachedState().getBlock()).getSpeed();
        ConveyorType type = ((Conveyor) blockEntity.getCachedState().getBlock()).getType();

        if (!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isAir() && !blockEntity.isEmpty()) {
            ItemStack stack = blockEntity.getStack();
            float position = blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks;
            float horizontalPosition = blockEntity.getRenderAttachmentData()[3] + (blockEntity.getRenderAttachmentData()[2] - blockEntity.getRenderAttachmentData()[3]) * partialTicks;

            renderSupport(blockEntity, type, position, speed, horizontalPosition, matrixStack, vertexConsumerProvider);

            renderItem(blockEntity, stack, position, speed, horizontalPosition, type, matrixStack, vertexConsumerProvider);
        }
    }
}
