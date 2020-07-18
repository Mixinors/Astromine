package com.github.chainmailstudios.astromine.common.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;

public class FluidExtractorScreenHandler extends DefaultedEnergyFluidScreenHandler {
	public FluidExtractorScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos blockPos) {
		super(synchronizationID, playerInventory, blockPos);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.FLUID_EXTRACTOR;
	}
}
