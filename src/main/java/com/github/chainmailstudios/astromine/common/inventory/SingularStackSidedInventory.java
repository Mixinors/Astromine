package com.github.chainmailstudios.astromine.common.inventory;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 * <p>
 * Originally by Juuz
 */
public interface SingularStackSidedInventory extends SingularStackInventory, SidedInventory {
	Direction[] getInsertionSides();

	Direction[] getExtractionSides();

	@Override
	default int[] getAvailableSlots(Direction side) {
		return new int[]{0};
	}

	@Override
	default boolean canInsert(int slot, ItemStack stack, Direction dir) {
		boolean matchesSide = false;
		for (int i = 0; i < getInsertionSides().length; i++) {
			if (dir == getInsertionSides()[i]) {
				matchesSide = true;
				break;
			}
		}

		return isEmpty() && matchesSide;
	}

	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction dir) {
		boolean matchesSide = false;
		for (int i = 0; i < getExtractionSides().length; i++) {
			if (dir == getExtractionSides()[i]) {
				matchesSide = true;
				break;
			}
		}

		return !isEmpty() && matchesSide;
	}
}

