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

package com.github.chainmailstudios.astromine.common.item.base;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import me.shedaniel.cloth.api.durability.bar.DurabilityBarItem;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

public class EnergyVolumeItem extends BaseVolumeItem<EnergyVolume> implements EnergyHolder, DurabilityBarItem {
	private final double size;

	public EnergyVolumeItem(Item.Settings settings, double size) {
		super(settings);

		this.size = size;
	}

	public static EnergyVolumeItem ofCreative(Item.Settings settings) {
		return new EnergyVolumeItem(settings, Double.MAX_VALUE);
	}

	public static EnergyVolumeItem of(Settings settings, double size) {
		return new EnergyVolumeItem(settings, size);
	}

	public double getSize() {
		return size;
	}

	@Override
	public double getMaxStoredPower() {
		return getSize();
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.INSANE;
	}

	@Override
	public double getDurabilityBarProgress(ItemStack stack) {
		if (!Energy.valid(stack) || getMaxStoredPower() == 0)
			return 0;
		return 1 - Energy.of(stack).getEnergy() / getMaxStoredPower();
	}

	@Override
	public boolean hasDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityBarColor(ItemStack stack) {
		return 0x91261f;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			ItemStack stack = new ItemStack(this);
			Energy.of(stack).set(getMaxStoredPower());
			stacks.add(stack);
		}
	}
}
