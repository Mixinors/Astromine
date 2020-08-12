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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.common.utilities.EnergyCapacityProvider;
import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import me.shedaniel.cloth.api.durability.bar.DurabilityBarItem;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class EnergyVolumeItem extends Item implements EnergyHolder, DurabilityBarItem, EnergyCapacityProvider {
	private double maxAmount;
	private boolean creative;

	public EnergyVolumeItem(Settings settings, double maxAmount, boolean creative) {
		super(settings);
		this.maxAmount = maxAmount;
		this.creative = creative;
	}

	public static EnergyVolumeItem ofCreative(Settings settings) {
		return new EnergyVolumeItem(settings, Integer.MAX_VALUE, true);
	}

	public static EnergyVolumeItem of(Settings settings, double maxAmount) {
		return new EnergyVolumeItem(settings, maxAmount, false);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);

		EnergyHandler energyHandler = Energy.of(stack);
		tooltip.add(EnergyUtilities.compoundDisplayColored(energyHandler.getEnergy(), energyHandler.getMaxStored()));
	}

	@Override
	public double getMaxStoredPower() {
		return maxAmount;
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
		return !isCreative();
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
		return maxAmount;
	}

	@Override
	public boolean isCreative() {
		return creative;
	}
}
