package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.body.Body;
import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketShieldingItem;

public class RocketShieldingPart extends RocketPart<RocketShieldingItem> {
	private final Body.Temperature[] temperatures;
	
	public RocketShieldingPart(RocketShieldingItem item, Body.Temperature... temperatures) {
		super(item);
		
		this.temperatures = temperatures;
	}
	
	public Body.Temperature[] getTemperatures() {
		return temperatures;
	}
}