package com.github.mixinors.astromine.common.config.entry.network;

import com.github.mixinors.astromine.common.config.entry.AMConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class NetworkConfig implements AMConfigEntry {
	@Comment("Transfer rate of this network")
	public int transferRate;
	
	public NetworkConfig(int transferRate) {
		this.transferRate = transferRate;
	}
}
