/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
