package com.github.chainmailstudios.astromine.discoveries.registry.client;

import com.github.chainmailstudios.astromine.discoveries.client.render.entity.RocketEntityRenderer;
import com.github.chainmailstudios.astromine.discoveries.client.render.entity.SpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.discoveries.client.render.entity.SuperSpaceSlimeEntityRenderer;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineEntityTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineEntityRenderers;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class AstromineDiscoveriesEntityRenderers extends AstromineEntityRenderers {
	public static void initialize() {
		EntityRendererRegistry.INSTANCE.register(AstromineDiscoveriesEntityTypes.ROCKET, RocketEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(AstromineDiscoveriesEntityTypes.SPACE_SLIME, (dispatcher, context) -> new SpaceSlimeEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(AstromineDiscoveriesEntityTypes.SUPER_SPACE_SLIME, (dispatcher, context) -> new SuperSpaceSlimeEntityRenderer(dispatcher));
	}
}
