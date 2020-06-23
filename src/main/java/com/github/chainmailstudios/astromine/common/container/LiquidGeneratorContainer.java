package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.container.base.AlphaContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class LiquidGeneratorContainer extends AlphaContainer {
	public LiquidGeneratorContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos blockPos) {
		super(synchronizationID, playerInventory, blockPos);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.LIQUID_GENERATOR;
	}
}
