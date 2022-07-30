package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketLandingMechanismPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;

public class RocketLandingMechanismItem extends RocketPartItem<RocketLandingMechanismPart> {
	public RocketLandingMechanismItem(Settings settings, RocketLandingMechanismPart.Type type) {
		super(settings);
		
		this.part = new RocketLandingMechanismPart(this, type);
	}
}
