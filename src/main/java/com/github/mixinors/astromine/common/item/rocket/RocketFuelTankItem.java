package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketFuelTankPart;
import com.github.mixinors.astromine.common.entity.rocket.part.RocketHullPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketFuelTankItem extends RocketPartItem<RocketFuelTankPart> {
	public RocketFuelTankItem(Settings settings, Tier tier, long capacity) {
		super(settings);
		
		this.part = new RocketFuelTankPart(this, tier, capacity);
	}
}
