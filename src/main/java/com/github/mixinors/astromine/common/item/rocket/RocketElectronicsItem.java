package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketElectronicsPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketElectronicsItem extends RocketPartItem<RocketElectronicsPart> {
	public RocketElectronicsItem(Settings settings, Tier tier) {
		super(settings);
		
		this.part = new RocketElectronicsPart(this, tier);
	}
}
