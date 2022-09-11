package com.github.mixinors.astromine.common.manager;

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
		return registerRocket(rocket);
	}
	
	/**
	 * Creates a new {@link Rocket} with the given {@link UUID}.
	 * @param uuid the rocket's {@link UUID}.
	 * @return the created {@link Rocket}.
	 */
	@NotNull
	public static Rocket create(UUID uuid) {
		var rocket = new Rocket(uuid, findUnoccupiedSpace());
		return registerRocket(rocket);
	}
	
	private static Rocket registerRocket(Rocket rocket) {
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
			chunkPos = new ChunkPos(random.nextInt(bound) % 32, random.nextInt(bound) % 32);
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
		
		if(component.getRockets().size() == 0) {
			throw new RuntimeException("Rockets Component failed to load.");
		}
		
		if (rocket == null) {
			return create(uuid);
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
