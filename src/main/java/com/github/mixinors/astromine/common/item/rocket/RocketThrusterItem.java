package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.rocket.RocketThrusterPart;
import net.minecraft.item.Item;

public class RocketThrusterItem extends RocketPartItem<RocketThrusterPart> {
	public RocketThrusterItem(Item.Settings settings, RocketThrusterPart.Efficiency efficiency) {
		super(settings);
		
		this.part = new RocketThrusterPart(this, efficiency);
	}
}
