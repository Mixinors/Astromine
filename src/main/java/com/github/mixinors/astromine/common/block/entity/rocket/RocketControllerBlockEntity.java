package com.github.mixinors.astromine.common.block.entity.rocket;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RocketControllerBlockEntity extends ExtendedBlockEntity {
	@Nullable
	private UUID rocketUuid = null;
	
	public RocketControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ROCKET_CONTROLLER, blockPos, blockState);
	}
	
	public Rocket getRocket() {
		if (rocketUuid == null) {
			// Expensive lookup.
			var pos = getPos();
			var x = pos.getX();
			x = x - (x % 32);
			var z = pos.getZ();
			z = z - (z % 32);
			
			var chunkPos = new ChunkPos(x,z);
			
			var rocket = RocketManager.get(chunkPos);
			
			if (rocket == null) {
				// TODO: Remove after debugging. Or leave, because it's useful if the Rocket is corrupted.
				rocket = RocketManager.create(UUID.randomUUID());
			}
			
			rocketUuid = rocket.getUuid();
			
			return rocket;
		} else {
			// Cheap lookup.
			return RocketManager.get(rocketUuid);
		}
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
