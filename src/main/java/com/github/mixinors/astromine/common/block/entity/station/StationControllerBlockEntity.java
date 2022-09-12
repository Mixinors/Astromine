package com.github.mixinors.astromine.common.block.entity.station;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class StationControllerBlockEntity extends ExtendedBlockEntity {
	private static final String OWNER_UUID_KEY = "OwnerUuid";
	private static final String STATION_UUID_KEY = "StationUuid";
	
	@Nullable
	private UUID ownerUuid = null;
	
	@Nullable
	private UUID stationUuid = null;
	
	public StationControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ROCKET_CONTROLLER, blockPos, blockState);
	}
	
	@Nullable
	public Station getStation() {
		if (ownerUuid == null) return null;
		
		if (stationUuid == null) {
			// Expensive lookup.
			var station = StationManager.get(pos);
			
			if (station == null) {
				// TODO: Remove after debugging. Or leave, because it's useful if the Rocket is corrupted.
				var world = getWorld();
				var pos = getPos();
				
				if (world == null) return null;
				
				station = StationManager.create(world.getRegistryKey(), pos, UUID.randomUUID(), ownerUuid, "Station");
			}
			
			stationUuid = station.getUuid();
			
			return station;
		} else {
			// Cheap lookup.
			return StationManager.get(stationUuid);
		}
	}
	
	public void setStation(Station station) {
		this.stationUuid = station.getUuid();
	}
	
	@Nullable
	public UUID getOwnerUuid() {
		return ownerUuid;
	}
	
	public void setOwnerUuid(@Nullable UUID ownerUuid) {
		this.ownerUuid = ownerUuid;
	}
	
	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		
		if (ownerUuid != null) {
			nbt.putUuid(OWNER_UUID_KEY, ownerUuid);
		}
		
		if (stationUuid != null) {
			nbt.putUuid(STATION_UUID_KEY, stationUuid);
		}
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		super.readNbt(nbt);
		
		if (nbt.contains(OWNER_UUID_KEY)) {
			ownerUuid = nbt.getUuid(OWNER_UUID_KEY);
		}
		
		if (nbt.contains(STATION_UUID_KEY)) {
			stationUuid = nbt.getUuid(STATION_UUID_KEY);
		}
	}
}
