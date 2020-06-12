package com.github.chainmailstudios.astromine;

import com.github.chainmailstudios.astromine.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.registry.AstromineEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class AstromineClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.INSTANCE.register(
				AstromineEntities.SPACE_SLIME,
				(dispatcher, context) -> {
					return new SpaceSlimeEntityRenderer(dispatcher);
				}
		);
	}
}
