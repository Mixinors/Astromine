package com.github.chainmailstudios.astromine.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.SidingUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import java.util.stream.IntStream;

public interface BlockEntityWithItemInventory extends ComponentProvider, InventoryProvider, SidedInventory, ItemInventoryFromInventoryComponent {
	ItemInventoryComponent getItemComponent();

	@Override
	default SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}

	default boolean isSideOpenForItems(int slot, Direction direction, boolean inserting) {
		return inserting ? SidingUtilities.isInsertingItem((BlockEntity) this, getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT), direction, true) && getItemInputSlots().contains(slot) :
				SidingUtilities.isExtractingItem((BlockEntity) this, getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT), direction, true) && getItemOutputSlots().contains(slot);
	}

	default IntSet getItemInputSlots() {
		return new IntArraySet(IntStream.range(0, size()).toArray());
	}

	default IntSet getItemOutputSlots() {
		return new IntArraySet(IntStream.range(0, size()).toArray());
	}

	@Override
	default boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return isSideOpenForItems(slot, dir, true);
	}

	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return isSideOpenForItems(slot, dir, false);
	}

	@Override
	default int[] getAvailableSlots(Direction side) {
		return IntStream.range(0, size()).filter(slot -> isSideOpenForItems(slot, side, true) || isSideOpenForItems(slot, side, false)).toArray();
	}
}
