/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.world.generation.space;

import com.github.mixinors.astromine.registry.common.AMBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.mixinors.astromine.common.noise.OctaveNoiseSampler;
import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMConfig;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EarthSpaceChunkGenerator extends ChunkGenerator {
	public static Codec<EarthSpaceChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed), RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry)).apply(instance,
		EarthSpaceChunkGenerator::new));

	private final long seed;
	private final Registry<Biome> biomeRegistry;

	private final OctaveNoiseSampler<OpenSimplexNoise> noise;

	public EarthSpaceChunkGenerator(long seed, Registry<Biome> biomeRegistry) {
		super(new EarthSpaceBiomeSource(biomeRegistry, seed), new StructuresConfig(false));
		this.seed = seed;
		this.biomeRegistry = biomeRegistry;
		this.noise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, new Random(seed), 3, 200, 1.225, 1);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return withSeedCommon(seed);
	}

	@Override
	public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
		return (i, j, k) -> MultiNoiseUtil.createNoiseValuePoint(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver generationStep) {

	}

	public ChunkGenerator withSeedCommon(long seed) {
		return new EarthSpaceChunkGenerator(seed, biomeRegistry);
	}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {

	}

	@Override
	public void populateEntities(ChunkRegion region) {

	}

	@Override
	public int getWorldHeight() {
		return 512;
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int x1 = chunk.getPos().getStartX();
		int z1 = chunk.getPos().getStartZ();
		int y1 = 0;

		int x2 = chunk.getPos().getEndX();
		int z2 = chunk.getPos().getEndZ();
		int y2 = 256;

		ChunkRandom random = new ChunkRandom(new AtomicSimpleRandom(0L));
		random.setPopulationSeed(this.seed, x1, z1);

		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
				for (int y = y1; y <= y2; ++y) {
					double noise = this.noise.sample(x, y, z);
					noise -= computeNoiseFalloff(y);

					if (noise > AMConfig.get().asteroidNoiseThreshold) {
						if (random.nextInt(64) != 0) {
							chunk.setBlockState(mutable.set(x, y, z), AMBlocks.ASTEROID_STONE.get().getDefaultState(), false);
						}
					}
				}
			}
		}
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinimumY() {
		return 512;
	}

	// Desmos: \frac{10}{x+1}-\frac{10}{x-257}-0.155
	// It should actually be 10/y - 10/(y - 256) but i don't want to divide by 0 today
	private double computeNoiseFalloff(int y) {
		return (10.0 / (y + 1.0)) - (10.0 / (y - 257.0)) - 0.155;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
		return 0;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		BlockState[] states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(world.getBottomY(), states);
	}
}
