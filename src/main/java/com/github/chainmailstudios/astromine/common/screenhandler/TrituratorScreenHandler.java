package com.github.chainmailstudios.astromine.common.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import spinnery.widget.WSlot;

public class TrituratorScreenHandler extends DefaultedEnergyItemScreenHandler {
	public TrituratorScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(0);
		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(1);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.TRITURATOR;
	}
}
