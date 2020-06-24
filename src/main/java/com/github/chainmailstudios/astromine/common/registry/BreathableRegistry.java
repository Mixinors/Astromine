package com.github.chainmailstudios.astromine.common.registry;

import com.github.chainmailstudios.astromine.common.registry.base.UniDirectionalRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineTags;

import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class BreathableRegistry extends UniDirectionalRegistry<EntityType<?>, Tag<Fluid>> {
	public static final BreathableRegistry INSTANCE = new BreathableRegistry();

	private BreathableRegistry() {
		super(AstromineTags.NORMAL_BREATHABLE);
	}

	@SafeVarargs
	public final void register(EntityType<?> type, Tag<Fluid>... tags) {
		for (Tag<Fluid> tag : tags) {
			register(type, tag);
		}
	}
}
