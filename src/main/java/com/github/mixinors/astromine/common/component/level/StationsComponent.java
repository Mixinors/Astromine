package com.github.mixinors.astromine.common.component.level;

import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StationsComponent implements AutoSyncedComponent {
	private static final String STATIONS_KEY = "Stations";
	
	private final Map<UUID, Station> stations = new ConcurrentHashMap<>();
	
	public StationsComponent(World world) {
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
	
	@Override
	public void writeToNbt(NbtCompound nbt) {
		var stationsNbt = new NbtCompound();
		
		for (var entry : stations.entrySet()) {
			var uuid = entry.getKey();
			var station = entry.getValue();
			
			var stationNbt = new NbtCompound();
			station.writeToNbt(stationNbt);
			
			stationsNbt.put(uuid.toString(), stationNbt);
		}
		
		nbt.put(STATIONS_KEY, stationsNbt);
	}
	
	@Override
	public void readFromNbt(NbtCompound nbt) {
		var rocketsNbt = nbt.getCompound(STATIONS_KEY);
		
		for (var key : rocketsNbt.getKeys()) {
			var uuid = UUID.fromString(key);
			var rocketNbt = rocketsNbt.getCompound(key);
			stations.computeIfAbsent(uuid, uuid1 -> StationManager.readFromNbt(rocketNbt));
		}
	}
	
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player.getWorld().getRegistryKey().equals(AMWorlds.ROCKET_INTERIORS);
	}
}
