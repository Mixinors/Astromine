package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.container.base.DefaultedEnergyFluidContainer;
import com.github.chainmailstudios.astromine.common.container.base.DefaultedFluidContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ElectrolyzerContainer extends DefaultedEnergyFluidContainer {
	public ElectrolyzerContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.ELECTROLYZER;
	}
}
