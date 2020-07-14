package com.github.chainmailstudios.astromine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.github.chainmailstudios.astromine.registry.client.AstromineBlockEntityRenderers;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientCallbacks;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientModels;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientPackets;
import com.github.chainmailstudios.astromine.registry.client.AstromineEntityRenderers;
import com.github.chainmailstudios.astromine.registry.client.AstromineParticleFactories;
import com.github.chainmailstudios.astromine.registry.client.AstrominePatchouliPages;
import com.github.chainmailstudios.astromine.registry.client.AstromineRenderLayers;
import com.github.chainmailstudios.astromine.registry.client.AstromineScreens;
import com.github.chainmailstudios.astromine.registry.client.AstromineSkyboxes;
import com.github.chainmailstudios.astromine.registry.client.AstromineSounds;

@Environment(EnvType.CLIENT)
public class AstromineClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AstromineEntityRenderers.initialize();
		AstromineBlockEntityRenderers.initialize();
		AstromineClientModels.initialize();
		AstromineSounds.initialize();
		AstromineParticleFactories.initialize();
		AstromineSkyboxes.initialize();
		AstromineScreens.initialize();
		AstromineClientCallbacks.initialize();
		AstromineClientPackets.initialize();
		AstromineRenderLayers.initialize();
		AstrominePatchouliPages.initialize();
	}
}
