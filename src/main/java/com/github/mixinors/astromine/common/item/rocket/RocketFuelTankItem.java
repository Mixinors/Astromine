package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketFuelTankPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;

public class RocketFuelTankItem extends RocketPartItem<RocketFuelTankPart> {
	public RocketFuelTankItem(Settings settings, RocketFuelTankPart.Capacity capacity) {
		super(settings);
		
		this.part = new RocketFuelTankPart(this, capacity);
	}
}
