package com.github.chainmailstudios.astromine.common.world.generation;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public interface BiomeRegistryCallback {
	Event<BiomeRegistryCallback> EVENT = EventFactory.createArrayBacked(BiomeRegistryCallback.class, callbacks -> (manager, key, biome) -> {
		for (BiomeRegistryCallback callback : callbacks) {
			callback.accept(manager, key, biome);
		}
	});

	void accept(DynamicRegistryManager manager, RegistryKey<Biome> key, Biome biome);
}
