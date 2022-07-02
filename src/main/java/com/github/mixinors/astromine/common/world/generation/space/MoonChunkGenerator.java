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

import com.github.mixinors.astromine.common.noise.OctaveNoiseSampler;
import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Unit;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomSeed;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MoonChunkGenerator extends ChunkGenerator {
	public static final Codec<MoonChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
			MoonChunkGenerator.method_41042(instance).and(
					instance.group(
							Codec.LONG.fieldOf("seed").forGetter(gen -> gen.field_37261),
							RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry)
					)).apply(instance, MoonChunkGenerator::new));
	
	private final Registry<Biome> biomeRegistry;
	
	private final OctaveNoiseSampler<OpenSimplexNoise> noise;
	
	private static final int MIN_Y = -64;
	private static final int MAX_Y = 64;
	
	public MoonChunkGenerator(Registry<StructureSet> structureFeatureRegistry, long seed, Registry<Biome> biomeRegistry) {
		super(structureFeatureRegistry, Optional.empty(), new MoonBiomeSource(biomeRegistry, seed));
		
		this.field_37261 = seed;
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
		return MultiNoiseUtil.method_40443();
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
		return new MoonChunkGenerator(field_37053, seed, biomeRegistry);
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
		return MIN_Y;
	}
	
	public void populateNoise(StructureAccessor accessor, Chunk chunk) {
		var mutable = new BlockPos.Mutable();
		
		var x1 = chunk.getPos().getStartX();
		var z1 = chunk.getPos().getStartZ();
		
		var x2 = chunk.getPos().getEndX();
		var z2 = chunk.getPos().getEndZ();
		
		var random = new ChunkRandom(new AtomicSimpleRandom(RandomSeed.getSeed()));
		random.setPopulationSeed(this.field_37261, x1, z1);
		
		// Populate with Moon Stone.
		for (var x = x1; x <= x2; ++x) {
			for (var z = z1; z <= z2; ++z) {
				var maxY = MAX_Y - (int) ((noise.sample(x, 0, z)) * 16);
				
				var biome = chunk.getBiomeForNoiseGen(BiomeCoords.fromBlock(x), 0, BiomeCoords.fromBlock(z)).getKey().orElseThrow();
				
				for (var y = MIN_Y; y < maxY; ++y) {
					if (biome.equals(AMBiomes.MOON_DARK_SIDE_KEY)) {
						chunk.setBlockState(mutable.set(x, y, z), AMBlocks.DARK_MOON_STONE.get().getDefaultState(), false);
					} else {
						chunk.setBlockState(mutable.set(x, y, z), AMBlocks.MOON_STONE.get().getDefaultState(), false);
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
	
	@Override
	public void getDebugHudText(List<String> text, BlockPos pos) {
	}
}
