package com.github.mixinors.astromine.common.config.section;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class ItemsConfigSection {
	@Comment("Settings for Batteries")
	@ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
	public BatteriesConfigSection batteries = new BatteriesConfigSection();
	
	@Comment("Settings for Portable Tanks")
	@ConfigEntry.Gui.CollapsibleObject
	public PortableTanksConfigSection portableTanks = new PortableTanksConfigSection();
	
	@Comment("Energy for the Gravity Gauntlet")
	public long gravityGauntletEnergy = batteries.singleBatteries.advanced;
	
	@Comment("Energy for the Gravity Gauntlet actions")
	public long gravityGauntletConsumed = 512L;
	
	@Comment("Energy for the Drill actions")
	public long drillConsumed = 6L;
	
	@Comment("Multiplier for the Drill entity hit actions")
	public double drillEntityHitMultiplier = 2.0D;
	
	@Comment("Delay for the Fire Extinguisher sneaking actions")
	public int fireExtinguisherSneakingDelay = 2;
	
	@Comment("Delay for the Fire Extinguisher standing actions")
	public int fireExtinguisherStandingDelay = 10;
	
	@Comment("Energy for the Primitive Drill")
	public long primitiveDrillEnergy = batteries.batteryPacks.primitive * 2;
	
	@Comment("Energy for the Basic Drill")
	public long basicDrillEnergy = batteries.batteryPacks.basic * 2;
	
	@Comment("Energy for the Advanced Drill")
	public long advancedDrillEnergy = batteries.batteryPacks.advanced * 2;
	
	@Comment("Energy for the Elite Drill")
	public long eliteDrillEnergy = batteries.batteryPacks.elite * 2;
}
