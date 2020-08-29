package com.github.chainmailstudios.astromine.discoveries;

import net.minecraft.client.render.RenderLayer;

import com.github.chainmailstudios.astromine.AstromineClient;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesBlockEntityRenderers;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesClientCallbacks;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesClientModels;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesEntityRenderers;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesParticleFactories;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesRenderLayers;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesScreens;
import com.github.chainmailstudios.astromine.discoveries.registry.client.AstromineDiscoveriesSkyboxes;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.registry.client.AstromineRenderLayers;

public class AstromineDiscoveriesClient extends AstromineClient {
	@Override
	public void onInitializeClient() {
		AstromineDiscoveriesSkyboxes.initialize();
		AstromineDiscoveriesEntityRenderers.initialize();
		AstromineDiscoveriesParticleFactories.initialize();
		AstromineDiscoveriesBlockEntityRenderers.initialize();
		AstromineDiscoveriesRenderLayers.initialize();
		AstromineDiscoveriesScreens.initialize();
		AstromineDiscoveriesClientCallbacks.initialize();
		AstromineDiscoveriesClientModels.initialize();
	}
}
