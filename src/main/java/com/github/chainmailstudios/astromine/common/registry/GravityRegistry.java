package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class GravityRegistry {
	private final Map<Identifier, Double> ENTRIES = new HashMap<>();

	public static final GravityRegistry INSTANCE = new GravityRegistry();

	private GravityRegistry() {
		// Unused.
	}

	public double get(Identifier identifier) {
		return ENTRIES.getOrDefault(identifier, 0.98D);
	}

	public void register(Identifier identifier, double gravity) {
		ENTRIES.put(identifier, gravity);
	}

	public void unregister(Identifier identifier, double gravity) {
		ENTRIES.remove(identifier, gravity);
	}
}
