package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.body.BodyTemperature;
import com.github.mixinors.astromine.common.item.rocket.RocketShieldingItem;

public class RocketShieldingPart extends RocketPart<RocketShieldingItem> {
	private final BodyTemperature[] temperatures;
	
	public RocketShieldingPart(RocketShieldingItem item, BodyTemperature... temperatures) {
		super(item);
		
		this.temperatures = temperatures;
	}
	
	public BodyTemperature[] getTemperatures() {
		return temperatures;
	}
}