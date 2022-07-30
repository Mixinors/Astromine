package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.rocket.RocketHullItem;

public class RocketHullPart extends RocketPart<RocketHullItem> {
	public enum Durability {
		LOW(AMConfig.get().items.lowDurabilityRocketHullTrips),
		MEDIUM(AMConfig.get().items.mediumDurabilityRocketHullTrips),
		HIGH(AMConfig.get().items.highDurabilityRocketHullTrips);
		
		private final long trips;
		
		Durability(long trips) {
			this.trips = trips;
		}
		
		public long getTrips() {
			return trips;
		}
	}
	
	private final Durability durability;

	public RocketHullPart(RocketHullItem item, Durability durability) {
		super(item);
		
		this.durability = durability;
	}
	
	public Durability getDurability() {
		return durability;
	}
}
