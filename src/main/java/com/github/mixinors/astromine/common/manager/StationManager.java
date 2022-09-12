package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public class StationManager {
	private static final PriorityQueue<Station> STATIONS_TO_REGISTER = new ObjectArrayFIFOQueue<>();
	
	public static Station readFromNbt(NbtCompound stationTag) {
		var station = new Station(stationTag);
		STATIONS_TO_REGISTER.enqueue(station);
		return station;
	}
	
	@NotNull
	public static Station create(RegistryKey<World> world, BlockPos pos, UUID uuid, UUID ownerUuid, String name) {
		var station = new Station(world, pos, uuid, ownerUuid, name);
		return add(station);
	}
	
	public static boolean canCreate(RegistryKey<World> worldKey, BlockPos pos, UUID ownerUuid) {
		var server = InstanceUtil.getServer();
		if (server == null) throw new RuntimeException("Server is null.");
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) throw new RuntimeException("Failed to load the interior world. Try queueing the stations to be registered?");
		
		var component = AMComponents.STATIONS.get(world);
		
		if (!AMWorlds.isAstromine(worldKey)) return false;
		
		return component.getAll()
				.stream()
				.filter(station -> station.getWorldKey().equals(worldKey))
				.filter(station -> !station.getOwnerUuid().equals(ownerUuid))
				.noneMatch(station -> station.getPos().isWithinDistance(pos, 256));
	}
	
	private static Station add(Station station) {
		var server = InstanceUtil.getServer();
		if (server == null) throw new RuntimeException("Server is null.");
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS); // TODO: Decide if we should just use this world as a data storage.
		if (world == null) throw new RuntimeException("Failed to load the interior world. Try queueing the stations to be registered?");
		
		var component = AMComponents.STATIONS.get(world);
		component.add(station);
		
		return station;
	}
	
	@Nullable
	public static Station get(UUID uuid) {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS); // TODO: See above.
		if (world == null) return null;
		var component = AMComponents.STATIONS.get(world);
		
		if (component.getAll().isEmpty()) {
			throw new RuntimeException("Stations Component failed to load.");
		}
		
		return component.get(uuid);
	}
	
	@Nullable
	public static Station get(BlockPos pos) {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return null;
		
		return getStations()
				.stream()
				.filter(station -> station.getPos().equals(pos))
				.findFirst()
				.orElse(null);
	}
	
	public static Collection<Station> getStations() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return ImmutableList.of();
		
		var component = AMComponents.STATIONS.get(world);
		return component.getAll();
	}
	
	public static void sync() {
		var server = InstanceUtil.getServer();
		var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return;
		
		AMComponents.STATIONS.sync(world);
	}
	
	public static void teleportToStation(PlayerEntity player, UUID uuid) {
		var station = get(uuid);
		if (station == null) return;
		
		var pos = station.getPos();
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			var server = player.getServer();
			if (server == null) return;
			var world = server.getWorld(AMWorlds.ROCKET_INTERIORS);
			if (world == null) return;
		
			serverPlayer.teleport(world, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, 270.0F, 0.0F);
		}
	}
	
	public static void registerQueuedStations() {
		while (!STATIONS_TO_REGISTER.isEmpty()) {
			add(STATIONS_TO_REGISTER.dequeue());
		}
	}
}
