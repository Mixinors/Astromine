package com.github.chainmailstudios.astromine.common.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class GravityRegistry {
	public static final GravityRegistry INSTANCE = new GravityRegistry();
	private final Map<RegistryKey<DimensionType>, Double> ENTRIES = new HashMap<>();

	private GravityRegistry() {
		// Unused.
	}

	public double get(RegistryKey<DimensionType> identifier) {
		return this.ENTRIES.getOrDefault(identifier, 0.08D);
	}

	public void register(RegistryKey<DimensionType> identifier, double gravity) {
		this.ENTRIES.put(identifier, gravity);
	}

	public void unregister(RegistryKey<DimensionType> identifier, double gravity) {
		this.ENTRIES.remove(identifier, gravity);
	}
}
