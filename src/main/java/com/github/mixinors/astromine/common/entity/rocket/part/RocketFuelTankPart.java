package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketFuelTankItem;
import com.github.mixinors.astromine.common.item.rocket.RocketHullItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketFuelTankPart extends RocketPart<RocketFuelTankItem> {
	private final long capacity;
	
	public RocketFuelTankPart(RocketFuelTankItem item, Tier tier, long capacity) {
		super(item, tier);
		
		this.capacity = capacity;
	}
	
	public long getCapacity() {
		return capacity;
	}
}
