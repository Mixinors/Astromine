package com.github.chainmailstudios.astromine.registry.client;

import com.github.chainmailstudios.astromine.client.render.entity.BulletEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.RocketEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.client.render.entity.SuperSpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.BULLET_ENTITY_TYPE, BulletEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.ROCKET, RocketEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SPACE_SLIME, (dispatcher, context) -> new SpaceSlimeEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(AstromineEntityTypes.SUPER_SPACE_SLIME, (dispatcher, context) -> new SuperSpaceSlimeEntityRenderer(dispatcher));
	}
}
