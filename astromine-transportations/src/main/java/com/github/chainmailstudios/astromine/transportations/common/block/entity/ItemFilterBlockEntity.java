package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.component.general.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;

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
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

			return filterStack.isEmpty() || (direction == facing && filterTag ? StackUtilities.areItemsAndTagsEqual(filterStack, stack) : ItemStack.areItemsEqual(filterStack, stack));
		})).withExtractPredicate(((direction, stack, slot) -> {
			Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

			return direction == facing.getOpposite();
		})).withListener((inventory) -> {
			if (world != null && !world.isClient) {
				sendPacket((ServerWorld) world, toTag(new CompoundTag()));
			}
		});
	}

	@Override
	public boolean accepts(ItemStack stack) {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

		return getItemComponent().canInsert(facing, stack, 0);
	}

	@Override
	public boolean canInsert(Direction direction) {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

		return direction == facing.getOpposite();
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

		return direction == facing;
	}

	@Override
	public void give(ItemStack stack) {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

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
	public void fromTag(BlockState state, CompoundTag tag) {
		filterStack = ItemStack.fromTag(tag.getCompound("filterStack"));
		filterTag = tag.getBoolean("filterTag");
		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("filterStack", filterStack.toTag(new CompoundTag()));
		tag.putBoolean("filterTag", filterTag);
		return super.toTag(tag);
	}
}
