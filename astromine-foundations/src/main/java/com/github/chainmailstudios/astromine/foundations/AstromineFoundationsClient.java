package com.github.chainmailstudios.astromine.foundations;

import com.github.chainmailstudios.astromine.foundations.registry.client.AstromineFoundationsBlockEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class AstromineFoundationsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AstromineFoundationsBlockEntityRenderers.initialize();
	}
}
