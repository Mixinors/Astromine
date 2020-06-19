package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.render.RocketEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntities.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineEntities.ROCKET, RocketEntityRenderer::new);
	}
}
