package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyItemContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import spinnery.widget.WSlot;

public class ElectricalSmelterContainer extends DefaultedEnergyItemContainer {
	public ElectricalSmelterContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);

		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(0);
		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(1);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.ELECTRICAL_SMELTER;
	}
}
