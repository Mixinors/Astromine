/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
		return new int[]{ 0 };
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
