package com.github.chainmailstudios.astromine.common.screenhandler.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import spinnery.common.handler.BaseScreenHandler;

public abstract class DefaultedBlockEntityScreenHandler extends BaseScreenHandler {
	public DefaultedBlockEntity syncBlockEntity;

	public DefaultedBlockEntityScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory);

		syncBlockEntity = (DefaultedBlockEntity) world.getBlockEntity(position);

		if (!world.isClient) {
			syncBlockEntity.doNotSkipInventory();
			syncBlockEntity.sync();
		}
	}
}
