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

package com.github.chainmailstudios.astromine.discoveries.common.world.generation.moon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.github.chainmailstudios.astromine.common.miscellaneous.BiomeGeneratorCache;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.server.level.WorldGenRegion;
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

public class MoonChunkGenerator extends ChunkGenerator {
	private static final double SCALE = 1.0 / 126.3;
	public static Codec<MoonChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed), RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(source -> source.biomeRegistry)).apply(instance,
		MoonChunkGenerator::new));

	private final long seed;
	private final Registry<Biome> biomeRegistry;
	private final OpenSimplexNoise mainNoise1;
	private final OpenSimplexNoise mainNoise2;
	private final OpenSimplexNoise ridgedNoise;
	private final OpenSimplexNoise detailNoise;
	private final OpenSimplexNoise caveNoise;
	private final ThreadLocal<BiomeGeneratorCache> cache;

	public MoonChunkGenerator(long seed, Registry<Biome> biomeRegistry) {
		super(new MoonBiomeSource(seed, biomeRegistry), new StructureSettings(false));
		this.seed = seed;
		this.biomeRegistry = biomeRegistry;
		this.mainNoise1 = new OpenSimplexNoise(seed);
		this.mainNoise2 = new OpenSimplexNoise(seed + 79);
		this.ridgedNoise = new OpenSimplexNoise(seed - 79);
		this.detailNoise = new OpenSimplexNoise(seed + 2003);
		this.caveNoise = new OpenSimplexNoise(seed - 2003);
		this.cache = ThreadLocal.withInitial(() -> new BiomeGeneratorCache(runtimeBiomeSource));
	}

	private static double computeNoiseFalloff(int y) {
		return Math.max(((10.0) / (y + 0.1)) - 1, 0);
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
		return new MoonChunkGenerator(seed, biomeRegistry);
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
				for (int x0 = -8; x0 <= 8; x0++) {
					for (int z0 = -8; z0 <= 8; z0++) {
						Biome biome = this.cache.get().getBiome((x + x0) >> 2, (z + z0) >> 2);

						i++;
						depth += biome.getDepth();
						scale += biome.getScale();
					}
				}

				depth /= i;
				scale /= i;

				// Noise calculation
				double noise = (this.mainNoise1.sample(x * SCALE, z * SCALE) + this.mainNoise2.sample(x * SCALE * 2, z * SCALE * 2)) / 2;

				noise += (1 - Math.abs(ridgedNoise.sample(x * SCALE * 3.24, z * SCALE * 3.24))) * 0.5;
				noise += detailNoise.sample(x * 0.05, z * 0.05) * 0.2;

				noise /= 1.7;

				int height = (int) (depth + (noise * scale));
				for (int y = 0; y <= height; ++y) {
					mutable.setY(y);

					double caveExtent = caveNoise.sample(x * SCALE, y / 32.0, z * SCALE) + computeNoiseFalloff(y);

					if (caveExtent > -0.575) {
						chunk.setBlockState(mutable, AstromineDiscoveriesBlocks.MOON_STONE.defaultBlockState(), false);
					}

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
