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

package com.github.chainmailstudios.astromine.common.utilities.capability.inventory;

import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.InventoryFromItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.TransportUtilities;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.stream.IntStream;

public interface ComponentInventoryProvider extends InventoryProvider, SidedInventory, InventoryFromItemComponent {
	@Override
	default SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}

	default boolean isSideOpenForItems(int slot, Direction direction, boolean inserting) {
		return inserting ? TransportUtilities.isInsertingItem(BlockEntityTransferComponent.get(this), direction, true) && getItemInputSlots().contains(slot) : TransportUtilities.isExtractingItem(BlockEntityTransferComponent.get(this), direction, true) && getItemOutputSlots()
			.contains(slot);
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

	@Override
	default ItemComponent getItemComponent() {
		return ItemComponent.get(this);
	}
}
