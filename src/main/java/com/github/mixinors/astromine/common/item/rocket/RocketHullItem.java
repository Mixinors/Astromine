package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketHullPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;

public class RocketHullItem extends RocketPartItem<RocketHullPart> {
	public RocketHullItem(Settings settings, RocketHullPart.Durability durability) {
		super(settings);
		
		this.part = new RocketHullPart(this, durability);
	}
}
