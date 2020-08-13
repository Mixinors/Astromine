package com.github.chainmailstudios.astromine.common.world.generation;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;

import java.util.function.BiConsumer;

public interface BiomeRegistryCallback extends BiConsumer<DynamicRegistryManager, Biome> {
	Event<BiomeRegistryCallback> EVENT = EventFactory.createArrayBacked(BiomeRegistryCallback.class, callbacks -> (manager, biome) -> {
		for (BiomeRegistryCallback callback : callbacks) {
			callback.accept(manager, biome);
		}
	});
}
