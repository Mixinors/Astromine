package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.component.level.RocketComponent;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.UUID;

public class RocketManager {
	public static Rocket createRocket(UUID uuid) {
		var rocket = new Rocket();
		rocket.setUuid(uuid);
		
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server.getWorld(World.OVERWORLD));
		component.addRocket(rocket);
		
		return rocket;
	}
	
	public static Rocket getRocket(UUID uuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server.getWorld(World.OVERWORLD));
		
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			rocket = createRocket(uuid);
		}
		
		return rocket;
	}
	
	public static ChunkPos getChunkPosition(UUID uuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server.getWorld(World.OVERWORLD));
		
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			createRocket(uuid);
		}
		
		return component.getChunkPosition(uuid);
	}
	
	public static RocketComponent.Placer getPlacer(UUID rocketUuid, UUID playerUuid) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server);
		
		return component.getPlacer(rocketUuid, playerUuid);
	}
	
	public static void setPlacer(UUID rocketUuid, UUID playerUuid, RocketComponent.Placer placer) {
		var server = InstanceUtil.getServer();
		
		var component = AMComponents.ROCKET_COMPONENT.get(server);
		
		component.setPlacer(rocketUuid, playerUuid, placer);
	}
}
