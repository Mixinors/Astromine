package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.rocket.Rocket;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.WorldProperties;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RocketComponent implements AutoSyncedComponent {
	private final WorldProperties worldProperties;
	
	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	
	public RocketComponent(WorldProperties worldProperties) {
		this.worldProperties = worldProperties;
	}
	
	public void add(Rocket rocket) {
		this.rockets.put(rocket.getUuid(), rocket);
	}
	
	public Rocket get(UUID uuid) {
		return this.rockets.get(uuid);
	}
	
	public void remove(Rocket rocket) {
		this.rockets.remove(rocket.getUuid());
	}
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		for (var entry : rockets.entrySet()) {
			var uuid = entry.getKey();
			var rocket = entry.getValue();
			
			var rocketNbt = new NbtCompound();
			rocket.writeToNbt(rocketNbt);
			
			nbt.put(uuid.toString(), rocketNbt);
		}
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		for (var key : nbt.getKeys()) {
			var uuid = UUID.fromString(key);
			var rocket = rockets.computeIfAbsent(uuid, k -> new Rocket());
			
			var rocketNbt = nbt.getCompound(key);
			
			rocket.readFromNbt(rocketNbt);
		}
	}
	
	// TODO: Check if player has a Rocket-related ScreenHandler open, or is in the Rocket Dimension!
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		if (player.getWorld().getRegistryKey().getValue().equals(AMCommon.id("rocket"))) {
			return true;
		} else {
			return false;
		}
	}
}
