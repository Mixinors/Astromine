package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.station.Station;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class StationsComponent extends SavedData {
	public static final String FILE_ID = "astromine_stations";
	
	private static final String STATIONS_KEY = "Stations";
	
	private final Map<UUID, Station> stations = new ConcurrentHashMap<>();
	
	public StationsComponent() {
	}
	
	public static SavedData.Factory<StationsComponent> factory() {
		return new SavedData.Factory<>(StationsComponent::new, StationsComponent::load);
	}
	
	public static StationsComponent load(CompoundTag nbt, HolderLookup.Provider registries) {
		var component = new StationsComponent();
		component.readFromNbt(nbt);
		return component;
	}
	
	public void add(Station rocket) {
		this.stations.put(rocket.getUuid(), rocket);
		setDirty();
	}
	
	public Station get(UUID rocketUuid) {
		return this.stations.get(rocketUuid);
	}
	
	public Collection<Station> getAll() {
		return this.stations.values();
	}
	
	public void remove(Station rocket) {
		this.stations.remove(rocket.getUuid());
		setDirty();
	}
	
	public void tick() {
		this.stations.values().forEach(Station::tick);
	}
	
	public boolean isEmpty() {
		return stations.isEmpty();
	}
	
	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registries) {
		writeToNbt(nbt);
		return nbt;
	}
	
	public void writeToNbt(CompoundTag nbt) {
		var stationsNbt = new CompoundTag();
		
		for (var entry : stations.entrySet()) {
			var stationUuid = entry.getKey();
			var station = entry.getValue();
			
			var stationNbt = new CompoundTag();
			station.writeToNbt(stationNbt);
			
			stationsNbt.put(stationUuid.toString(), stationNbt);
		}
		
		nbt.put(STATIONS_KEY, stationsNbt);
	}
	
	public void readFromNbt(CompoundTag nbt) {
		// This component persists between worlds, so we need to clear it.
		stations.clear();
		
		var rocketsNbt = nbt.getCompound(STATIONS_KEY);
		
		for (var key : rocketsNbt.getAllKeys()) {
			var stationUuid = UUID.fromString(key);
			var stationNbt = rocketsNbt.getCompound(key);
			
			stations.computeIfAbsent(stationUuid, uuid1 -> StationManager.readFromNbt(stationNbt));
		}
	}
}
