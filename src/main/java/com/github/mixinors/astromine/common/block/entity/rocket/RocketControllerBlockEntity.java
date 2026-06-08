package com.github.mixinors.astromine.common.block.entity.rocket;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class RocketControllerBlockEntity extends ExtendedBlockEntity {
	private static final String OWNER_UUID_KEY = "OwnerUuid";
	private static final String ROCKET_UUID_KEY = "RocketUuid";
	
	@Nullable
	private UUID ownerUuid = null;
	
	@Nullable
	private UUID rocketUuid = null;
	
	public RocketControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ROCKET_CONTROLLER, blockPos, blockState);
	}
	
	@Nullable
	public Rocket getRocket() {
		var level = getLevel();
		return level == null ? null : RocketManager.get(level, RocketManager.getInteriorBaseChunk(getBlockPos()));
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
		super.saveAdditional(nbt, registries);
		
		if (ownerUuid != null) {
			nbt.putUUID(OWNER_UUID_KEY, ownerUuid);
		}
		
		if (rocketUuid != null) {
			nbt.putUUID(ROCKET_UUID_KEY, rocketUuid);
		}
	}
	
	@Override
	protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.Provider registries) {
		super.loadAdditional(nbt, registries);
		
		if (nbt.contains(OWNER_UUID_KEY)) {
			ownerUuid = nbt.getUUID(OWNER_UUID_KEY);
		}

		if (nbt.contains(ROCKET_UUID_KEY)) {
			rocketUuid = nbt.getUUID(ROCKET_UUID_KEY);
		}
	}
	
	@Override
	public @Nullable SimpleItemStorage getItemStorage() {
		var rocket = getRocket();
		if (rocket == null) return null;
		return rocket.getItemStorage();
	}
	
	@Override
	public @Nullable SimpleFluidStorage getFluidStorage() {
		var rocket = getRocket();
		if (rocket == null) return null;
		return rocket.getFluidStorage();
	}
}
