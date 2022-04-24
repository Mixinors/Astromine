package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.entry.tiered.CapacitorConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class BlocksConfigSection {
	@Comment("Settings for Machines")
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public MachinesConfigSection machines = new MachinesConfigSection();
	
	@Comment("Settings for Utilities")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilitiesConfigSection utilities = new UtilitiesConfigSection();
}
