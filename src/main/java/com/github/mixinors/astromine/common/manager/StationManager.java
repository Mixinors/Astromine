package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMStaticComponents;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager.PacketContext;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import static com.github.mixinors.astromine.registry.common.AMNetworking.SYNC_STATIONS;

public class StationManager {
	public static Station readFromNbt(NbtCompound stationTag) {
		return new Station(stationTag);
	}
	
	@NotNull
	public static Station create(RegistryKey<World> world, BlockPos pos, UUID uuid, UUID ownerUuid, String name) {
		var station = new Station(world, pos, uuid, ownerUuid, name);
		return add(station);
	}
	
	private static Station add(Station station) {
		var server = InstanceUtil.getServer();
		if (server == null) throw new RuntimeException("Server is null.");
		
		var component = AMStaticComponents.getStations();
		component.add(station);
		
		sync(server);
		
		return station;
	}
	
	@Nullable
	public static Station get(UUID uuid) {
		var component = AMStaticComponents.getStations();
		return component.get(uuid);
	}
	
	@Nullable
	public static Station get(BlockPos pos) {
		return getStations()
				.stream()
				.filter(station -> station.getPos().equals(pos))
				.findFirst()
				.orElse(null);
	}
	
	public static Collection<Station> getStations() {
		var component = AMStaticComponents.getStations();
		return component.getAll();
	}
	
	private static ChunkPos findUnnocuppiedSpace() {
		// TODO: Implement!
		throw new UnsupportedOperationException();
	}
	
	public static void teleportToStation(PlayerEntity player, UUID uuid) {
		// TODO: Implement!
		throw new UnsupportedOperationException();
	}
	
	public static void onSync(PacketByteBuf buf, PacketContext context) {
		var nbt = buf.readNbt();
		
		context.queue(() -> {
			var component = AMStaticComponents.getStations();
			component.readFromNbt(nbt);
		});
	}
	
	public static void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
		sync(server);
	}
	
	public static void onServerStarting(MinecraftServer server) {
		var fabricLoader = FabricLoader.getInstance();
		if (fabricLoader == null) return;
		
		var worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
		
		var stationsFile = worldDir.toPath().resolve("data").resolve("stations.dat").toFile();
		
		if (stationsFile.exists()) {
			var stationsNbt = new NbtCompound();
			
			try {
				stationsNbt = NbtIo.read(stationsFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			AMStaticComponents.getStations().readFromNbt(stationsNbt);
		}
	}
	
	public static void onServerStopping(MinecraftServer server) {
		var fabricLoader = FabricLoader.getInstance();
		if (fabricLoader == null) return;
		
		var worldDir = server.getSavePath(WorldSavePath.ROOT).toFile();
		
		var stationsFile = worldDir.toPath().resolve("data").resolve("stations.dat").toFile();
		
		var stationsNbt = new NbtCompound();
		
		AMStaticComponents.getStations().writeToNbt(stationsNbt);
		
		try {
			// TODO: Use NbtIo#writeCompressed.
			NbtIo.write(stationsNbt, stationsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sync(MinecraftServer server) {
		var component = AMStaticComponents.getStations();
		
		var nbt = new NbtCompound();
		component.writeToNbt(nbt);
		
		var playerManager = server.getPlayerManager();
		if (playerManager == null) return;
		
		var playerList = playerManager.getPlayerList();
		if (playerList == null) return;
		
		var buf = PacketByteBufs.create();
		buf.writeNbt(nbt);
		
		for (var player : playerList) {
			ServerPlayNetworking.send(player, SYNC_STATIONS, PacketByteBufs.duplicate(buf));
		}
	}
}
