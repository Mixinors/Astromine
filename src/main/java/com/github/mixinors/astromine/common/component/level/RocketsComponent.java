package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class RocketsComponent extends SavedData {
	public static final String FILE_ID = "astromine_rockets";
	
	private static final String ROCKETS_KEY = "Rockets";
	
	private final Map<UUID, Rocket> rockets = new ConcurrentHashMap<>();
	
	public RocketsComponent() {
	}
	
	public static SavedData.Factory<RocketsComponent> factory() {
		return new SavedData.Factory<>(RocketsComponent::new, RocketsComponent::load);
	}
	
	public static RocketsComponent load(CompoundTag nbt, HolderLookup.Provider registries) {
		var component = new RocketsComponent();
		component.readFromNbt(nbt);
		return component;
	}
	
	public void add(Rocket rocket) {
		this.rockets.put(rocket.getUuid(), rocket);
		setDirty();
	}
	
	public Rocket get(UUID rocketUuid) {
		return this.rockets.get(rocketUuid);
	}
	
	public Collection<Rocket> getAll() {
		return this.rockets.values();
	}
	
	public void remove(Rocket rocket) {
		this.rockets.remove(rocket.getUuid());
		setDirty();
	}
	
	public void tick() {
		this.rockets.values().forEach(Rocket::tick);
	}
	
	public boolean isEmpty() {
		return rockets.isEmpty();
	}
	
	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
		writeToNbt(nbt);
		return nbt;
	}
	
	public void writeToNbt(CompoundTag nbt) {
		var rocketsNbt = new CompoundTag();
		
		for (var entry : rockets.entrySet()) {
			var rocketUuid = entry.getKey();
			var rocket = entry.getValue();
			
			var rocketNbt = new CompoundTag();
			rocket.writeToNbt(rocketNbt);
			
			rocketsNbt.put(rocketUuid.toString(), rocketNbt);
		}
		
		nbt.put(ROCKETS_KEY, rocketsNbt);
	}
	
	public void readFromNbt(CompoundTag nbt) {
		// This component persists between worlds, so we need to clear it.
		rockets.clear();
		
		var rocketsNbt = nbt.getCompound(ROCKETS_KEY);
		
		for (var key : rocketsNbt.getAllKeys()) {
			var rocketUuid = UUID.fromString(key);
			var rocketNbt = rocketsNbt.getCompound(key);
			
			rockets.put(rocketUuid, RocketManager.readFromNbt(rocketNbt));
		}
	}
}
