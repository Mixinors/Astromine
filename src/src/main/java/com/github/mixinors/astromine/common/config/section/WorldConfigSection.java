package com.github.mixinors.astromine.common.config.section;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class WorldConfigSection {
	@Comment("Settings for the layers")
	@ConfigEntry.Gui.CollapsibleObject
	public LayersConfigSection layers = new LayersConfigSection();
	
	@Comment("Settings for Ores")
	@ConfigEntry.Gui.CollapsibleObject
	public OresConfigSection ores = new OresConfigSection();
	
	@Comment("Gravity level in Space")
	public float spaceGravity = 0.01F;
	
	@Comment("Gravity for non-Astromine dimensions")
	public double gravity = 0.08D;
	
	@Comment("Whether generation of Overworld Crude Oil Wells is enabled or not")
	public boolean crudeOilWellsGeneration = true;
	
	@Comment("Whether generation of Overworld Meteors is enabled or not")
	public boolean meteorGeneration = true;
	
	@Comment("Threshold for Asteroid Ore generation")
	public int asteroidOreGenerationThreshold = 2;
	
	@Comment("Threshold for Asteroid generation")
	public float asteroidGenerationThreshold = 0.545F;
	
	@Comment("Threshold for Crude Oil Well generation")
	public int crudeOilWellGenerationThreshold = 2000;
}
