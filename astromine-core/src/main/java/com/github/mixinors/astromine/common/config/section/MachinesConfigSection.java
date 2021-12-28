package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.tiered.FluidStorageMachineTieredConfig;
import com.github.mixinors.astromine.common.config.tiered.SimpleMachineTieredConfig;
import com.github.mixinors.astromine.common.util.tier.MachineTier;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class MachinesConfigSection {
	@Comment("Settings for the Triturator")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig triturator = new SimpleMachineTieredConfig();

	@Comment("Settings for the Solid Generator")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig solidGenerator = new SimpleMachineTieredConfig();

	@Comment("Settings for the Press")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig press = new SimpleMachineTieredConfig();

	@Comment("Settings for the Wire Mill")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig wireMill = new SimpleMachineTieredConfig();

	@Comment("Settings for the Fluid Generator")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig fluidGenerator = new FluidStorageMachineTieredConfig();

	@Comment("Settings for the Fluid Mixer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig fluidMixer = new FluidStorageMachineTieredConfig();

	@Comment("Settings for the Electrolyzer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig electrolyzer = new FluidStorageMachineTieredConfig();

	@Comment("Settings for the Refinery")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig refinery = new FluidStorageMachineTieredConfig();

	@Comment("Settings for the Electric Furnace")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig electricFurnace = new SimpleMachineTieredConfig();

	@Comment("Settings for the Alloy Smelter")
	@ConfigEntry.Gui.CollapsibleObject
	public SimpleMachineTieredConfig alloySmelter = new SimpleMachineTieredConfig() {
		@Override
		public long getDefaultEnergyStorage(MachineTier tier) {
			return super.getDefaultEnergyStorage(tier) + (electricFurnace.getEnergyStorage(tier) * 2);
		}
	};

	@Comment("Settings for the Melter")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig melter = new FluidStorageMachineTieredConfig();

	@Comment("Settings for the Solidifier")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineTieredConfig solidifier = new FluidStorageMachineTieredConfig();
}
