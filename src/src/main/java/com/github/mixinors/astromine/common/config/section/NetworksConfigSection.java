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

import com.github.mixinors.astromine.common.config.entry.network.NetworkConfig;
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
