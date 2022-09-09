package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

import java.util.Collection;
import java.util.UUID;

public class RocketManager {
	/**
	 * Creates a new {@link Rocket} with the given {@link UUID}.
	 * @param uuid the rocket's {@link UUID}.
	 * @return the created {@link Rocket}.
	 */
	public static Rocket create(UUID uuid) {
		var rocket = new Rocket(uuid);
		rocket.initStorage();
		
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		
		var component = AMComponents.ROCKET_COMPONENTS.get(world);
		component.addRocket(rocket);
		
		return rocket;
	}
	
	/**
	 * Gets a {@link Rocket} with the given {@link UUID}, if present.
	 * If not present, {@link #create(UUID)}s it.
	 * @param uuid the rocket's {@link UUID}.
	 * @return the {@link Rocket}.
	 * @deprecated Deprecated as this method is hiding issues with Rocket storage as it just creates a new rocket when it fails.
	 */
	@Deprecated
	public static Rocket getOrCreate(UUID uuid) {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		
		var component = AMComponents.ROCKET_COMPONENTS.get(world);
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			AMCommon.LOGGER.error("RocketManager#getOrCreate created a new Rocket! This shouldn't be hit. Why did the rocket not already exist?");
			rocket = create(uuid);
		}
		
		return rocket;
	}
	
	/**
	 * Gets all {@link Rocket}s.
	 */
	public static Collection<Rocket> getRockets() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return ImmutableList.of();
		
		var component = AMComponents.ROCKET_COMPONENTS.get(world);
		return component.getRockets();
	}
	
	public static void sync() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return;
		
		AMComponents.ROCKET_COMPONENTS.sync(world);
	}
}
