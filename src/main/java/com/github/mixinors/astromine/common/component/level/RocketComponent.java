package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import com.mojang.datafixers.util.Pair;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.vini2003.hammer.core.api.common.util.NbtUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RocketComponent implements AutoSyncedComponent {
	private static final String ROCKETS_KEY = "Rockets";

	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unused")
	public RocketComponent(World worldProperties) {
	
	}
	
	public void addRocket(Rocket rocket) {
		this.rockets.put(rocket.getUuid(), rocket);
	}
	
	public Rocket getRocket(UUID rocketUuid) {
		return this.rockets.get(rocketUuid);
	}
	
	public Collection<Rocket> getRockets() {
		return this.rockets.values();
	}
	
	public void removeRocket(Rocket rocket) {
		this.rockets.remove(rocket.getUuid());
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var rocketsNbt = new NbtCompound();
		
		for (var entry : rockets.entrySet()) {
			var uuid = entry.getKey();
			var rocket = entry.getValue();
			
			var rocketNbt = new NbtCompound();
			rocket.writeToNbt(rocketNbt);
			
			rocketsNbt.put(uuid.toString(), rocketNbt);
		}
		
		nbt.put(ROCKETS_KEY, rocketsNbt);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var rocketsNbt = nbt.getCompound(ROCKETS_KEY);
		
		for (var key : rocketsNbt.getKeys()) {
			var uuid = UUID.fromString(key);
			var rocket = rockets.computeIfAbsent(uuid, Rocket::new);
			
			var rocketNbt = rocketsNbt.getCompound(key);
			
			rocket.readFromNbt(rocketNbt);
		}
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player.getWorld().getRegistryKey().equals(AMWorlds.ROCKET_INTERIORS);
	}
}
