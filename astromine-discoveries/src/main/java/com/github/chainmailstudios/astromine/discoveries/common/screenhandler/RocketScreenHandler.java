package com.github.chainmailstudios.astromine.discoveries.common.screenhandler;

import com.github.chainmailstudios.astromine.common.screenhandler.base.entity.ComponentEntityFluidScreenHandler;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;

public class RocketScreenHandler extends ComponentEntityFluidScreenHandler {
	public RocketScreenHandler(int syncId, PlayerEntity player, int entityId) {
		super(AstromineDiscoveriesScreenHandlers.ROCKET, syncId, player, entityId);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
	}
}
