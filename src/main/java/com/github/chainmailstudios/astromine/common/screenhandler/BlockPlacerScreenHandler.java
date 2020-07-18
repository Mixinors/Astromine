package com.github.chainmailstudios.astromine.common.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import spinnery.widget.WSlot;

public class BlockPlacerScreenHandler extends DefaultedEnergyItemScreenHandler {
	public BlockPlacerScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos blockPos) {
		super(synchronizationID, playerInventory, blockPos);

		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(0);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.BLOCK_PLACER;
	}
}
