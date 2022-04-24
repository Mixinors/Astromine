package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.entry.network.NetworkConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class NetworksConfigSection {
	@Comment("Settings for Primitive Energy Network")
	public NetworkConfig primitiveEnergyNetwork = new NetworkConfig(256);
	
	@Comment("Settings for Basic Energy Network")
	public NetworkConfig basicEnergyNetwork = new NetworkConfig(512);
	
	@Comment("Settings for Advanced Energy Network")
	public NetworkConfig advancedEnergyNetwork = new NetworkConfig(1024);
	
	@Comment("Settings for Elite Energy Network")
	public NetworkConfig eliteEnergyNetwork = new NetworkConfig(4096);
	
	@Comment("Settings for the Fluid Network")
	public NetworkConfig fluidNetwork = new NetworkConfig((int) (FluidConstants.BUCKET * 4));
	
	@Comment("Settings for the Item Network")
	public NetworkConfig itemNetwork = new NetworkConfig(1);
}
