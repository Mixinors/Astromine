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

package com.github.chainmailstudios.astromine.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import com.github.chainmailstudios.astromine.common.utilities.capability.energy.EnergyCapacityProvider;
import me.shedaniel.cloth.api.durability.bar.DurabilityBarItem;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

public class EnergyBlockItem extends BlockItem implements EnergyHolder, DurabilityBarItem, EnergyCapacityProvider {
	private final EnergyCapacityProvider energyCapacityProvider;

	private EnergyBlockItem(Block block, Settings settings) {
		super(block, settings);
		this.energyCapacityProvider = (EnergyCapacityProvider) block;
	}

	public static BlockItem create(Block block, Settings settings) {
		return new EnergyBlockItem(block, settings);
	}

	@Override
	public double getMaxStoredPower() {
		return energyCapacityProvider.getEnergyCapacity();
	}

	@Override
	public EnergyTier getTier() {
		return EnergyTier.INFINITE;
	}

	@Override
	public int getDurabilityBarColor(ItemStack stack) {
		return 0x91261f;
	}

	@Override
	public double getDurabilityBarProgress(ItemStack itemStack) {
		if (!Energy.valid(itemStack) || getMaxStoredPower() == 0)
			return 0;
		return 1 - Energy.of(itemStack).getEnergy() / getMaxStoredPower();
	}

	@Override
	public boolean hasDurabilityBar(ItemStack itemStack) {
		return !isCreative() && Energy.of(itemStack).getEnergy() > 0;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (!isCreative() && this.isIn(group)) {
			ItemStack itemStack = new ItemStack(this);
			Energy.of(itemStack).set(getMaxStoredPower());
			stacks.add(itemStack);
		}
	}

	@Override
	public double getEnergyCapacity() {
		return energyCapacityProvider.getEnergyCapacity();
	}

	@Override
	public boolean isCreative() {
		return energyCapacityProvider.isCreative();
	}
}
