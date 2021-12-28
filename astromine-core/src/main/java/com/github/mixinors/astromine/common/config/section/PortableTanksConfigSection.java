package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class PortableTanksConfigSection {
	@Comment("Fluid for the Portable Tank.")
	public long regular = DefaultConfigValues.PORTABLE_TANK_FLUID;

	@Comment("Fluid for the Large Portable Tank.")
	public long large = DefaultConfigValues.LARGE_PORTABLE_TANK_FLUID;
}
