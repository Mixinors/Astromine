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

package com.github.chainmailstudios.astromine.common.block.entity.base;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.compatibility.ItemInventoryFromInventoryComponent;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import java.util.Set;
import java.util.stream.IntStream;

public abstract class DefaultedEnergyItemBlockEntity extends DefaultedEnergyBlockEntity implements ComponentProvider, InventoryProvider, SidedInventory, BlockEntityClientSerializable {
	protected final ItemInventoryComponent itemComponent = createItemComponent();

	protected abstract ItemInventoryComponent createItemComponent();

	private final ItemInventoryFromInventoryComponent inventory = () -> itemComponent;

	public DefaultedEnergyItemBlockEntity(Block energyBlock, BlockEntityType<?> type) {
		super(energyBlock, type);

		addComponent(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT, itemComponent);
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.getStack(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return inventory.removeStack(slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return inventory.removeStack(slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.setStack(slot, stack);
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public int count(Item item) {
		return inventory.count(item);
	}

	@Override
	public boolean containsAny(Set<Item> items) {
		return inventory.containsAny(items);
	}

	@Override
	public int[] getAvailableSlots(Direction direction) {
		if (!transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction).isNone()) {
			return IntStream.rangeClosed(0, inventory.size() - 1).toArray();
		} else {
			return new int[0];
		}
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction direction) {
		if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction).canInsert()) {
			return getSidedComponent(direction, AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).canInsert(direction, stack, slot);
		} else {
			return false;
		}
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction direction) {
		if (transferComponent.get(AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).get(direction).canExtract()) {
			return getSidedComponent(direction, AstromineComponentTypes.ITEM_INVENTORY_COMPONENT).canExtract(direction, stack, slot);
		} else {
			return false;
		}
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		return this;
	}
}
