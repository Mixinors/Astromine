package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class RocketManager {
	private static final PriorityQueue<Rocket> ROCKETS_TO_REGISTER = new ObjectArrayFIFOQueue<>();
	
	public static Rocket readFromNbt(NbtCompound rocketTag) {
		var rocket = new Rocket(rocketTag);
		ROCKETS_TO_REGISTER.enqueue(rocket);
		return rocket;
	}
	
	@NotNull
	public static Rocket create(UUID ownerUuid, UUID uuid) {
		var rocket = new Rocket(uuid, ownerUuid, findUnoccupiedSpace());
		return add(rocket);
	}
	
	private static Rocket add(Rocket rocket) {
		var server = InstanceUtil.getServer();
		if (server == null) throw new RuntimeException("Server is null.");
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) throw new RuntimeException("Failed to load the interior world. Try queueing the rockets to be registered?");
		
		var component = AMComponents.ROCKETS.get(world);
		component.add(rocket);
		
		return rocket;
	}
	
	@Nullable
	public static Rocket get(UUID uuid) {
		var server = InstanceUtil.getServer();
		if (server == null) return null;
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		var component = AMComponents.ROCKETS.get(world);
		
		return component.get(uuid);
	}
	
	@Nullable
	public static Rocket get(ChunkPos interiorPos) {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		
		return getRockets()
				.stream()
				.filter(rocket -> rocket.getInteriorPos().equals(interiorPos))
				.findFirst()
				.orElse(null);
	}
	
	public static Collection<Rocket> getRockets() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return ImmutableList.of();
		
		var component = AMComponents.ROCKETS.get(world);
		return component.getAll();
	}
	
	public static void sync() {
		var server = InstanceUtil.getServer();
		if (server == null) return;
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return;
		
		AMComponents.ROCKETS.sync(world);
	}
	
	private static ChunkPos findUnoccupiedSpace() {
		var occupiedPositions = RocketManager
				.getRockets()
				.stream()
				.map(Rocket::getInteriorPos)
				.collect(Collectors.toSet());
		
		var random = new Random();
		ChunkPos chunkPos = null;
		
		while (chunkPos == null || occupiedPositions.contains(chunkPos)) {
			var bound = 16_000_000 / 16;
			
			var x = random.nextInt(bound);
			var y = random.nextInt(bound);
			
			chunkPos = new ChunkPos(x - (x % 32), y - (y % 32));
		}
		
		return chunkPos;
	}
	
	public static void teleportToRocketInterior(PlayerEntity player, UUID uuid) {
		var rocket = get(uuid);
		if (rocket == null) return;
		
		var chunkPos = rocket.getInteriorPos();
		var placer = rocket.getPlacer(player.getUuid());
		
		if (placer == null) {
			placer = new Rocket.Placer(player.getWorld().getRegistryKey(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
		
			rocket.setPlacer(player.getUuid(), placer);
		}
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			var server = player.getServer();
			if (server == null) return;
			var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
			if (world == null) return;
		
			serverPlayer.teleport(world, chunkPos.x * 16.0F + 3.5F, 1.0F, chunkPos.z * 16.0F + 3.5F, 270.0F, 0.0F);
		}
	}
	
	public static void teleportToPlacer(PlayerEntity player, UUID uuid) {
		var rocket = get(uuid);
		if (rocket == null) return;
		
		var placer = rocket.getPlacer(player.getUuid());
		
		if (placer != null) {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				var server = player.getServer();
				if (server == null) return;
				var world = server.getWorld(placer.worldKey());
				if (world == null) return;
		
				serverPlayer.teleport(world, placer.x(), placer.y(), placer.z(), placer.yaw(), placer.pitch());
			}
		} else {
			if (player instanceof ServerPlayerEntity serverPlayer) {
				var world = serverPlayer.getWorld();
				if (world == null) return;
				
				serverPlayer.teleport(world.getSpawnPos().getX(), world.getSpawnPos().getY(), world.getSpawnPos().getZ());
			}
		}
	}
	
	public static void registerQueuedRockets() {
		while (!ROCKETS_TO_REGISTER.isEmpty()) {
			add(ROCKETS_TO_REGISTER.dequeue());
		}
	}
}
