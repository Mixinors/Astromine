package com.github.chainmailstudios.astromine.common.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class BreathableRegistry {
	private final Multimap<EntityType<?>, Tag<Fluid>> ENTRIES = HashMultimap.create();

	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	private BreathableRegistry() {
		// Unused.
	}

	public void register(EntityType<?> type, Tag<Fluid> tag) {
		ENTRIES.put(type, tag);
	}

	public void register(EntityType<?> type, Tag<Fluid>... tags) {
		for (Tag<Fluid> tag : tags) {
			register(type, tag);
		}
	}

	public void unregister(EntityType<?> type, Tag<Fluid> tag) {
		ENTRIES.remove(type, tag);
	}

	public boolean contains(EntityType<?> type, Fluid fluid) {
		if (ENTRIES.containsKey(type)) {
			for (Tag<Fluid> tag : ENTRIES.get(type)) {
				if (tag.contains(fluid)) return true;
			}
		}
		return false;
	}
}
