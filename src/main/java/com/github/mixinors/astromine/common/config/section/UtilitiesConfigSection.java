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

import com.github.mixinors.astromine.common.config.entry.tiered.CapacitorConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.TankConfig;
import com.github.mixinors.astromine.common.config.entry.utility.FluidStorageUtilityConfig;
import com.github.mixinors.astromine.common.config.entry.utility.UtilityConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class UtilitiesConfigSection {
	@Comment("Settings for Capacitors")
	@ConfigEntry.Gui.CollapsibleObject
	public CapacitorConfig capacitors = new CapacitorConfig();
	
	@Comment("Settings for Tanks")
	@ConfigEntry.Gui.CollapsibleObject
	public TankConfig tanks = new TankConfig();
	
	@Comment("Settings for the Block Placer")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilityConfig blockPlacer = new UtilityConfig();
	
	@Comment("Settings for the Block Breaker")
	@ConfigEntry.Gui.CollapsibleObject
	public UtilityConfig blockBreaker = new UtilityConfig();
	
	@Comment("Settings for the Fluid Placer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageUtilityConfig fluidPlacer = new FluidStorageUtilityConfig();
	
	@Comment("Settings for the Fluid Collector")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageUtilityConfig fluidCollector = new FluidStorageUtilityConfig();
}
