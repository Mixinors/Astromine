package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.entity.rocket.part.RocketShieldingPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;

public class RocketShieldingItem extends RocketPartItem<RocketShieldingPart> {
	public RocketShieldingItem(Settings settings, Body.Temperature... temperatures) {
		super(settings);
		
		this.part = new RocketShieldingPart(this, temperatures);
	}
}
