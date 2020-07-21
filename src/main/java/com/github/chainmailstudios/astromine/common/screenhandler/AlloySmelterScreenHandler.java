package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WSlot;

public class AlloySmelterScreenHandler extends DefaultedEnergyItemScreenHandler {
	public AlloySmelterScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(0);
		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(1);
		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(2);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.ALLOY_SMELTER;
	}
}
