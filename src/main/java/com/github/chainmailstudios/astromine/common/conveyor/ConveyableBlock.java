package com.github.chainmailstudios.astromine.common.conveyor;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface ConveyableBlock {
    default void updateDiagonals(World world, Block block, BlockPos pos) {
		world.updateNeighbors(pos, block);
		for (Direction direction : Direction.values()) {
			world.updateNeighbor(pos.offset(direction).down(1), block, pos);
		}
	}
}
