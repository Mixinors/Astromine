package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.SuperSpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

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

		EntityRendererRegistry.INSTANCE.register(
				AstromineEntities.SPACE_SLIME,
				(dispatcher, context) -> new SpaceSlimeEntityRenderer(dispatcher)
		);

		EntityRendererRegistry.INSTANCE.register(
				AstromineEntities.SUPER_SPACE_SLIME,
				(dispatcher, context) -> new SuperSpaceSlimeEntityRenderer(dispatcher)
		);
	}
}
