package com.github.chainmailstudios.astromine.common.miscellaneous;

import java.util.Arrays;

import it.unimi.dsi.fastutil.HashCommon;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public class BiomeGenCache {
	private static final int CACHE_SIZE = 65536;

	private final int mask;
	private final long[] keys;
	private final Biome[] values;
	private final BiomeSource source;

	public BiomeGenCache(BiomeSource source) {
		this.source = source;
		this.mask = CACHE_SIZE - 1;

		// Initialize default values
		this.keys = new long[CACHE_SIZE];
		Arrays.fill(this.keys, Long.MIN_VALUE);
		this.values = new Biome[CACHE_SIZE];
	}

	public Biome getBiome(int x, int z) {
		long key = ChunkPos.toLong(x, z);
		int idx = (int) HashCommon.mix(key) & this.mask;

		if (this.keys[idx] == key) {
			return this.values[idx];
		}

		Biome biome = source.getBiomeForNoiseGen(x, 0, z);

		this.keys[idx] = key;
		this.values[idx] = biome;

		return biome;
	}
}
