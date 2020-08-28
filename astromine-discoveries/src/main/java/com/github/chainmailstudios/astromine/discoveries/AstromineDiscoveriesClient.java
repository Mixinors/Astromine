package com.github.chainmailstudios.astromine.discoveries;

import com.github.chainmailstudios.astromine.AstromineClient;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesEntityRenderers;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesParticleFactories;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesSkyboxes;

public class AstromineDiscoveriesClient extends AstromineClient {
	@Override
	public void onInitializeClient() {
		AstromineDiscoveriesSkyboxes.initialize();
		AstromineDiscoveriesEntityRenderers.initialize();
		AstromineDiscoveriesParticleFactories.initialize();
	}
}
