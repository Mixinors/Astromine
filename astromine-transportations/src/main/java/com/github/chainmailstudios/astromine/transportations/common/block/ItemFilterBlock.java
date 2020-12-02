package com.github.chainmailstudios.astromine.transportations.common.block;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ItemFilterBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyableBlock;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ItemFilterBlock extends ConveyorBlock {
    public ItemFilterBlock(Properties settings) {
        super(settings, AstromineConfig.get().eliteConveyorSpeed);
    }

    @Override
    public ConveyorTypes getType() {
        return ConveyorTypes.NORMAL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockGetter world) {
        return new ItemFilterBlockEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockView, BlockPos blockPos, CollisionContext entityContext) {
        return Shapes.block();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            ItemFilterBlockEntity blockEntity = (ItemFilterBlockEntity) world.getBlockEntity(pos);

            ItemStack stack = player.getItemInHand(hand);

            if (stack.isEmpty()) {
                blockEntity.setFilterTag(false);
                blockEntity.setFilterStack(ItemStack.EMPTY);
            } else {
                if (StackUtilities.areItemsAndTagsEqual(stack, blockEntity.getFilterStack())) {
                    blockEntity.setFilterTag(!blockEntity.isFilterTag());
                } else {
                    blockEntity.setFilterTag(false);
                    blockEntity.setFilterStack(stack.copy());
                }
            }

            player.displayClientMessage(new TranslatableComponent("text.astromine.item_filter_use", new TranslatableComponent(stack.getItem().getDescriptionId()).getString(), blockEntity.isFilterTag() ? "Yes" : "No"), false);
        }

        return InteractionResult.SUCCESS;
    }
}
