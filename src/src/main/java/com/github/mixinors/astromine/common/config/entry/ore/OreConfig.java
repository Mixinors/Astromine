package com.github.mixinors.astromine.common.config.entry.ore;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class OreConfig implements AMConfigEntry {
	@Comment("Minimum range of this ore's weight")
	public int minRange;
	@Comment("Maximum range of this ore's weight")
	public int maxRange;
	
	@Comment("Minimum size of this ore's veins")
	public int minSize;
	@Comment("Maximum size of this ore's veins")
	public int maxSize;
	
	public OreConfig(int minRange, int maxRange, int minSize, int maxSize) {
		this.minRange = minRange;
		this.maxRange = maxRange;
		
		this.minSize = minSize;
		this.maxSize = maxSize;
	}
}
