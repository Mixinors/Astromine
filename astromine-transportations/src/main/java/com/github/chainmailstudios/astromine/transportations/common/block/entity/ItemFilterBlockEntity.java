package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.HorizontalFacingBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.google.common.primitives.Ints.max;

public class ItemFilterBlockEntity extends ConveyorBlockEntity implements Conveyable {
    private ItemStack filterStack = ItemStack.EMPTY;
    private boolean filterTag = false;

    public ItemFilterBlockEntity() {
        super(AstromineTransportationsBlockEntityTypes.ITEM_FILTER);
    }

    @Override
    public ItemComponent createItemComponent() {
        return new SimpleItemComponent(1) {
            @Override
            public ItemStack removeStack(int slot) {
                position = 0;
                prevPosition = 0;

                return super.removeStack(slot);
            }
        }.withInsertPredicate(((direction, stack, slot) -> {
            Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            return filterStack.isEmpty() || (direction == facing && filterTag ? StackUtilities.areItemsAndTagsEqual(filterStack, stack) : ItemStack.isSameIgnoreDurability(filterStack, stack));
        })).withExtractPredicate(((direction, stack, slot) -> {
            Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

            return direction == facing.getOpposite();
        })).withListener((inventory) -> {
            if (level != null && !level.isClientSide) {
                sendPacket((ServerLevel) level, save(new CompoundTag()));
            }
        });
    }

    @Override
    public boolean accepts(ItemStack stack) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        return getItemComponent().canInsert(facing, stack, 0);
    }

    @Override
    public boolean canInsert(Direction direction) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        return direction == facing.getOpposite();
    }

    @Override
    public boolean canExtract(Direction direction, ConveyorTypes type) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        return direction == facing;
    }

    @Override
    public void give(ItemStack stack) {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        ItemComponent.of(stack).into(getItemComponent(), stack.getCount(), null, facing);
    }

    public ItemStack getFilterStack() {
        return filterStack;
    }

    public void setFilterStack(ItemStack filterStack) {
        this.filterStack = filterStack;
    }

    public boolean isFilterTag() {
        return filterTag;
    }

    public void setFilterTag(boolean filterTag) {
        this.filterTag = filterTag;
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        filterStack = ItemStack.of(tag.getCompound("filterStack"));
        filterTag = tag.getBoolean("filterTag");
        super.load(state, tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("filterStack", filterStack.save(new CompoundTag()));
        tag.putBoolean("filterTag", filterTag);
        return super.save(tag);
    }
}
