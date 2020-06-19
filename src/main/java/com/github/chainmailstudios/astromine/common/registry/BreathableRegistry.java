package com.github.chainmailstudios.astromine.common.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;

public class BreathableRegistry {
	private final Multimap<EntityType<?>, Fluid> ENTRIES = HashMultimap.create();

	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	private BreathableRegistry() {
		// Unused.
	}

	public void register(EntityType<?> type, Fluid... fluids) {
		for (Fluid fluid : fluids) {
			ENTRIES.put(type, fluid);
		}
	}

	public void unregister(EntityType<?> type, Fluid fluid) {
		ENTRIES.remove(type, fluid);
	}

	public boolean contains(EntityType<?> type, Fluid fluid) {
		return ENTRIES.containsEntry(type, fluid);
	}
}
