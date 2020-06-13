package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityRenderers;
import com.github.chainmailstudios.astromine.registry.AstromineEntityRenderers;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;
import net.fabricmc.api.ClientModInitializer;

public class AstromineClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AstromineEntityRenderers.initialize();
		AstromineBlockEntityRenderers.initialize();
		AstromineSounds.initialize();
	}
}
