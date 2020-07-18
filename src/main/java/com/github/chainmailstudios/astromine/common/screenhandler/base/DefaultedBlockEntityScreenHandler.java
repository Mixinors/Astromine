package com.github.chainmailstudios.astromine.common.screenhandler.base;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;

import spinnery.common.handler.BaseScreenHandler;

public abstract class DefaultedBlockEntityScreenHandler extends BaseScreenHandler {
	public BlockEntity syncBlockEntity;

	public DefaultedBlockEntityScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		syncBlockEntity = world.getBlockEntity(position);
	}
}
