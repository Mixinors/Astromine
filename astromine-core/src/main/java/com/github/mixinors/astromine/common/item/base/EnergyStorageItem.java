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

package com.github.mixinors.astromine.common.item.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;

/**
 * An {@link Item} with an attached {@link EnergyStorage}.
 */
public class EnergyStorageItem extends Item implements SimpleBatteryItem {
	private final long capacity;

	/** Instantiates an {@link EnergyStorageItem}s. */
	protected EnergyStorageItem(Item.Settings settings, long capacity) {
		super(settings);

		this.capacity = capacity;
	}

	/** Instantiates an {@link EnergyStorageItem}. */
	public static EnergyStorageItem ofCreative(Item.Settings settings) {
		return new EnergyStorageItem(settings, Long.MAX_VALUE);
	}

	/** Instantiates an {@link EnergyStorageItem}s. */
	public static EnergyStorageItem of(Settings settings, long size) {
		return new EnergyStorageItem(settings, size);
	}

	@Override
	public long getEnergyCapacity() {
		return capacity;
	}

	@Override
	public long getEnergyMaxInput() {
		return capacity;
	}

	@Override
	public long getEnergyMaxOutput() {
		return capacity;
	}

	/** Override behavior to return our progress. */
	@Override
	public int getItemBarStep(ItemStack stack) {
		if (getEnergyCapacity() == 0)
			return 0;
		return Math.round(13 * (SimpleBatteryItem.getStoredEnergyUnchecked(stack) / getEnergyCapacity()));
	}

	/** Override behavior to return true. */
	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	/** Override behavior to return a median red. */
	@Override
	public int getItemBarColor(ItemStack stack) {
		return 0x91261f;
	}

	/** Override behavior to add instances of {@link EnergyStorageItem}
	 * as {@link ItemStack}s to {@link ItemGroup}s with full energy. */
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);

		if (this.isIn(group)) {
			ItemStack stack = new ItemStack(this);

			setStoredEnergy(stack, getEnergyCapacity());

			stacks.add(stack);
		}
	}
}
