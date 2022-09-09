package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class RocketManager {
	
	public static Rocket readFromNbt(NbtCompound rocketTag) {
		var rocket = new Rocket(rocketTag);
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) throw new RuntimeException("Failed to load the interior world.");
		
		var component = AMComponents.ROCKETS.get(world);
		component.addRocket(rocket);
		
		return rocket;
	}
	
	/**
	 * Creates a new {@link Rocket} with the given {@link UUID}.
	 * @param uuid the rocket's {@link UUID}.
	 * @return the created {@link Rocket}.
	 */
	@NotNull
	public static Rocket create(UUID uuid) {
		var rocket = new Rocket(uuid, findUnoccupiedSpace());
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) throw new RuntimeException("Failed to load the interior world.");
		
		var component = AMComponents.ROCKETS.get(world);
		component.addRocket(rocket);
		
		return rocket;
	}
	
	public static ChunkPos findUnoccupiedSpace() {
		var occupiedPositions = RocketManager
				.getRockets()
				.stream()
				.map(Rocket::getInteriorPos)
				.collect(Collectors.toSet());
		
		var random = new Random();
		ChunkPos chunkPos = null;
		
		while (chunkPos == null || occupiedPositions.contains(chunkPos)) {
			var bound = 16_000_000 / 16;
			chunkPos = new ChunkPos(random.nextInt(bound), random.nextInt(bound));
		}
		
		return chunkPos;
	}
	
	/**
	 * Gets a {@link Rocket} with the given {@link UUID}, if present.
	 * If not present, {@link #create(UUID)}s it.
	 * @param uuid the rocket's {@link UUID}.
	 * @return the {@link Rocket}.
	 */
	public static Rocket get(UUID uuid) {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		
		var component = AMComponents.ROCKETS.get(world);
		var rocket = component.getRocket(uuid);
		
		if (rocket == null) {
			AMCommon.LOGGER.error("RocketManager#getOrCreate created a new Rocket! This shouldn't be hit. Why did the rocket not already exist?");
			return null;
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
		
		var component = AMComponents.ROCKETS.get(world);
		return component.getRockets();
	}
	
	public static void sync() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return;
		
		AMComponents.ROCKETS.sync(world);
	}
}
