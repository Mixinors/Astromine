package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.component.level.RocketComponent;
import com.github.mixinors.astromine.common.item.rocket.*;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.rocket.RocketFuelTankPart;
import com.github.mixinors.astromine.common.rocket.RocketHullPart;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.util.math.ChunkPos;

import java.util.UUID;

public class RocketManager {
	public static Rocket createRocket(UUID uuid) {
		var rocket = new Rocket(
				uuid,
				((RocketFuelTankItem) AMItems.HIGH_CAPACITY_ROCKET_FUEL_TANK.get()).getPart(),
				((RocketHullItem) AMItems.HIGH_DURABILITY_ROCKET_HULL.get()).getPart(),
				((RocketLandingMechanismItem) AMItems.STANDING_ROCKET_LANDING_MECHANISM.get()).getPart(),
				((RocketLifeSupportItem) AMItems.ROCKET_LIFE_SUPPORT.get()).getPart(),
				((RocketShieldingItem) AMItems.HIGH_TEMPERATURE_ROCKET_SHIELDING.get()).getPart(),
				((RocketThrusterItem) AMItems.HIGH_EFFICIENCY_ROCKET_THRUSTER.get()).getPart()
		);
		rocket.onPartChanged();
		
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(server.getWorld(AMWorlds.ROCKET_INTERIORS));
		component.addRocket(rocket);
		
		return rocket;
	}
	
	public static Rocket getRocket(UUID uuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(server.getWorld(AMWorlds.ROCKET_INTERIORS));
		
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			rocket = createRocket(uuid);
		}
		
		return rocket;
	}
	
	public static ChunkPos getChunkPosition(UUID uuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(server.getWorld(AMWorlds.ROCKET_INTERIORS));
		
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			createRocket(uuid);
		}
		
		return component.getChunkPosition(uuid);
	}
	
	public static RocketComponent.Placer getPlacer(UUID rocketUuid, UUID playerUuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(server.getWorld(AMWorlds.ROCKET_INTERIORS));
		
		return component.getPlacer(rocketUuid, playerUuid);
	}
	
	public static void setPlacer(UUID rocketUuid, UUID playerUuid, RocketComponent.Placer placer) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(server.getWorld(AMWorlds.ROCKET_INTERIORS));
		
		component.setPlacer(rocketUuid, playerUuid, placer);
	}
}
