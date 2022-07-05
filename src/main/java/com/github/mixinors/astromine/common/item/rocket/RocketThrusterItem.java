package com.github.mixinors.astromine.common.item.rocket;

import com.github.mixinors.astromine.common.entity.rocket.part.RocketThrusterPart;
import com.github.mixinors.astromine.common.item.rocket.base.RocketPartItem;
import com.github.mixinors.astromine.common.util.data.tier.Tier;
import net.minecraft.item.Item;

public class RocketThrusterItem extends RocketPartItem<RocketThrusterPart> {
	public RocketThrusterItem(Item.Settings settings, Tier tier, double liquidOxygenConsumptionCoefficient, double liquidFuelConsumptionCoefficient) {
		super(settings);
		
		this.part = new RocketThrusterPart(this, tier, liquidOxygenConsumptionCoefficient, liquidFuelConsumptionCoefficient);
	}
	
	public RocketThrusterItem(Item.Settings settings, Tier tier, double solidFuelConsumptionCoefficient) {
		super(settings);
		
		this.part = new RocketThrusterPart(this, tier, solidFuelConsumptionCoefficient);
	}
}
