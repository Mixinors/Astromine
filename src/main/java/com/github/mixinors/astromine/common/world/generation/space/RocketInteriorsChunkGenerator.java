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

import com.github.mixinors.astromine.AMCommon;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RocketInteriorsChunkGenerator extends ChunkGenerator {
	public static final MapCodec<RocketInteriorsChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
	).apply(instance, RocketInteriorsChunkGenerator::new));
	
	public RocketInteriorsChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource);
	}
	
	public RocketInteriorsChunkGenerator(Registry<StructureSet> structureFeatureRegistry, BiomeSource biomeSource) {
		this(biomeSource);
	}
	
	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}
	
	@Override
	public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk, GenerationStep.Carving carverStep) {
	
	}
	
	@Override
	public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {
		populateNoise(region, chunk);
	}
	
	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {
		
	}
	
	@Override
	public int getGenDepth() {
		return 96;
	}
	
	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk) {
		return CompletableFuture.completedFuture(chunk);
	}
	
	@Override
	public int getSeaLevel() {
		return 0;
	}
	
	@Override
	public int getMinY() {
		return 0;
	}
	
	public void populateNoise(ServerLevelAccessor access, ChunkAccess chunk) {
		if (chunk.getPos().x % 32 == 0 && chunk.getPos().z % 32 == 0) {
			var server = access.getServer();
			if (server == null) return;
			var manager = server.getStructureManager();
			var structure = manager.get(AMCommon.id("rocket"));
			
			if (structure.isPresent()) {
				var structurePlacementData = new StructurePlaceSettings();
				structure.get().placeInWorld(access, new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16), new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16), structurePlacementData, access.getRandom(), Block.UPDATE_CLIENTS);
			}
		}
	}
	
	// Desmos: \frac{10}{x+1}-\frac{10}{x-257}-0.155
	// It should actually be 10/y - 10/(y - 256) but i don't want to divide by 0 today
	private double computeNoiseFalloff(int y) {
		return (10.0 / (y + 1.0)) - (10.0 / (y - 257.0)) - 0.155;
	}
	
	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor world, RandomState noiseConfig) {
		return 96;
	}
	
	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor world, RandomState noiseConfig) {
		var states = new BlockState[96];
		Arrays.fill(states, Blocks.AIR.defaultBlockState());
		return new NoiseColumn(world.getMinBuildHeight(), states);
	}
	
	@Override
	public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos) {
	
	}
}
