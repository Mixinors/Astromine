package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class AMBlockRenderLayers {
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlock(AMBlocks.ROCKET_WINDOW.get(), RenderLayer.getTranslucent());
	}
}
