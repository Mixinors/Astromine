package com.github.chainmailstudios.astromine.common.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class BreathableRegistry extends BetaRegistry<EntityType<?>, Tag<Fluid>> {
	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	private BreathableRegistry() {
		// Locked.
	}

	@SafeVarargs
	public final void register(EntityType<?> type, Tag<Fluid>... tags) {
		for (Tag<Fluid> tag : tags) {
			register(type, tag);
		}
	}
}
