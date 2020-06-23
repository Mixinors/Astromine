package com.github.chainmailstudios.astromine.common.container;

import com.github.chainmailstudios.astromine.common.container.base.AlphaContainer;
import com.github.chainmailstudios.astromine.registry.AstromineContainers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class VentContainer extends AlphaContainer {
	public VentContainer(int synchronizationID, PlayerInventory playerInventory, BlockPos position) {
		super(synchronizationID, playerInventory, position);
	}

	@Override
	public ScreenHandlerType<?> getType() {
		return AstromineContainers.VENT;
	}
}
