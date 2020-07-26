package com.github.chainmailstudios.astromine.client.render.block;

import com.github.chainmailstudios.astromine.common.block.conveyor.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.common.conveyor.ConveyorType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class ConveyorBlockEntityRenderer extends BlockEntityRenderer<ConveyorBlockEntity> implements ConveyorRenderer<ConveyorBlockEntity> {
    public ConveyorBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        super(blockEntityRenderDispatcher);
    }

    @Override
    public void render(ConveyorBlockEntity blockEntity, float partialTicks, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
        int speed = ((Conveyor) blockEntity.getCachedState().getBlock()).getSpeed();
        ConveyorType type = ((Conveyor) blockEntity.getCachedState().getBlock()).getType();

        if (!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isAir() && !blockEntity.isEmpty()) {
            ItemStack stack = blockEntity.getStack();

            float position = blockEntity.getRenderAttachmentData()[1] + (blockEntity.getRenderAttachmentData()[0] - blockEntity.getRenderAttachmentData()[1]) * partialTicks;

            renderItem(blockEntity, stack, position, speed, 0, type, matrixStack, vertexConsumerProvider);
        }
    }
}
