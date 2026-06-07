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

public class OresConfigSection {
	public MoonOreConfig moonLunumOre = new MoonOreConfig(12);
	public MoonOreConfig moonIronOre = new MoonOreConfig(6);
	public MoonOreConfig moonRedstoneOre = new MoonOreConfig(6);
	public MoonOreConfig moonLapisOre = new MoonOreConfig(6);
	public DarkMoonOreConfig darkMoonLunumOre = new DarkMoonOreConfig(12);
	public DarkMoonOreConfig darkMoonIronOre = new DarkMoonOreConfig(6);
	public DarkMoonOreConfig darkMoonRedstoneOre = new DarkMoonOreConfig(6);
	public DarkMoonOreConfig darkMoonLapisOre = new DarkMoonOreConfig(6);
	public AsteroidOreConfig asteroidCoalOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidIronOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidGoldOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidCopperOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidTinOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidSilverOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidLeadOre = new AsteroidOreConfig(0, 100, 8, 48);
	public AsteroidOreConfig asteroidRedstoneOre = new AsteroidOreConfig(0, 40, 8, 48);
	public AsteroidOreConfig asteroidLapisOre = new AsteroidOreConfig(0, 40, 8, 48);
	public AsteroidOreConfig asteroidDiamondOre = new AsteroidOreConfig(0, 50, 8, 48);
	public AsteroidOreConfig asteroidEmeraldOre = new AsteroidOreConfig(0, 50, 8, 48);
	public AsteroidOreConfig asteroidMetiteOre = new AsteroidOreConfig(0, 50, 8, 48);
	public AsteroidOreConfig asteroidAsteriteOre = new AsteroidOreConfig(0, 40, 8, 48);
	public AsteroidOreConfig asteroidStellumOre = new AsteroidOreConfig(0, 30, 8, 48);
	public AsteroidOreConfig asteroidGalaxiumOre = new AsteroidOreConfig(0, 20, 8, 48);
	public boolean overworldTinOre = true;
	public boolean overworldSilverOre = true;
	public boolean overworldLeadOre = true;
}
