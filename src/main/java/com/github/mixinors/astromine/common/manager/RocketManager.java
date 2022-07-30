package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

import java.util.UUID;

public class RocketManager {
	public static Rocket createRocket(UUID uuid) {
		var rocket = new Rocket();
		rocket.setUuid(uuid);
		
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server);
		component.add(rocket);
		
		return rocket;
	}
	
	public static Rocket getRocket(UUID uuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server);
		
		var rocket = component.get(uuid);
		
		if (rocket == null) {
			rocket = createRocket(uuid);
		}
		
		return rocket;
	}
}
