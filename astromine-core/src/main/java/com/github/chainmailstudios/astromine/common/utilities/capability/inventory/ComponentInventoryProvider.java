/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.chainmailstudios.astromine.common.utilities.capability.inventory;

import com.github.chainmailstudios.astromine.common.component.block.entity.TransferComponent;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.compatibility.InventoryFromItemComponent;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.stream.IntStream;

public interface ComponentInventoryProvider extends InventoryProvider, SidedInventory, InventoryFromItemComponent {
	/** Returns this inventory. */
	@Override
	default SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}

	/** Returns this inventory's input slots, defaulting to all slots. */
	default IntSet getItemInputSlots() {
		return new IntArraySet(IntStream.range(0, size()).toArray());
	}

	/** Returns this inventory's output slots, defaulting to all slots. */
	default IntSet getItemOutputSlots() {
		return new IntArraySet(IntStream.range(0, size()).toArray());
	}

	/** Override behavior to redirect calls to this inventory's
	 * {@link TransferComponent}, if present. */
	@Override
	default boolean canInsert(int slot, ItemStack stack, Direction direction) {
		TransferComponent transferComponent = TransferComponent.get(this);
		
		if (transferComponent != null) {
			return transferComponent.hasItem() && transferComponent.getItem(direction).canInsert() && getItemComponent().canInsert(direction, stack, slot);
		} else {
			return getItemComponent().canInsert(direction, stack, slot);
		}
	}

	/** Override behavior to redirect calls to this inventory's
	 * {@link TransferComponent}, if present. */
	@Override
	default boolean canExtract(int slot, ItemStack stack, Direction direction) {
		TransferComponent transferComponent = TransferComponent.get(this);

		if (transferComponent != null) {
			return transferComponent.hasItem() && transferComponent.getItem(direction).canExtract() && getItemComponent().canExtract(direction, stack, slot);
		} else {
			return getItemComponent().canExtract(direction, stack, slot);
		}
	}

	/** Override behavior to redirect calls to this inventory's
	 * {@link TransferComponent}, if present. */
	@Override
	default int[] getAvailableSlots(Direction direction) {
		TransferComponent transferComponent = TransferComponent.get(this);

		if (transferComponent != null) {
			if (transferComponent.hasItem()) {
				if (transferComponent.getItem(direction).canInsert()) {
					return IntStream.range(0, size()).filter(slot -> getItemInputSlots().contains(slot)).toArray();
				} else if (transferComponent.getItem(direction).canExtract()) {
					return IntStream.range(0, size()).filter(slot -> getItemOutputSlots().contains(slot)).toArray();
				}
			}
		}

		return new int[] { 0 };
	}

	/** Override behavior to return this as an {@link ItemComponent}. */
	@Override
	default ItemComponent getItemComponent() {
		return ItemComponent.get(this);
	}
}
