package com.github.chainmailstudios.astromine.common.container.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import spinnery.common.container.BaseContainer;

public abstract class DefaultedBlockEntityContainer extends BaseContainer {
	public BlockEntity syncBlockEntity;

	public DefaultedBlockEntityContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		syncBlockEntity = world.getBlockEntity(position);
	}
}
