package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.entry.layer.LayerConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class LayersConfigSection {
	@Comment("Settings for the Overworld layer")
	@ConfigEntry.Gui.CollapsibleObject
	public LayerConfig overworld = new LayerConfig(-58, 992);
	
	@Comment("Settings for the Space layer")
	@ConfigEntry.Gui.CollapsibleObject
	public LayerConfig space = new LayerConfig(1024, 32);
}
