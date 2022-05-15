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

import com.github.mixinors.astromine.common.config.entry.ore.OreConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class OresConfigSection {
	@Comment("Settings for Asteroid Coal Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidCoalOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Iron Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidIronOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Gold Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidGoldOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Copper Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidCopperOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Tin Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidTinOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Silver Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidSilverOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Lead Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidLeadOre = new OreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Redstone Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidRedstoneOre = new OreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Lapis Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidLapisOre = new OreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Diamond Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidDiamondOre = new OreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Emerald Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidEmeraldOre = new OreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Metite Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidMetiteOre = new OreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Asterite Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidAsteriteOre = new OreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Stellum Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidStellumOre = new OreConfig(0, 30, 8, 48);
	
	@Comment("Settings for Asteroid Galaxium Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public OreConfig asteroidGalaxiumOre = new OreConfig(0, 20, 8, 48);
	
	@Comment("Whether generation of Overworld Tin Ore is enabled or not")
	public boolean overworldTinOre = true;
	
	@Comment("Whether generation of Overworld Silver Ore is enabled or not")
	public boolean overworldSilverOre = true;
	
	@Comment("Whether generation of Overworld Lead Ore is enabled or not")
	public boolean overworldLeadOre = true;
}
