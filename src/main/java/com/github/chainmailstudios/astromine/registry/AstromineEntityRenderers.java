package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntities.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
	}
}
