package com.github.mixinors.astromine.common.config.entry.layer;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class LayerConfig implements AMConfigEntry {
	@Comment("Y level to travel to this layer")
	public int travelY;
	
	@Comment("Y level to spawn at in this layer")
	public int spawnY;
	
	public LayerConfig(int travelY, int spawnY) {
		this.travelY = travelY;
		this.spawnY = spawnY;
	}
}
