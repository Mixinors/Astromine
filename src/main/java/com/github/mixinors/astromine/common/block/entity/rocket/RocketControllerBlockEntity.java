package com.github.mixinors.astromine.common.block.entity.rocket;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.rocket.Rocket;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

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
		if (ownerUuid == null) return null;
		
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
				rocket = RocketManager.create(ownerUuid, UUID.randomUUID());
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
		
		if (rocketUuid != null) {
			nbt.putUuid(ROCKET_UUID_KEY, rocketUuid);
		}
	}
	
	@Override
	public void readNbt(@NotNull NbtCompound nbt) {
		super.readNbt(nbt);
		
		if (nbt.contains(OWNER_UUID_KEY)) {
			ownerUuid = nbt.getUuid(OWNER_UUID_KEY);
		}

		if (nbt.contains(ROCKET_UUID_KEY)) {
			rocketUuid = nbt.getUuid(ROCKET_UUID_KEY);
		}
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
