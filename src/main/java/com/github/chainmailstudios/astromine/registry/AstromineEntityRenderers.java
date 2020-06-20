package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.render.RocketEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.ROCKET, RocketEntityRenderer::new);
	}
}
