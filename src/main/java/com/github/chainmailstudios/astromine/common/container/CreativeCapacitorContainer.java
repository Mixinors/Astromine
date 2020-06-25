package com.github.chainmailstudios.astromine.common.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;

public class CreativeCapacitorContainer extends DefaultedEnergyContainer {
	public CreativeCapacitorContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.CREATIVE_CAPACITOR;
	}
}
