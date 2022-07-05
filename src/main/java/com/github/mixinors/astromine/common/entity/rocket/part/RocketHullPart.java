package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketHullItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketHullPart extends RocketPart<RocketHullItem> {
	private final double minimumTemperature;
	private final double maximumTemperature;
	
	public RocketHullPart(RocketHullItem item, Tier tier, double minimumTemperature, double maximumTemperature) {
		super(item, tier);
		
		this.minimumTemperature = minimumTemperature;
		this.maximumTemperature = maximumTemperature;
	}
	
	public double getMinimumTemperature() {
		return minimumTemperature;
	}
	
	public double getMaximumTemperature() {
		return maximumTemperature;
	}
}
