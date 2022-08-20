package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RocketComponent implements AutoSyncedComponent {
	public record Placer(
			RegistryKey<World> worldKey,
			double x, double y, double z,
			float yaw, float pitch
	) {
		public static final Codec<Placer> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
						RegistryKey.createCodec(Registry.WORLD_KEY).fieldOf("world").forGetter(Placer::worldKey),
						Codec.DOUBLE.fieldOf("x").forGetter(Placer::x),
						Codec.DOUBLE.fieldOf("y").forGetter(Placer::y),
						Codec.DOUBLE.fieldOf("z").forGetter(Placer::z),
						Codec.FLOAT.fieldOf("yaw").forGetter(Placer::yaw),
						Codec.FLOAT.fieldOf("pitch").forGetter(Placer::pitch)
				).apply(instance, Placer::new)
			);
	}
	
	private static final String ROCKET_KEY = "Rocket";
	private static final String PLAYER_KEY = "Player";
	private static final String PLACER_KEY = "Placer";
	
	private static final String POSITION_KEY = "Position";

	private static final String ROCKETS_KEY = "Rockets";
	private static final String PLACERS_KEY = "Placers";
	
	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	private final Map<UUID, ChunkPos> chunkPositions = new ConcurrentHashMap<>();
	private final Map<Pair<UUID, UUID>, Placer> placers = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unused")
	public RocketComponent(WorldProperties worldProperties) {
	
	}
	
	public void addRocket(Rocket rocket) {
		this.rockets.put(rocket.getUuid(), rocket);
		this.chunkPositions.computeIfAbsent(rocket.getUuid(), this::getRandomChunkPosition);
	}
	
	public Rocket getRocket(UUID rocketUuid) {
		return this.rockets.get(rocketUuid);
	}
	
	public ChunkPos getChunkPosition(UUID rocketUuid) {
		return this.chunkPositions.get(rocketUuid);
	}
	
	public Placer getPlacer(UUID playerUuid, UUID rocketUuid) {
		return this.placers.get(new Pair<>(rocketUuid, playerUuid));
	}
	
	public void setPlacer(UUID playerUuid, UUID rocketUuid, Placer placer) {
		this.placers.put(new Pair<>(rocketUuid, playerUuid), placer);
	}
	
	public void removeRocket(Rocket rocket) {
		this.rockets.remove(rocket.getUuid());
		this.chunkPositions.remove(rocket.getUuid());
		
		var placersToRemove = placers.keySet().stream().filter(key -> key.getFirst().equals(rocket.getUuid())).toList();
		placersToRemove.forEach(placers::remove);
	}
	
	private ChunkPos getRandomChunkPosition(UUID key) {
		var random = new Random(key.hashCode());
		
		ChunkPos chunkPos = null;
		
		while (chunkPos == null || chunkPositions.containsValue(chunkPos)) {
			chunkPos = new ChunkPos(random.nextInt(Integer.MAX_VALUE / 2), random.nextInt(Integer.MAX_VALUE / 2));
		}
		
		return chunkPos;
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var rocketsNbt = new NbtCompound();
		
		for (var entry : rockets.entrySet()) {
			var uuid = entry.getKey();
			var rocket = entry.getValue();
			
			var rocketNbt = new NbtCompound();
			rocket.writeToNbt(rocketNbt);
			
			NbtUtil.putChunkPos(rocketsNbt, POSITION_KEY, chunkPositions.get(uuid));
			
			rocketsNbt.put(uuid.toString(), rocketNbt);
		}
		
		nbt.put(ROCKETS_KEY, rocketsNbt);
		
		var placersNbt = new NbtList();
		
		for (var entry : placers.entrySet()) {
			var uuids = entry.getKey();
			var placer = entry.getValue();
			
			var rocketUuid = uuids.getFirst();
			var playerUuid = uuids.getSecond();
			
			var placerNbt = new NbtCompound();
			placerNbt.putUuid(ROCKET_KEY, rocketUuid);
			placerNbt.putUuid(PLAYER_KEY, playerUuid);
			
			var dataNbt = new NbtCompound();
			Placer.CODEC.encode(placer, NbtOps.INSTANCE, dataNbt);
			
			placerNbt.put(PLACER_KEY, dataNbt);
			
			placersNbt.add(placerNbt);
		}
		
		nbt.put(PLACERS_KEY, placersNbt);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var rocketsNbt = nbt.getCompound(ROCKETS_KEY);
		
		for (var key : rocketsNbt.getKeys()) {
			var uuid = UUID.fromString(key);
			var rocket = rockets.computeIfAbsent(uuid, k -> new Rocket());
			
			var rocketNbt = rocketsNbt.getCompound(key);
			
			rocket.readFromNbt(rocketNbt);
			
			chunkPositions.put(rocket.getUuid(), NbtUtil.getChunkPos(rocketsNbt, POSITION_KEY));
		}
		
		var placersNbt = nbt.getList(PLACERS_KEY, NbtElement.COMPOUND_TYPE);
		
		for (var entry : placersNbt) {
			var placerNbt = (NbtCompound) entry;
			
			var rocketUuid = placerNbt.getUuid(ROCKET_KEY);
			var playerUuid = placerNbt.getUuid(PLAYER_KEY);
			
			var dataNbt = placerNbt.getCompound(PLACER_KEY);
			var placer = Placer.CODEC.decode(NbtOps.INSTANCE, dataNbt).result().get().getFirst();
			
			placers.put(new Pair<>(rocketUuid, playerUuid), placer);
		}
	}
	
	// TODO: Check if player has a Rocket-related ScreenHandler open, or is in the Rocket Dimension!
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		if (player.getWorld().getRegistryKey().equals(AMWorlds.ROCKET)) {
			return true;
		} else {
			return false;
		}
	}
}
