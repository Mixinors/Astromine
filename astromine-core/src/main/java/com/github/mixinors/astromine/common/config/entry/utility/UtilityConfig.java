package com.github.mixinors.astromine.common.config.entry.utility;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedEnergyConsumedProvider;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedEnergyStorageSizeProvider;
import com.github.mixinors.astromine.common.config.entry.provider.DefaultedSpeedProvider;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class UtilityConfig implements AMConfigEntry, DefaultedSpeedProvider, DefaultedEnergyStorageSizeProvider, DefaultedEnergyConsumedProvider {
	@Comment("The delay between actions of the utility. (smaller = faster)")
	public double delay = getDefaultSpeed();
	@Comment("The maximum energy able to be stored by this utility.")
	public long energyStorageSize = getDefaultEnergyStorageSize();
	@Comment("The energy consumed by each action of this utility.")
	public long energyConsumed = getDefaultEnergyConsumed();

	@Override
	public double getSpeed() {
		return delay;
	}

	@Override
	public long getEnergyStorageSize() {
		return energyStorageSize;
	}

	@Override
	public long getEnergyConsumed() {
		return energyConsumed;
	}
}
