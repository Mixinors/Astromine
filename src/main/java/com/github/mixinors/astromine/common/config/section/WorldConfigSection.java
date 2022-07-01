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

import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class WorldConfigSection {
	@Comment("Settings for the layers")
	@ConfigEntry.Gui.CollapsibleObject
	public LayersConfigSection layers = new LayersConfigSection();
	
	@Comment("Settings for Ores")
	@ConfigEntry.Gui.CollapsibleObject
	public OresConfigSection ores = new OresConfigSection();
	
	@Comment("Gravity level in Space")
	public float spaceGravity = 0.01F;
	
	@Comment("Gravity level in Space")
	public float moonGravity = 0.015F;
	
	@Comment("Whether generation of Overworld Crude Oil Wells is enabled or not")
	public boolean crudeOilWellsGeneration = true;
	
	@Comment("Whether generation of Overworld Meteors is enabled or not")
	public boolean meteorGeneration = true;
	
	@Comment("Threshold for Asteroid Ore generation")
	public int asteroidOreGenerationThreshold = 2;
	
	@Comment("Threshold for Asteroid generation")
	public float asteroidGenerationThreshold = 0.545F;
	
	@Comment("Threshold for Crude Oil Well generation")
	public int crudeOilWellGenerationThreshold = 2000;
}
