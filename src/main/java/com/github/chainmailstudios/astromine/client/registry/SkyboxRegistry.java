package com.github.chainmailstudios.astromine.client.registry;

import java.util.HashMap;
import java.util.Map;

import com.github.chainmailstudios.astromine.client.render.skybox.Skybox;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class SkyboxRegistry {
	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();
	private final Map<RegistryKey<DimensionType>, Skybox> ENTRIES = new HashMap<>();

	private SkyboxRegistry() {
		// Unused.
	}

	public Skybox get(RegistryKey<DimensionType> identifier) {
		return this.ENTRIES.get(identifier);
	}

	public void register(RegistryKey<DimensionType> identifier, Skybox skybox) {
		this.ENTRIES.put(identifier, skybox);
	}

	public void unregister(RegistryKey<DimensionType> identifier, double gravity) {
		this.ENTRIES.remove(identifier, gravity);
	}
}
