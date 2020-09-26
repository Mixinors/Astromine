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

import com.github.chainmailstudios.astromine.discoveries.common.world.generation.glacios.GlaciosChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars.MarsChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon.MoonChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.space.EarthSpaceChunkGenerator;
import com.github.chainmailstudios.astromine.discoveries.common.world.generation.vulcan.VulcanChunkGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineChunkGenerators;

public class AstromineDiscoveriesChunkGenerators extends AstromineChunkGenerators {
	public static void initialize() {
		register(AstromineDiscoveriesDimensions.EARTH_SPACE_ID, EarthSpaceChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.MOON_ID, MoonChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.MARS_ID, MarsChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.VULCAN_ID, VulcanChunkGenerator.CODEC);
		register(AstromineDiscoveriesDimensions.GLACIOS_ID, GlaciosChunkGenerator.CODEC);
	}
}
