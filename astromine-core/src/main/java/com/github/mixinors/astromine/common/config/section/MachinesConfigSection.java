package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.entry.tiered.AlloySmelterConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class MachinesConfigSection {
	@Comment("Settings for the Triturator")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineConfig triturator = new SimpleMachineConfig();

	@Comment("Settings for the Solid Generator")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineConfig solidGenerator = new SimpleMachineConfig();

	@Comment("Settings for the Press")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineConfig press = new SimpleMachineConfig();

	@Comment("Settings for the Wire Mill")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineConfig wireMill = new SimpleMachineConfig();

	@Comment("Settings for the Fluid Generator")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig fluidGenerator = new FluidStorageMachineConfig();

	@Comment("Settings for the Fluid Mixer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig fluidMixer = new FluidStorageMachineConfig();

	@Comment("Settings for the Electrolyzer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig electrolyzer = new FluidStorageMachineConfig();

	@Comment("Settings for the Refinery")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig refinery = new FluidStorageMachineConfig();

	@Comment("Settings for the Electric Furnace")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineConfig electricFurnace = new SimpleMachineConfig();

	@Comment("Settings for the Alloy Smelter")
	@ConfigEntry.Gui.CollapsibleObject
	public AlloySmelterConfig alloySmelter = new AlloySmelterConfig();

	@Comment("Settings for the Melter")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig melter = new FluidStorageMachineConfig();

	@Comment("Settings for the Solidifier")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig solidifier = new FluidStorageMachineConfig();
}
