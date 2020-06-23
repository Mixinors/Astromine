package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.container.base.BetaContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class CreativeTankContainer extends BetaContainer {
	public CreativeTankContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.CREATIVE_TANK;
	}
}
