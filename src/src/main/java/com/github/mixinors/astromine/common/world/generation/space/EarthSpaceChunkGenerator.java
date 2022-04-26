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

package com.github.mixinors.astromine.common.world.generation.space;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.noise.OctaveNoiseSampler;
import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
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
import net.minecraft.world.gen.random.RandomSeed;

public class EarthSpaceChunkGenerator extends ChunkGenerator {
	public static final Codec<EarthSpaceChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed), RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry)).apply(instance,
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

	private static final MultiNoiseUtil.NoiseValuePoint POINT = new MultiNoiseUtil.NoiseValuePoint(
			0, // space is cold
			0, // there is no humidity in space
			0, // nor are there continents
			0, // or erosion for that matter
			0, // and there's certainly no depth
			0 // or weirdness, well, maybe a little
	);
	@Override
	public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
		return (i, j, k) -> POINT;
	}

	@Override
	public void carve(ChunkRegion chunkRegion,
			long seed,
			BiomeAccess biomeAccess,
			StructureAccessor structureAccessor,
			Chunk chunk,
			GenerationStep.Carver generationStep) {
		// hm yes today I will carve space
		// yes yes hyperspace lanes in astromine when
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
		return CompletableFuture.supplyAsync(() -> {
			populateNoise(structureAccessor, chunk);
			return Unit.INSTANCE;
		}, executor).thenApply(unit -> chunk);
	}

	@Override
	public int getSeaLevel() {
		return 0; // there is no ocean in space, or maybe all of space is an ocean, after all we travel in space with spaceSHIPs :tiny_potato:
	}

	@Override
	public int getMinimumY() {
		return 0;
	}

	public void populateNoise(StructureAccessor accessor, Chunk chunk) {
		var mutable = new BlockPos.Mutable();
		var x1 = chunk.getPos().getStartX();
		var z1 = chunk.getPos().getStartZ();
		var y1 = 0;
		
		var x2 = chunk.getPos().getEndX();
		var z2 = chunk.getPos().getEndZ();
		var y2 = 256;
		
		var random = new ChunkRandom(new AtomicSimpleRandom(RandomSeed.getSeed()));
		random.setPopulationSeed(this.seed, x1, z1);

		for (var x = x1; x <= x2; ++x) {
			for (var z = z1; z <= z2; ++z) {
				for (var y = y1; y <= y2; ++y) {
					var noise = this.noise.sample(x, y, z);
					noise -= computeNoiseFalloff(y);

					if (noise > AMConfig.get().world.asteroidGenerationThreshold) {
						if (random.nextInt(64) != 0) {
							chunk.setBlockState(mutable.set(x, y, z), AMBlocks.ASTEROID_STONE.get().getDefaultState(), false);
						}
					}
				}
			}
		}
	}

	// Desmos: \frac{10}{x+1}-\frac{10}{x-257}-0.155
	// It should actually be 10/y - 10/(y - 256) but i don't want to divide by 0 today
	private double computeNoiseFalloff(int y) {
		return (10.0 / (y + 1.0)) - (10.0 / (y - 257.0)) - 0.155;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
		return 512;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		var states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(world.getBottomY(), states);
	}
}
