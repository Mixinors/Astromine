package com.github.mixinors.astromine.common.block.entity.rocket;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class RocketControllerBlockEntity extends ExtendedBlockEntity {
	public RocketControllerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(AMBlockEntityTypes.ROCKET_CONTROLLER, blockPos, blockState);
	}
}
