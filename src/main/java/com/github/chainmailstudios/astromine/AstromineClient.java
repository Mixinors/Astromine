package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.registry.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AstromineClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AstromineEntityRenderers.initialize();
		AstromineBlockEntityRenderers.initialize();
		AstromineClientModels.initialize();
		AstromineParticleFactories.initialize();
		AstromineSkyboxes.initialize();
		AstromineScreens.initialize();
		AstromineClientCallbacks.initialize();
		AstromineClientPackets.initialize();
		AstromineRenderLayers.initialize();
		AstrominePatchouliPages.initialize();
	}
}
