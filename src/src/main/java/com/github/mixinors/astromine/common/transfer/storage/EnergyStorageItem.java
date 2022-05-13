package com.github.mixinors.astromine.common.transfer.storage;

import team.reborn.energy.api.base.SimpleBatteryItem;

public interface EnergyStorageItem extends SimpleBatteryItem {
	@Override
	default long getEnergyMaxInput() {
		return getEnergyCapacity();
	}
	
	@Override
	default long getEnergyMaxOutput() {
		return getEnergyCapacity();
	}
}
