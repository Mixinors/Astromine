package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.screen.handler.base.entity.ExtendedEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMStaticComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.mixinors.astromine.registry.common.AMNetworking.SYNC_ROCKETS;

public class RocketManager {
	public static Rocket readFromNbt(NbtCompound rocketTag) {
		return new Rocket(rocketTag);
	}
	
	@NotNull
	public static Rocket create(UUID ownerUuid, UUID uuid) {
		var rocket = new Rocket(uuid, ownerUuid, findUnoccupiedSpace());
		return add(rocket);
	}
	
	private static Rocket add(Rocket rocket) {
		var server = InstanceUtil.getServer();
		if (server == null) throw new RuntimeException("Server is null.");

		var component = AMStaticComponents.getRockets();
		component.add(rocket);
		
		sync(server);
		
		return rocket;
	}
	
	@Nullable
	public static Rocket get(UUID uuid) {
		var component = AMStaticComponents.getRockets();
		return component.get(uuid);
	}
	
	@Nullable
	public static Rocket get(ChunkPos interiorPos) {
		return getRockets()
				.stream()
				.filter(rocket -> rocket.getInteriorPos().equals(interiorPos))
				.findFirst()
				.orElse(null);
	}
	
	public static Collection<Rocket> getRockets() {
		var component = AMStaticComponents.getRockets();
		return component.getAll();
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
		if (rocket == null) rocket = create(player.getUuid(), uuid);
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
				var server = serverPlayer.getServer();
				if (server == null) return;
				
				var world = server.getWorld(World.OVERWORLD);
				if (world == null) return;
				
				serverPlayer.teleport(world.getSpawnPos().getX(), world.getSpawnPos().getY(), world.getSpawnPos().getZ());
			}
		}
	}
	
	public static void onSync(PacketByteBuf buf, NetworkManager.PacketContext context) {
		var nbt = buf.readNbt();
		
		context.queue(() -> {
			var component = AMStaticComponents.getRockets();
			component.readFromNbt(nbt);
		});
	}

	public static void onServerPre(MinecraftServer server) {
		getRockets().forEach(Rocket::tick);
	}

	public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		sync(server);
	}
	
	public static void onServerStarting(MinecraftServer server) {
		var fabricLoader = FabricLoader.getInstance();
		if (fabricLoader == null) return;
		
		var worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
		
		var rocketsFile = worldDir.toPath().resolve("data").resolve("rockets.dat").toFile();
		
		if (rocketsFile.exists()) {
			var rocketsNbt = new NbtCompound();
			
			try {
				rocketsNbt = NbtIo.read(rocketsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			AMStaticComponents.getRockets().readFromNbt(rocketsNbt);
		}
	}
	
	public static void onServerStopping(MinecraftServer server) {
		var fabricLoader = FabricLoader.getInstance();
		if (fabricLoader == null) return;
		
		var worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
		
		var rocketFile = worldDir.toPath().resolve("data").resolve("rockets.dat").toFile();
		
		var rocketsNbt = new NbtCompound();
		
		AMStaticComponents.getRockets().writeToNbt(rocketsNbt);
		
		try {
			// TODO: Use NbtIo#writeCompressed.
			NbtIo.write(rocketsNbt, rocketFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sync(MinecraftServer server) {
		var component = AMStaticComponents.getRockets();
		
		var nbt = new NbtCompound();
		component.writeToNbt(nbt);
		
		var playerManager = server.getPlayerManager();
		if (playerManager == null) return;
		
		var playerList = playerManager.getPlayerList();
		if (playerList == null) return;
		
		var buf = PacketByteBufs.create();
		buf.writeNbt(nbt);
		
		for (var player : playerList) {
			ServerPlayNetworking.send(player, SYNC_ROCKETS, PacketByteBufs.duplicate(buf));
		}
	}
}
