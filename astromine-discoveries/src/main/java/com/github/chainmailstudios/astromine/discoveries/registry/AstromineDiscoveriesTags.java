package com.github.chainmailstudios.astromine.discoveries.registry;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineTags;

public class AstromineDiscoveriesTags extends AstromineTags {
	public static final Tag<Fluid> ROCKET_FUELS = TagRegistry.fluid(AstromineCommon.identifier("rocket_fuels"));
}
