package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class GravityRegistry {
	private final Map<RegistryKey<DimensionType>, Double> ENTRIES = new HashMap<>();

	public static final GravityRegistry INSTANCE = new GravityRegistry();

	private GravityRegistry() {
		// Unused.
	}

	public double get(RegistryKey<DimensionType> identifier) {
		return ENTRIES.getOrDefault(identifier, 0.08D);
	}

	public void register(RegistryKey<DimensionType> identifier, double gravity) {
		ENTRIES.put(identifier, gravity);
	}

	public void unregister(RegistryKey<DimensionType> identifier, double gravity) {
		ENTRIES.remove(identifier, gravity);
	}
}
