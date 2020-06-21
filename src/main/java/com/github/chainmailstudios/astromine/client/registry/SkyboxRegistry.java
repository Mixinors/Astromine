package com.github.chainmailstudios.astromine.client.registry;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.client.render.skybox.AbstractSkybox;

import java.util.HashMap;
import java.util.Map;

public class SkyboxRegistry {
	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();
	private final Map<RegistryKey<DimensionType>, AbstractSkybox> ENTRIES = new HashMap<>();

	private SkyboxRegistry() {
		// Unused.
	}

	public AbstractSkybox get(RegistryKey<DimensionType> identifier) {
		return this.ENTRIES.get(identifier);
	}

	public void register(RegistryKey<DimensionType> identifier, AbstractSkybox skybox) {
		this.ENTRIES.put(identifier, skybox);
	}

	public void unregister(RegistryKey<DimensionType> identifier, double gravity) {
		this.ENTRIES.remove(identifier, gravity);
	}
}
