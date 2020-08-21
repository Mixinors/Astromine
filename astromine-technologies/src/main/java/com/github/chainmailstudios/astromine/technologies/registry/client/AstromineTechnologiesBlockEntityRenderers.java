package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.client.AstromineBlockEntityRenderers;
import com.github.chainmailstudios.astromine.technologies.client.render.block.HolographicBridgeBlockEntityRenderer;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;

public class AstromineTechnologiesBlockEntityRenderers extends AstromineBlockEntityRenderers {
	public static void initialize() {
		register(AstromineTechnologiesBlockEntityTypes.HOLOGRAPHIC_BRIDGE, HolographicBridgeBlockEntityRenderer::new);
	}
}
