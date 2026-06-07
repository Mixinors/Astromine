package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.level.StationsComponent;
import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StationManager {
	private static final StationsComponent CLIENT_STATIONS = new StationsComponent();
	private static final String LEGACY_FILE_ID = "stations";
	
	public static Station readFromNbt(CompoundTag stationTag) {
		return new Station(stationTag);
	}
	
	private static StationsComponent getData(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(StationsComponent.factory(), StationsComponent.FILE_ID);
	}
	
	@NotNull
	public static Station create(MinecraftServer server, ResourceKey<Level> world, BlockPos pos, UUID uuid, UUID ownerUuid, String name) {
		var station = new Station(world, pos, uuid, ownerUuid, name);
		return add(server, station);
	}
	
	private static Station add(MinecraftServer server, Station station) {
		var component = getData(server);
		component.add(station);
		
		sync(server);
		
		return station;
	}
	
	@Nullable
	public static Station get(MinecraftServer server, UUID uuid) {
		return getData(server).get(uuid);
	}
	
	@Nullable
	public static Station get(Level level, UUID uuid) {
		return level instanceof ServerLevel serverLevel ? get(serverLevel.getServer(), uuid) : CLIENT_STATIONS.get(uuid);
	}
	
	@Nullable
	public static Station get(UUID uuid) {
		return CLIENT_STATIONS.get(uuid);
	}
	
	@Nullable
	public static Station get(MinecraftServer server, BlockPos pos) {
		return getStations(server)
				.stream()
				.filter(station -> station.getPos().equals(pos))
				.findFirst()
				.orElse(null);
	}
	
	@Nullable
	public static Station get(Level level, BlockPos pos) {
		return level instanceof ServerLevel serverLevel
				? get(serverLevel.getServer(), pos)
				: getStations()
						.stream()
						.filter(station -> station.getPos().equals(pos))
						.findFirst()
						.orElse(null);
	}
	
	@Nullable
	public static Station get(MinecraftServer server, ResourceKey<Level> world, BlockPos pos) {
		return getStations(server)
				.stream()
				.filter(station -> station.getWorldKey().equals(world) && station.getPos().equals(pos))
				.findFirst()
				.orElse(null);
	}
	
	@Nullable
	public static Station get(Level level, ResourceKey<Level> world, BlockPos pos) {
		return level instanceof ServerLevel serverLevel
				? get(serverLevel.getServer(), world, pos)
				: getStations()
						.stream()
						.filter(station -> station.getWorldKey().equals(world) && station.getPos().equals(pos))
						.findFirst()
						.orElse(null);
	}
	
	public static Collection<Station> getStations(MinecraftServer server) {
		return getData(server).getAll();
	}
	
	public static Collection<Station> getStations() {
		return CLIENT_STATIONS.getAll();
	}
	
	public static void teleportToStation(Player player, UUID uuid) {
		var server = player.getServer();
		
		if (server == null || !(player instanceof ServerPlayer serverPlayer)) {
			return;
		}
		
		var station = get(server, uuid);
		
		if (station == null) {
			return;
		}
		
		var world = server.getLevel(station.getWorldKey());
		
		if (world == null) {
			return;
		}
		
		var pos = station.getPos();
		serverPlayer.teleportTo(world, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, player.getYRot(), player.getXRot());
	}
	
	public static void onSync(CompoundTag nbt) {
		CLIENT_STATIONS.readFromNbt(nbt);
	}
	
	public static void onPlayerJoin(MinecraftServer server) {
		sync(server);
	}
	
	public static void onServerStarting(MinecraftServer server) {
		var component = getData(server);
		migrateLegacyData(server, component);
	}
	
	public static void sync(MinecraftServer server) {
		var component = getData(server);
		
		var nbt = new CompoundTag();
		component.writeToNbt(nbt);
		
		for (var player : server.getPlayerList().getPlayers()) {
			PacketDistributor.sendToPlayer(player, new AMNetworking.SyncStationsPayload(nbt.copy()));
		}
	}
	
	private static void migrateLegacyData(MinecraftServer server, StationsComponent component) {
		if (!component.isEmpty()) {
			return;
		}
		
		var legacyFile = server.getWorldPath(LevelResource.ROOT).resolve("data").resolve(LEGACY_FILE_ID + ".dat");
		
		if (!legacyFile.toFile().exists()) {
			return;
		}
		
		try {
			component.readFromNbt(NbtIo.read(legacyFile));
			component.setDirty();
		} catch (IOException exception) {
			AMCommon.LOGGER.warn("Failed to migrate legacy station data from {}", legacyFile, exception);
		}
	}
}
