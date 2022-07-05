package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketElectronicsItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;

public class RocketElectronicsPart extends RocketPart<RocketElectronicsItem> {
	public RocketElectronicsPart(RocketElectronicsItem item, Tier tier) {
		super(item, tier);
	}
}
