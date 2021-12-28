package com.github.mixinors.astromine.common.config.section;

import com.github.mixinors.astromine.common.config.DefaultConfigValues;

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class BatteriesConfigSection {
	@Comment("Energy for single batteries")
	@ConfigEntry.Gui.CollapsibleObject
	public SingleBatteriesConfigSection singleBatteries = new SingleBatteriesConfigSection();

	@Comment("Energy for battery packs")
	@ConfigEntry.Gui.CollapsibleObject
	public BatteryPacksConfigSection batteryPacks = new BatteryPacksConfigSection();

	public static class SingleBatteriesConfigSection {
		@Comment("Energy for the Primitive Battery.")
		public long primitive = DefaultConfigValues.PRIMITIVE_BATTERY_ENERGY;

		@Comment("Energy for the Basic Battery.")
		public long basic = DefaultConfigValues.BASIC_BATTERY_ENERGY;

		@Comment("Energy for the Advanced Battery.")
		public long advanced = DefaultConfigValues.ADVANCED_BATTERY_ENERGY;

		@Comment("Energy for the Elite Battery.")
		public long elite = DefaultConfigValues.ELITE_BATTERY_ENERGY;
	}

	public static class BatteryPacksConfigSection {
		@Comment("Energy for the Primitive Battery Pack.")
		public long primitive = DefaultConfigValues.PRIMITIVE_BATTERY_PACK_ENERGY;

		@Comment("Energy for the Basic Battery Pack.")
		public long basic = DefaultConfigValues.BASIC_BATTERY_PACK_ENERGY;

		@Comment("Energy for the Advanced Battery Pack.")
		public long advanced = DefaultConfigValues.ADVANCED_BATTERY_PACK_ENERGY;

		@Comment("Energy for the Elite Battery Pack.")
		public long elite = DefaultConfigValues.ELITE_BATTERY_PACK_ENERGY;
	}
}
