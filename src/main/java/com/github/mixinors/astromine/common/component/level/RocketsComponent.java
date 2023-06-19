package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import net.minecraft.nbt.NbtCompound;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RocketsComponent {
	private static final String ROCKETS_KEY = "Rockets";
	
	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	
	public RocketsComponent() {
	}
	
	public void add(Rocket rocket) {
		this.rockets.put(rocket.getUuid(), rocket);
	}
	
	public Rocket get(UUID rocketUuid) {
		return this.rockets.get(rocketUuid);
	}
	
	public Collection<Rocket> getAll() {
		return this.rockets.values();
	}
	
	public void remove(Rocket rocket) {
		this.rockets.remove(rocket.getUuid());
	}
	
	public void tick() {
		this.rockets.values().forEach(Rocket::tick);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		var rocketsNbt = new NbtCompound();
		
		for (var entry : rockets.entrySet()) {
			var rocketUuid = entry.getKey();
			var rocket = entry.getValue();
			
			var rocketNbt = new NbtCompound();
			rocket.writeToNbt(rocketNbt);
			
			rocketsNbt.put(rocketUuid.toString(), rocketNbt);
		}
		
		nbt.put(ROCKETS_KEY, rocketsNbt);
	}
	
	public void readFromNbt(NbtCompound nbt) {
		// This component persists between worlds, so we need to clear it.
		rockets.clear();
		
		var rocketsNbt = nbt.getCompound(ROCKETS_KEY);
		
		for (var key : rocketsNbt.getKeys()) {
			var rocketUuid = UUID.fromString(key);
			var rocketNbt = rocketsNbt.getCompound(key);
			
			rockets.put(rocketUuid, RocketManager.readFromNbt(rocketNbt));
		}
	}
}
