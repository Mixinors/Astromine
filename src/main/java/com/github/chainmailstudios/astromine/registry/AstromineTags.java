package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

public class AstromineTags {
	public static final Tag<Fluid> NORMAL_BREATHABLE = TagRegistry.fluid(AstromineCommon.identifier("normal_breathable"));
	public static final Tag<Fluid> WATER_BREATHABLE = TagRegistry.fluid(AstromineCommon.identifier("water_breathable"));
	public static final Tag<Fluid> LAVA_BREATHABLE = TagRegistry.fluid(AstromineCommon.identifier("lava_breathable"));

	public static final Tag<EntityType<?>> DOES_NOT_BREATHE = TagRegistry.entityType(AstromineCommon.identifier("does_not_breathe"));
}
