package com.github.chainmailstudios.astromine.discoveries.registry.client;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import com.github.chainmailstudios.astromine.discoveries.client.screen.RocketHandledScreen;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesScreenHandlers;
import com.github.chainmailstudios.astromine.registry.client.AstromineScreens;

public class AstromineDiscoveriesScreens extends AstromineScreens {
	public static void initialize() {
		ScreenRegistry.register(AstromineDiscoveriesScreenHandlers.ROCKET, RocketHandledScreen::new);
	}
}
