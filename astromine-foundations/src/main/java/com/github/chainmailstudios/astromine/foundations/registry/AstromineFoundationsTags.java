package com.github.chainmailstudios.astromine.foundations.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class AstromineFoundationsTags {
	public static final Tag<Fluid> OXYGEN = TagRegistry.fluid(new Identifier("c:oxygen"));
	public static final Tag<Fluid> HYDROGEN = TagRegistry.fluid(new Identifier("c:hydrogen"));
}
