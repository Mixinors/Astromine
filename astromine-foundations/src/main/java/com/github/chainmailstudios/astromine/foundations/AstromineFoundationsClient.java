package com.github.chainmailstudios.astromine.foundations;

import com.github.chainmailstudios.astromine.foundations.datagen.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.datagen.registry.client.AstromineFoundationsBlockEntityRenderers;
import com.github.chainmailstudios.astromine.registry.client.AstromineRenderLayers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.RenderLayer;

public class AstromineFoundationsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AstromineFoundationsBlockEntityRenderers.initialize();
		AstromineRenderLayers.register(AstromineFoundationsBlocks.ALTAR, RenderLayer.getCutout());
		AstromineRenderLayers.register(AstromineFoundationsBlocks.ITEM_DISPLAYER, RenderLayer.getCutout());
	}
}
