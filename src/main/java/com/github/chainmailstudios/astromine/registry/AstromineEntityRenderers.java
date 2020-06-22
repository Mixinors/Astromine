package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.RocketEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.ROCKET, RocketEntityRenderer::new);
	}
}
