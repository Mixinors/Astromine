package com.github.mixinors.astromine.common.block.entity.station;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.station.Station;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class StationControllerBlockEntity extends ExtendedBlockEntity {
	private static final String OWNER_UUID_KEY = "OwnerUuid";
	private static final String STATION_UUID_KEY = "StationUuid";
	
	@Nullable
	private UUID ownerUuid = null;
	
	@Nullable
	private UUID stationUuid = null;
	
	public StationControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.STATION_CONTROLLER, blockPos, blockState);
	}
	
	@Nullable
	public Station getStation() {
		if (ownerUuid == null) return null;
		
		if (stationUuid == null) {
			// Expensive lookup.
			var world = getLevel();
			
			if (!(world instanceof ServerLevel serverLevel)) return null;
			
			var station = StationManager.get(world, world.dimension(), worldPosition);
			
			if (station == null) {
				var pos = getBlockPos();
				
				station = StationManager.create(serverLevel.getServer(), world.dimension(), pos, UUID.randomUUID(), ownerUuid, "Station");
			}
			
			stationUuid = station.getUuid();
			
			return station;
		} else {
			// Cheap lookup.
			var world = getLevel();
			return world == null ? null : StationManager.get(world, stationUuid);
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
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		
		if (ownerUuid != null) {
			nbt.putUUID(OWNER_UUID_KEY, ownerUuid);
		}
		
		if (stationUuid != null) {
			nbt.putUUID(STATION_UUID_KEY, stationUuid);
		}
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		
		if (nbt.contains(OWNER_UUID_KEY)) {
			ownerUuid = nbt.getUUID(OWNER_UUID_KEY);
		}
		
		if (nbt.contains(STATION_UUID_KEY)) {
			stationUuid = nbt.getUUID(STATION_UUID_KEY);
		}
	}
}
