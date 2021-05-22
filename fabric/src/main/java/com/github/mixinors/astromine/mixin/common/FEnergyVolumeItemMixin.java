package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.item.base.EnergyVolumeItem;
import me.shedaniel.cloth.api.durability.bar.DurabilityBarItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

@Mixin(EnergyVolumeItem.class)
public abstract class FEnergyVolumeItemMixin implements EnergyStorage, DurabilityBarItem {
	@Shadow public abstract double getSize();
	
	/** Returns this item's size. */
	@Override
	public double getMaxStoredPower() {
		return getSize();
	}

	/** Override behavior to ignore TechReborn's energy tiers. */
	@Override
	public EnergyTier getTier() {
		return EnergyTier.INSANE;
	}

	/** Override behavior to return our progress. */
	@Override
	public double getDurabilityBarProgress(ItemStack stack) {
		if (!Energy.valid(stack) || getMaxStoredPower() == 0)
			return 0;
		
		return 1 - Energy.of(stack).getEnergy() / getMaxStoredPower();
	}

	/** Override behavior to return true. */
	@Override
	public boolean hasDurabilityBar(ItemStack stack) {
		return true;
	}

	/** Override behavior to return a median red. */
	@Override
	public int getDurabilityBarColor(ItemStack stack) {
		return 0x91261f;
	}
}
