/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.biome.EarthSpaceBiome;
import com.github.chainmailstudios.astromine.common.world.biome.MarsBiome;
import com.github.chainmailstudios.astromine.common.world.biome.MoonBiome;
import com.github.chainmailstudios.astromine.common.world.generation.space.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.mars.MarsBiomeSource;
import com.github.chainmailstudios.astromine.common.world.generation.moon.MoonBiomeSource;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class AstromineBiomes {
	public static Biome ASTEROID_BELT;
	public static Biome MOON_FLATS;
	public static Biome MOON_HILLS;
	public static Biome MOON_LOWLANDS;
	public static Biome MARS;
	public static Biome MARS_RIVERBED;

	public static void initialize() {
		// Biome Sources
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("earth_space"), EarthSpaceBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("moon"), MoonBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineCommon.identifier("mars"), MarsBiomeSource.CODEC);

		// Biomes
		ASTEROID_BELT = Registry.register(Registry.BIOME, AstromineCommon.identifier("asteroid_belt"), new EarthSpaceBiome());

		MOON_FLATS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_flats"), new MoonBiome(100, 20));
		MOON_HILLS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_hills"), new MoonBiome(105, 30));
		MOON_LOWLANDS = Registry.register(Registry.BIOME, AstromineCommon.identifier("moon_lowlands"), new MoonBiome(93, 9));

		MARS = Registry.register(Registry.BIOME, AstromineCommon.identifier("mars"), new MarsBiome(100, 1));
		MARS_RIVERBED = Registry.register(Registry.BIOME, AstromineCommon.identifier("mars_riverbed"), new MarsBiome(60, 0.1f));
	}
}
