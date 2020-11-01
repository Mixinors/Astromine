package com.github.chainmailstudios.astromine.foundations.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.foundations.AstromineFoundationsCommon;

public class AstromineFoundationsTags {
	public static final Tag<Fluid> OXYGEN = TagRegistry.fluid(AstromineFoundationsCommon.identifier("oxygen"));
	public static final Tag<Fluid> HYDROGEN = TagRegistry.fluid(AstromineFoundationsCommon.identifier("hydrogen"));
}
