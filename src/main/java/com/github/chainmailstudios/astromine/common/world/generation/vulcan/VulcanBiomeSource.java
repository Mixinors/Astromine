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

package com.github.chainmailstudios.astromine.common.world.generation.vulcan;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class VulcanBiomeSource extends BiomeSource {
	public static Codec<VulcanBiomeSource> CODEC = Codec.LONG.fieldOf("seed").xmap(VulcanBiomeSource::new, (source) -> source.seed).stable().codec();
	private final long seed;

	public VulcanBiomeSource(long seed) {
		super(ImmutableList.of());
		this.seed = seed;
	}

	@Override
	protected Codec<? extends BiomeSource> method_28442() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return new VulcanBiomeSource(seed);
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		return AstromineBiomes.VULCAN;
	}
}
