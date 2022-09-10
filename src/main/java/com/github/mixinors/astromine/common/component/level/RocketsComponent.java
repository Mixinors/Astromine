package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RocketsComponent implements AutoSyncedComponent {
	private static final String ROCKETS_KEY = "Rockets";
	
	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	
	public RocketsComponent(World world) {
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
	
	public void tick() {
		this.rockets.values().forEach(Rocket::tick);
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
			var rocketNbt = rocketsNbt.getCompound(key);
			rockets.computeIfAbsent(uuid, uuid1 -> RocketManager.readFromNbt(rocketNbt));
		}
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player.getWorld().getRegistryKey().equals(AMWorlds.ROCKET_INTERIORS);
	}
}
