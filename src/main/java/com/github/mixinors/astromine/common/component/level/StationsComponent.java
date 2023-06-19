package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.station.Station;
import net.minecraft.nbt.NbtCompound;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StationsComponent {
	private static final String STATIONS_KEY = "Stations";
	
	private final Map<UUID, Station> stations = new ConcurrentHashMap<>();
	
	public StationsComponent() {
	}
	
	public void add(Station rocket) {
		this.stations.put(rocket.getUuid(), rocket);
	}
	
	public Station get(UUID rocketUuid) {
		return this.stations.get(rocketUuid);
	}
	
	public Collection<Station> getAll() {
		return this.stations.values();
	}
	
	public void remove(Station rocket) {
		this.stations.remove(rocket.getUuid());
	}
	
	public void tick() {
		this.stations.values().forEach(Station::tick);
	}
	
	public void writeToNbt(NbtCompound nbt) {
		var stationsNbt = new NbtCompound();
		
		for (var entry : stations.entrySet()) {
			var stationUuid = entry.getKey();
			var station = entry.getValue();
			
			var stationNbt = new NbtCompound();
			station.writeToNbt(stationNbt);
			
			stationsNbt.put(stationUuid.toString(), stationNbt);
		}
		
		nbt.put(STATIONS_KEY, stationsNbt);
	}
	
	public void readFromNbt(NbtCompound nbt) {
		// This component persists between worlds, so we need to clear it.
		stations.clear();
		
		var rocketsNbt = nbt.getCompound(STATIONS_KEY);
		
		for (var key : rocketsNbt.getKeys()) {
			var stationUuid = UUID.fromString(key);
			var stationNbt = rocketsNbt.getCompound(key);
			
			stations.computeIfAbsent(stationUuid, uuid1 -> StationManager.readFromNbt(stationNbt));
		}
	}
}
