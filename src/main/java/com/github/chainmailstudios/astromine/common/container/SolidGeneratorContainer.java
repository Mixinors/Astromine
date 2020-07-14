package com.github.chainmailstudios.astromine.common.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyItemContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import spinnery.widget.WSlot;

public class SolidGeneratorContainer extends DefaultedEnergyItemContainer {
	public SolidGeneratorContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos blockPos) {
		super(synchronizationID, playerInventory, blockPos);

		getInterface().createChild(WSlot::new).setInventoryNumber(1).setSlotNumber(0);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.SOLID_GENERATOR;
	}
}
