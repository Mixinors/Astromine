package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.client.render.skybox.Skybox;
import com.github.chainmailstudios.astromine.common.registry.GravityRegistry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class SkyboxRegistry {
	private final Map<Identifier, Skybox> ENTRIES = new HashMap<>();

	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();

	private SkyboxRegistry() {
		// Unused.
	}

	public Skybox get(Identifier identifier) {
		return ENTRIES.get(identifier);
	}

	public void register(Identifier identifier, Skybox skybox) {
		ENTRIES.put(identifier, skybox);
	}

	public void unregister(Identifier identifier, double gravity) {
		ENTRIES.remove(identifier, gravity);
	}
}
