package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

public class AstromineRenderLayers {
	public static void initialize() {
		BlockRenderLayerMap.INSTANCE.putBlock(AstromineBlocks.ENERGY_WIRE_CONNECTOR, RenderLayer.getCutout());
	}

	/**
	 * @param block Block instance to be registered
	 * @param renderLayer RenderLayer of block instance to be registered
	 * @return Block instance registered
	 */
	static <T extends Block> T register(T block, RenderLayer renderLayer) {
		BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
		return block;
	}
}
