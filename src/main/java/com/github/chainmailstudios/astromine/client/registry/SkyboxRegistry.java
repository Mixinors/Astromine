package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.client.render.skybox.Skybox;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class SkyboxRegistry {
	private final Map<RegistryKey<DimensionType>, Skybox> ENTRIES = new HashMap<>();

	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();

	private SkyboxRegistry() {
		// Unused.
	}

	public Skybox get(RegistryKey<DimensionType> identifier) {
		return ENTRIES.get(identifier);
	}

	public void register(RegistryKey<DimensionType> identifier, Skybox skybox) {
		ENTRIES.put(identifier, skybox);
	}

	public void unregister(RegistryKey<DimensionType> identifier, double gravity) {
		ENTRIES.remove(identifier, gravity);
	}
}
