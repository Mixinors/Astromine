package com.github.chainmailstudios.astromine.transportations.common.block;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.component.general.base.FluidComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.common.utilities.TextUtilities;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.FluidFilterBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ItemFilterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class FluidFilterBlock extends HorizontalFacingBlockWithEntity {
    public FluidFilterBlock(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            FluidFilterBlockEntity blockEntity = (FluidFilterBlockEntity) world.getBlockEntity(pos);

            ItemStack stack = player.getItemInHand(hand);

            Fluid fluid = FluidComponent.get(stack).getFirst().getFluid();

            if (stack.isEmpty()) {
                blockEntity.setFilterFluid(Fluids.EMPTY);
            } else {
                blockEntity.setFilterFluid(fluid);
            }

            player.displayClientMessage(new TranslatableComponent("text.astromine.fluid_filter_use", TextUtilities.getFluid(fluid)), false);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public boolean hasScreenHandler() {
        return false;
    }

    @Override
    public BlockEntity createBlockEntity() {
        return new ItemFilterBlockEntity();
    }

    @Override
    public AbstractContainerMenu createScreenHandler(BlockState state, Level world, BlockPos pos, int syncId, Inventory playerInventory, Player player) {
        return null;
    }

    @Override
    public void populateScreenHandlerBuffer(BlockState state, Level world, BlockPos pos, ServerPlayer player, FriendlyByteBuf buffer) {}
}
