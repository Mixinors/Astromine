package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.entry.utility.FluidStorageUtilityConfig;
import com.github.mixinors.astromine.common.config.entry.utility.UtilityConfig;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class UtilitiesConfigSection {
	@Comment("Settings for the Block Placer")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilityConfig blockPlacer = new UtilityConfig();

	@Comment("Settings for the Block Breaker")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilityConfig blockBreaker = new UtilityConfig();

	@Comment("Settings for the Fluid Placer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageUtilityConfig fluidPlacer = new FluidStorageUtilityConfig();

	@Comment("Settings for the Fluid Collector")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageUtilityConfig fluidCollector = new FluidStorageUtilityConfig();
}
