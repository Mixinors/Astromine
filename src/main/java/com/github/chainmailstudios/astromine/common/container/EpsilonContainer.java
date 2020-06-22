package com.github.chainmailstudios.astromine.common.container;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.container.BaseContainer;

public class EpsilonContainer extends BaseContainer {
	BlockState state;

	public EpsilonContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		world.getBlockState(position);
	}
}
