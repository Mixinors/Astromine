package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.body.BodyTemperature;
import com.github.mixinors.astromine.common.rocket.RocketShieldingPart;

public class RocketShieldingItem extends RocketPartItem<RocketShieldingPart> {
	public RocketShieldingItem(Properties settings, BodyTemperature... temperatures) {
		super(settings);
		
		this.part = new RocketShieldingPart(this, temperatures);
	}
}
