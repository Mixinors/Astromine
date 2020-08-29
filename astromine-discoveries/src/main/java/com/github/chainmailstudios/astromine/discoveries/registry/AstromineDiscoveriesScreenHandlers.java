package com.github.chainmailstudios.astromine.discoveries.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.common.screenhandler.RocketScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;

public class AstromineDiscoveriesScreenHandlers extends AstromineScreenHandlers {
	public static final ScreenHandlerType<RocketScreenHandler> ROCKET = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("rocket"), ((syncId, inventory, buffer) -> {
		return new RocketScreenHandler(syncId, inventory.player, buffer.readInt());
	}));

	public static void initialize() {

	}
}
