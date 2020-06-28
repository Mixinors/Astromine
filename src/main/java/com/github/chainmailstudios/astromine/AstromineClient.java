package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import com.github.chainmailstudios.astromine.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.SuperSpaceSlimeEntityRenderer;

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

		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SPACE_SLIME, (dispatcher, context) -> new SpaceSlimeEntityRenderer(dispatcher));

		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SUPER_SPACE_SLIME, (dispatcher, context) -> new SuperSpaceSlimeEntityRenderer(dispatcher));
	}
}
