package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.level.RocketsComponent;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMNetworking;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RocketManager {
	private static final RocketsComponent CLIENT_ROCKETS = new RocketsComponent();
	private static final String LEGACY_FILE_ID = "rockets";
	
	public static Rocket readFromNbt(CompoundTag rocketTag) {
		return new Rocket(rocketTag);
	}
	
	private static RocketsComponent getData(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(RocketsComponent.factory(), RocketsComponent.FILE_ID);
	}
	
	@NotNull
	public static Rocket create(MinecraftServer server, UUID ownerUuid, UUID uuid) {
		var rocket = new Rocket(uuid, ownerUuid, findUnoccupiedSpace(server));
		return add(server, rocket);
	}
	
	private static Rocket add(MinecraftServer server, Rocket rocket) {
		var component = getData(server);
		component.add(rocket);
		attachSyncListener(server, component, rocket);
		
		sync(server);
		
		return rocket;
	}
	
	@Nullable
	public static Rocket get(MinecraftServer server, UUID uuid) {
		return getData(server).get(uuid);
	}
	
	@Nullable
	public static Rocket get(Level level, UUID uuid) {
		return level instanceof ServerLevel serverLevel ? get(serverLevel.getServer(), uuid) : CLIENT_ROCKETS.get(uuid);
	}
	
	@Nullable
	public static Rocket get(UUID uuid) {
		return CLIENT_ROCKETS.get(uuid);
	}
	
	@Nullable
	public static Rocket get(MinecraftServer server, ChunkPos interiorPos) {
		return getRockets(server)
				.stream()
				.filter(rocket -> rocket.getInteriorPos().equals(interiorPos))
				.findFirst()
				.orElse(null);
	}
	
	@Nullable
	public static Rocket get(Level level, ChunkPos interiorPos) {
		return level instanceof ServerLevel serverLevel
				? get(serverLevel.getServer(), interiorPos)
				: getRockets()
						.stream()
						.filter(rocket -> rocket.getInteriorPos().equals(interiorPos))
						.findFirst()
						.orElse(null);
	}
	
	public static Collection<Rocket> getRockets(MinecraftServer server) {
		return getData(server).getAll();
	}
	
	public static Collection<Rocket> getRockets() {
		return CLIENT_ROCKETS.getAll();
	}
	
	private static ChunkPos findUnoccupiedSpace(MinecraftServer server) {
		var occupiedPositions = RocketManager
				.getRockets(server)
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
	
	public static void teleportToRocketInterior(Player player, UUID uuid) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}
		
		var server = serverPlayer.getServer();
		var rocket = get(server, uuid);
		if (rocket == null) rocket = create(server, player.getUUID(), uuid);
		
		var chunkPos = rocket.getInteriorPos();
		var placer = rocket.getPlacer(player.getUUID());
		
		if (placer == null) {
			placer = new Rocket.Placer(player.level().dimension(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
			
			rocket.setPlacer(player.getUUID(), placer);
		}
		
		var world = server.getLevel(AMWorlds.ROCKET_INTERIORS);
		if (world == null) return;
		
		serverPlayer.teleportTo(world, chunkPos.x * 16.0F + 3.5F, 1.0F, chunkPos.z * 16.0F + 3.5F, 270.0F, 0.0F);
	}
	
	public static void teleportToPlacer(Player player, UUID uuid) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}
		
		var server = serverPlayer.getServer();
		var rocket = get(server, uuid);
		if (rocket == null) return;
		
		var placer = rocket.getPlacer(player.getUUID());
		
		if (placer != null) {
			var world = server.getLevel(placer.worldKey());
			if (world == null) return;
			
			serverPlayer.teleportTo(world, placer.x(), placer.y(), placer.z(), placer.yaw(), placer.pitch());
		} else {
			var world = server.getLevel(Level.OVERWORLD);
			if (world == null) return;
			
			serverPlayer.teleportTo(world.getSharedSpawnPos().getX(), world.getSharedSpawnPos().getY(), world.getSharedSpawnPos().getZ());
		}
	}
	
	public static void onSync(CompoundTag nbt) {
		CLIENT_ROCKETS.readFromNbt(nbt);
	}
	
	public static void onPlayerJoin(MinecraftServer server) {
		sync(server);
	}
	
	public static void onServerStarting(MinecraftServer server) {
		var component = getData(server);
		migrateLegacyData(server, component);
		attachSyncListeners(server, component);
	}
	
	public static void sync(MinecraftServer server) {
		var component = getData(server);
		
		var nbt = new CompoundTag();
		component.writeToNbt(nbt);
		
		for (var player : server.getPlayerList().getPlayers()) {
			PacketDistributor.sendToPlayer(player, new AMNetworking.SyncRocketsPayload(nbt.copy()));
		}
	}
	
	private static void migrateLegacyData(MinecraftServer server, RocketsComponent component) {
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
			AMCommon.LOGGER.warn("Failed to migrate legacy rocket data from {}", legacyFile, exception);
		}
	}
	
	private static void attachSyncListeners(MinecraftServer server, RocketsComponent component) {
		for (var rocket : component.getAll()) {
			attachSyncListener(server, component, rocket);
		}
	}
	
	private static void attachSyncListener(MinecraftServer server, RocketsComponent component, Rocket rocket) {
		rocket.setSyncListener(() -> {
			component.setDirty();
			sync(server);
		});
	}
}
