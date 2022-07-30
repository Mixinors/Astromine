package com.github.mixinors.astromine.common.rocket;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.item.rocket.RocketFuelTankItem;

public class RocketFuelTankPart extends RocketPart<RocketFuelTankItem> {
	public enum Capacity {
		LOW(AMConfig.get().items.smallRocketFuelTankCapacity),
		MEDIUM(AMConfig.get().items.mediumRocketFuelTankCapacity),
		HIGH(AMConfig.get().items.largeRocketFuelTankCapacity);
		
		private final long size;
		
		Capacity(long size) {
			this.size = size;
		}
		
		public long getSize() {
			return size;
		}
	}
	
	private final Capacity capacity;
	
	public RocketFuelTankPart(RocketFuelTankItem item, Capacity capacity) {
		super(item);
		
		this.capacity = capacity;
	}
	
	public Capacity getCapacity() {
		return capacity;
	}
}
