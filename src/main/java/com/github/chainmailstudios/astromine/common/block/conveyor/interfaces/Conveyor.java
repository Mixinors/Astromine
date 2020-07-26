package com.github.chainmailstudios.astromine.common.block.conveyor.interfaces;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface Conveyor extends ConveyableBlock {
    /**
     * @return How many ticks it takes for an item to traverse one block.
     */
    int getSpeed();

    /**
     * @return The type of conveyor that this is.
     */
    ConveyorType getType();

	default void updateDiagonals(World world, Block block, BlockPos pos) {
		world.updateNeighbors(pos, block);
		for (Direction direction : Direction.values()) {
			world.updateNeighbor(pos.offset(direction).down(1), block, pos);
		}
	}
}
