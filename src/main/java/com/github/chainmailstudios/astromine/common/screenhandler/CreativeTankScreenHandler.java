package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedFluidScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class CreativeTankScreenHandler extends DefaultedFluidScreenHandler {
	public CreativeTankScreenHandler(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineScreenHandlers.CREATIVE_TANK;
	}
}
