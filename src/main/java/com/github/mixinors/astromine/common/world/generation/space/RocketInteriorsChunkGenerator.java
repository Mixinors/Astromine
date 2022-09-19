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
import com.github.mixinors.astromine.mixin.common.StructureAccessorAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RocketInteriorsChunkGenerator extends ChunkGenerator {
	public static final Codec<RocketInteriorsChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> {
		return createStructureSetRegistryGetter(instance).and(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource)
		).apply(instance, RocketInteriorsChunkGenerator::new);
	});
	
	public RocketInteriorsChunkGenerator(Registry<StructureSet> structureFeatureRegistry, BiomeSource biomeSource) {
		super(structureFeatureRegistry, Optional.empty(), biomeSource);
	}
	
	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}
	
	@Override
	public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
	
	}
	
	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
	
	}
	
	@Override
	public void populateEntities(ChunkRegion region) {
		
	}
	
	@Override
	public int getWorldHeight() {
		return 96;
	}
	
	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
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
		if (chunk.getPos().x % 32 == 0 && chunk.getPos().z % 32 == 0) {
			var world = ((StructureAccessorAccessor) accessor).getWorld();
			var server = world.getServer();
			if (server == null) return;
			var manager = server.getStructureTemplateManager();
			var structure = manager.getTemplate(AMCommon.id("rocket"));
			
			if (structure.isPresent() && world instanceof ServerWorldAccess access) {
				var structurePlacementData = new StructurePlacementData();
				structure.get().place(access, new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16), new BlockPos(chunk.getPos().x * 16, 0, chunk.getPos().z * 16), structurePlacementData, access.getRandom(), Block.NOTIFY_LISTENERS);
			}
		}
	}
	
	// Desmos: \frac{10}{x+1}-\frac{10}{x-257}-0.155
	// It should actually be 10/y - 10/(y - 256) but i don't want to divide by 0 today
	private double computeNoiseFalloff(int y) {
		return (10.0 / (y + 1.0)) - (10.0 / (y - 257.0)) - 0.155;
	}
	
	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
		return 96;
	}
	
	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
		var states = new BlockState[96];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(world.getBottomY(), states);
	}
	
	@Override
	public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
	
	}
}
