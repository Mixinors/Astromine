package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntities.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
	}
}
