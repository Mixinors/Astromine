package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketHullPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketHullItem extends RocketPartItem<RocketHullPart> {
	public RocketHullItem(Settings settings, Tier tier, double maximumTemperature, double minimumTemperature) {
		super(settings);
		
		this.part = new RocketHullPart(this, tier, maximumTemperature, minimumTemperature);
	}
}
