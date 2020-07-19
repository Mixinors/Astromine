package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.MultiRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class BreathableRegistry extends MultiRegistry<EntityType<?>, Tag<Fluid>> {
	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	private BreathableRegistry() {
		super();
	}

	@SafeVarargs
	public final void register(EntityType<?> type, Tag<Fluid>... tags) {
		for (Tag<Fluid> tag : tags) {
			register(type, tag);
		}
	}

	public boolean canBreathe(EntityType<?> type, Fluid fluid) {
		return get(type).stream().anyMatch(tag -> tag.contains(fluid));
	}
}
