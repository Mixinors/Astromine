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

import com.github.mixinors.astromine.common.config.entry.tiered.AlloySmelterConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SpeedyFluidStorageMachineConfig;

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
	public FluidStorageMachineConfig fluidGenerator = new SpeedyFluidStorageMachineConfig();

	@Comment("Settings for the Fluid Mixer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig fluidMixer = new SpeedyFluidStorageMachineConfig();

	@Comment("Settings for the Electrolyzer")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig electrolyzer = new SpeedyFluidStorageMachineConfig();

	@Comment("Settings for the Refinery")
	@ConfigEntry.Gui.CollapsibleObject
	public FluidStorageMachineConfig refinery = new SpeedyFluidStorageMachineConfig();

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
	public FluidStorageMachineConfig solidifier = new SpeedyFluidStorageMachineConfig();
}
