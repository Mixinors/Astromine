package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.common.component.level.RocketsComponent;
import com.github.mixinors.astromine.common.component.level.StationsComponent;

public class AMStaticComponents {
	private static final ThreadLocal<RocketsComponent> ROCKETS = ThreadLocal.withInitial(RocketsComponent::new);
	private static final ThreadLocal<StationsComponent> STATIONS = ThreadLocal.withInitial(StationsComponent::new);
	
	public static RocketsComponent getRockets() {
		return ROCKETS.get();
	}
	
	public static StationsComponent getStations() {
		return STATIONS.get();
	}
}
