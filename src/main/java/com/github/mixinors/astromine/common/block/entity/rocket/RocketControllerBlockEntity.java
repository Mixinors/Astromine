package com.github.mixinors.astromine.common.block.entity.rocket;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RocketControllerBlockEntity extends ExtendedBlockEntity {
	private UUID rocketUuid;
	
	public RocketControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ROCKET_CONTROLLER, blockPos, blockState);
	}
	
	public Rocket getRocket() {
		return RocketManager.getOrCreate(rocketUuid);
	}
	
	public void setRocket(Rocket rocket) {
		this.rocketUuid = rocket.getUuid();
	}
	
	@Override
	public @Nullable SimpleItemStorage getItemStorage() {
		return getRocket().getItemStorage();
	}
	
	@Override
	public @Nullable SimpleFluidStorage getFluidStorage() {
		return getRocket().getFluidStorage();
	}
}
