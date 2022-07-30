package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.rocket.RocketLifeSupportPart;

public class RocketLifeSupportItem extends RocketPartItem<RocketLifeSupportPart> {
	public RocketLifeSupportItem(Settings settings) {
		super(settings);
		
		this.part = new RocketLifeSupportPart(this);
	}
}
