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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.glacios;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.common.noise.FastNoise;
import com.github.chainmailstudios.astromine.common.noise.OctaveNoiseSampler;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class GlaciosChunkGenerator extends ChunkGenerator {
	public static Codec<GlaciosChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry)).apply(instance,
		GlaciosChunkGenerator::new));
	private final long seed;
	private final Registry<Biome> biomeRegistry;

	private final OctaveNoiseSampler<OpenSimplexNoise> floorNoise;
	private final OctaveNoiseSampler<OpenSimplexNoise> ceilingNoise;
	private final FastNoise fastNoise;

	public GlaciosChunkGenerator(long seed, Registry<Biome> biomeRegistry) {
		super(new GlaciosBiomeSource(biomeRegistry, seed), new StructureSettings(false));
		this.seed = seed;
		this.biomeRegistry = biomeRegistry;

		Random random = new Random();

		this.floorNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 3, 194.21, 12, 12);
		this.ceilingNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 2, 402.64, 4, 4);

		this.fastNoise = new FastNoise((int) seed);

		fastNoise.setCellularDistanceFunction(FastNoise.CellularDistanceFunction.EUCLIDEAN);
		fastNoise.setCellularReturnType(FastNoise.CellularReturnType.DISTANCE_TO_DIVISION);
		fastNoise.setFrequency(0.0125F);
	}

	@Override
	protected Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return withSeedCommon(seed);
	}

	public ChunkGenerator withSeedCommon(long seed) {
		return new GlaciosChunkGenerator(seed, biomeRegistry);
	}

	@Override
	public void buildSurfaceAndBedrock(WorldGenRegion region, ChunkAccess chunk) {

	}

	@Override
	public void fillFromNoise(LevelAccessor world, StructureFeatureManager accessor, ChunkAccess chunk) {
		int x1 = chunk.getPos().getMinBlockX();
		int z1 = chunk.getPos().getMinBlockZ();

		int x2 = chunk.getPos().getMaxBlockX();
		int z2 = chunk.getPos().getMaxBlockZ();

		WorldgenRandom chunkRandom = new WorldgenRandom();
		chunkRandom.setBaseChunkSeed(chunk.getPos().x, chunk.getPos().z);

		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		mutable.setY(1);

		for (int x = x1; x <= x2; ++x) {
			mutable.setX(x);
			for (int z = z1; z <= z2; ++z) {
				mutable.setZ(z);
				int floorExtent = (int) (30 + floorNoise.sample(x, z));
				int ceilingStart = (int) (200 + ceilingNoise.sample(x, z));
				int ceilingEnd = ceilingStart + 5;

				for (int y = 0; y < ceilingEnd; y++) {
					mutable.setY(y);
					if (y <= floorExtent) { // Packed ice floor
						world.setBlock(mutable, Blocks.PACKED_ICE.defaultBlockState(), 3);
					} else if (y >= ceilingStart) { // Ice roof
						world.setBlock(mutable, Blocks.ICE.defaultBlockState(), 3);
					} else {
						double voronoiAt = fastNoise.getCellular(x, y, z);

						if (voronoiAt > -0.125) { // Ice spires
							world.setBlock(mutable, Blocks.ICE.defaultBlockState(), 3);
						}
					}

					// Bedrock
					if (y <= 5) {
						if (chunkRandom.nextInt(y + 1) == 0) {
							chunk.setBlockState(mutable, Blocks.BEDROCK.defaultBlockState(), false);
						}
					}
				}
			}
		}
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types heightmapType) {
		return 0;
	}

	@Override
	public BlockGetter getBaseColumn(int x, int z) {
		return null;
	}
}
