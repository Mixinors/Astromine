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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.mars;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.common.miscellaneous.BiomeGeneratorCache;
import com.github.chainmailstudios.astromine.common.noise.OctaveNoiseSampler;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class MarsChunkGenerator extends ChunkGenerator {
	public static Codec<MarsChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry)).apply(instance,
		MarsChunkGenerator::new));

	private final long seed;
	private final Registry<Biome> biomeRegistry;
	private final OctaveNoiseSampler<OpenSimplexNoise> lowerInterpolatedNoise;
	private final OctaveNoiseSampler<OpenSimplexNoise> upperInterpolatedNoise;
	private final OctaveNoiseSampler<OpenSimplexNoise> interpolationNoise;
	private final ThreadLocal<BiomeGeneratorCache> cache;

	public MarsChunkGenerator(long seed, Registry<Biome> biomeRegistry) {
		super(new MarsBiomeSource(seed, biomeRegistry), new StructureSettings(false));
		this.seed = seed;
		this.biomeRegistry = biomeRegistry;
		Random random = new Random(seed);
		lowerInterpolatedNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 5, 140.43, 45, 10);
		upperInterpolatedNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 5, 140.43, 45, 10);
		interpolationNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 3, 80.32, 3, 3);
		this.cache = ThreadLocal.withInitial(() -> new BiomeGeneratorCache(runtimeBiomeSource));
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
		return new MarsChunkGenerator(seed, biomeRegistry);
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

		for (int x = x1; x <= x2; ++x) {
			mutable.setX(x);
			for (int z = z1; z <= z2; ++z) {
				mutable.setZ(z);

				float depth = 0;
				float scale = 0;
				int i = 0;

				// Biome lerp
				for (int x0 = -5; x0 <= 5; x0++) {
					for (int z0 = -5; z0 <= 5; z0++) {
						Biome biome = this.cache.get().getBiome((x + x0) >> 2, (z + z0) >> 2);

						i++;
						depth += biome.getDepth();
						scale += biome.getScale();
					}
				}

				depth /= i;
				scale /= i;

				// Noise calculation
				double noise = interpolationNoise.sample(x, z);
				if (noise >= 1) {
					noise = upperInterpolatedNoise.sample(x, z);
				} else if (noise <= -1) {
					noise = lowerInterpolatedNoise.sample(x, z);
				} else {
					noise = Mth.clampedLerp(lowerInterpolatedNoise.sample(x, z), upperInterpolatedNoise.sample(x, z), noise);
				}

				int height = (int) (depth + (noise * scale));
				for (int y = 0; y <= height; ++y) {
					mutable.setY(y);
					chunk.setBlockState(mutable, y == height ? AstromineDiscoveriesBlocks.MARTIAN_SOIL.defaultBlockState() : AstromineDiscoveriesBlocks.MARTIAN_STONE.defaultBlockState(), false);
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
		BlockState[] states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.defaultBlockState());
		return new NoiseColumn(states);
	}
}
