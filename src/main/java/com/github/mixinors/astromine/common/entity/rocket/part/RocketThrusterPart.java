package com.github.mixinors.astromine.common.entity.rocket.part;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.entity.rocket.part.base.RocketPart;
import com.github.mixinors.astromine.common.item.rocket.RocketThrusterItem;

public class RocketThrusterPart extends RocketPart<RocketThrusterItem> {
	public enum Efficiency {
		LOW(AMConfig.get().items.lowEfficiencyRocketThrusterFuelConsumptionMultiplier),
		MEDIUM(AMConfig.get().items.mediumEfficiencyRocketThrusterFuelConsumptionMultiplier),
		HIGH(AMConfig.get().items.highEfficiencyRocketThrusterFuelConsumptionMultiplier);
		
		private final double fuelConsumptionMultiplier;
		
		Efficiency(double fuelConsumptionMultiplier) {
			this.fuelConsumptionMultiplier = fuelConsumptionMultiplier;
		}
		
		public double getFuelConsumptionMultiplier() {
			return fuelConsumptionMultiplier;
		}
	}
	
	private final Efficiency efficiency;
	
	public RocketThrusterPart(RocketThrusterItem item, Efficiency efficiency) {
		super(item);
		
		this.efficiency = efficiency;
	}
}
