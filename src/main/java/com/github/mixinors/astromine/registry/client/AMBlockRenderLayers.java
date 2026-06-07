package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class AMBlockRenderLayers {
	public static void init(IEventBus modBus) {
		modBus.addListener(AMBlockRenderLayers::register);
	}
	
	private static void register(FMLClientSetupEvent event) {
		event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(AMBlocks.ROCKET_WINDOW.get(), RenderType.translucent()));
	}
}
