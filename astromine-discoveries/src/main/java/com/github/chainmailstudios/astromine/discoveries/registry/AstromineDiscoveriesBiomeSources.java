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

package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.world.generation.glacios.GlaciosBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceBiomeSource;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanBiomeSource;
import com.github.chainmailstudios.astromine.registry.AstromineBiomeSources;
import net.minecraft.core.Registry;

public class AstromineDiscoveriesBiomeSources extends AstromineBiomeSources {
	public static void initialize() {
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.EARTH_SPACE_ID, EarthSpaceBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.MOON_ID, MoonBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.MARS_ID, MarsBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.VULCAN_ID, VulcanBiomeSource.CODEC);
		Registry.register(Registry.BIOME_SOURCE, AstromineDiscoveriesDimensions.GLACIOS_ID, GlaciosBiomeSource.CODEC);
	}
}
