package com.github.chainmailstudios.astromine.common.item.base;

import com.github.chainmailstudios.astromine.common.utilities.EnergyUtilities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
		tooltip.add(new LiteralText(EnergyUtilities.toDecimalString(energyHandler.getEnergy()) + " ").append(new TranslatableText("text.astromine.energy")));
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
