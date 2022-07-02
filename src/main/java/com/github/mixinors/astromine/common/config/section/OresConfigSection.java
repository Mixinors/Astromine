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

import com.github.mixinors.astromine.common.config.entry.ore.AsteroidOreConfig;
import com.github.mixinors.astromine.common.config.entry.ore.DarkMoonOreConfig;
import com.github.mixinors.astromine.common.config.entry.ore.MoonOreConfig;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class OresConfigSection {
	@Comment("Settings for Moon Lunum Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public MoonOreConfig moonLunumOre = new MoonOreConfig(12);
	
	@Comment("Settings for Moon Iron Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public MoonOreConfig moonIronOre = new MoonOreConfig(6);
	
	@Comment("Settings for Moon Redstone Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public MoonOreConfig moonRedstoneOre = new MoonOreConfig(6);
	
	@Comment("Settings for Dark MoonLunum Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public DarkMoonOreConfig darkMoonLunumOre = new DarkMoonOreConfig(12);
	
	@Comment("Settings for Dark MoonIron Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public DarkMoonOreConfig darkMoonIronOre = new DarkMoonOreConfig(6);
	
	@Comment("Settings for Dark MoonRedstone Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public DarkMoonOreConfig darkMoonRedstoneOre = new DarkMoonOreConfig(6);
	
	@Comment("Settings for Asteroid Coal Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidCoalOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Iron Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidIronOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Gold Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidGoldOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Copper Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidCopperOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Tin Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidTinOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Silver Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidSilverOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Lead Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidLeadOre = new AsteroidOreConfig(0, 100, 8, 48);
	
	@Comment("Settings for Asteroid Redstone Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidRedstoneOre = new AsteroidOreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Lapis Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidLapisOre = new AsteroidOreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Diamond Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidDiamondOre = new AsteroidOreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Emerald Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidEmeraldOre = new AsteroidOreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Metite Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidMetiteOre = new AsteroidOreConfig(0, 50, 8, 48);
	
	@Comment("Settings for Asteroid Asterite Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidAsteriteOre = new AsteroidOreConfig(0, 40, 8, 48);
	
	@Comment("Settings for Asteroid Stellum Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidStellumOre = new AsteroidOreConfig(0, 30, 8, 48);
	
	@Comment("Settings for Asteroid Galaxium Ore")
	@ConfigEntry.Gui.CollapsibleObject
	public AsteroidOreConfig asteroidGalaxiumOre = new AsteroidOreConfig(0, 20, 8, 48);
	
	@Comment("Whether generation of Overworld Tin Ore is enabled or not")
	public boolean overworldTinOre = true;
	
	@Comment("Whether generation of Overworld Silver Ore is enabled or not")
	public boolean overworldSilverOre = true;
	
	@Comment("Whether generation of Overworld Lead Ore is enabled or not")
	public boolean overworldLeadOre = true;
}
