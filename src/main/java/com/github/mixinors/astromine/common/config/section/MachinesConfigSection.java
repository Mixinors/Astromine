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
import com.github.mixinors.astromine.common.config.entry.tiered.ElectricFurnaceConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.FluidStorageMachineConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SimpleMachineConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SolidGeneratorConfig;
import com.github.mixinors.astromine.common.config.entry.tiered.SpeedyFluidStorageMachineConfig;

public class MachinesConfigSection {
	public SimpleMachineConfig triturator = new SimpleMachineConfig();
	public SolidGeneratorConfig solidGenerator = new SolidGeneratorConfig();
	public SimpleMachineConfig press = new SimpleMachineConfig();
	public SimpleMachineConfig wireMill = new SimpleMachineConfig();
	public FluidStorageMachineConfig fluidGenerator = new SpeedyFluidStorageMachineConfig();
	public FluidStorageMachineConfig fluidMixer = new SpeedyFluidStorageMachineConfig();
	public FluidStorageMachineConfig electrolyzer = new SpeedyFluidStorageMachineConfig();
	public FluidStorageMachineConfig refinery = new SpeedyFluidStorageMachineConfig();
	public ElectricFurnaceConfig electricFurnace = new ElectricFurnaceConfig();
	public AlloySmelterConfig alloySmelter = new AlloySmelterConfig();
	public FluidStorageMachineConfig melter = new FluidStorageMachineConfig();
	public FluidStorageMachineConfig solidifier = new SpeedyFluidStorageMachineConfig();
}
