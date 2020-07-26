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

import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergyTier;

import java.util.List;

public class EnergyVolumeItem extends Item implements EnergyHolder {
	private double maxAmount;

	public EnergyVolumeItem(Settings settings) {
		super(settings);
	}

	public EnergyVolumeItem(Settings settings, double maxAmount) {
		super(settings);
		this.maxAmount = maxAmount;
	}

	public static EnergyVolumeItem of(Settings settings, double maxAmount) {
		EnergyVolumeItem item = new EnergyVolumeItem(settings);
		item.maxAmount = maxAmount;
		return item;
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
}
