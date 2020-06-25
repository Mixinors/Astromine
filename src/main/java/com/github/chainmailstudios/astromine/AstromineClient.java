package com.github.chainmailstudios.astromine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import com.github.chainmailstudios.astromine.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.SuperSpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityRenderers;
import com.github.chainmailstudios.astromine.registry.AstromineClientCallbacks;
import com.github.chainmailstudios.astromine.registry.AstromineClientPackets;
import com.github.chainmailstudios.astromine.registry.AstromineEntityRenderers;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineParticles;
import com.github.chainmailstudios.astromine.registry.AstromineRenderLayers;
import com.github.chainmailstudios.astromine.registry.AstromineScreens;
import com.github.chainmailstudios.astromine.registry.AstromineSkyboxes;
import com.github.chainmailstudios.astromine.registry.AstromineSounds;

@Environment(EnvType.CLIENT)
public class AstromineClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AstromineEntityRenderers.initialize();
		AstromineBlockEntityRenderers.initialize();
		AstromineSounds.initialize();
		AstromineParticles.initialize();
		AstromineSkyboxes.initialize();
		AstromineScreens.initialize();
		AstromineClientCallbacks.initialize();
		AstromineClientPackets.initialize();
		AstromineRenderLayers.initialize();

		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SPACE_SLIME, (dispatcher, context) -> new SpaceSlimeEntityRenderer(dispatcher));

		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SUPER_SPACE_SLIME, (dispatcher, context) -> new SuperSpaceSlimeEntityRenderer(dispatcher));
	}
}
