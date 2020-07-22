package com.github.chainmailstudios.astromine.common.world.generation;

import java.util.Arrays;

import com.github.chainmailstudios.astromine.common.miscellaneous.BiomeGenCache;
import com.github.chainmailstudios.astromine.common.miscellaneous.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class MoonChunkGenerator extends ChunkGenerator {
	private static final double SCALE = 1.0 / 126.3;
	public static Codec<MoonChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BiomeSource.field_24713.fieldOf("biome_source").forGetter(gen -> gen.biomeSource), Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed))
			.apply(instance, MoonChunkGenerator::new));

	private final BiomeSource biomeSource;
	private final long seed;

	private final OpenSimplexNoise mainNoise1;
	private final OpenSimplexNoise mainNoise2;
	private final OpenSimplexNoise ridgedNoise;
	private final OpenSimplexNoise detailNoise;
	private final ThreadLocal<BiomeGenCache> cache;

	public MoonChunkGenerator(BiomeSource biomeSource, long seed) {
		super(biomeSource, new StructuresConfig(false));
		this.biomeSource = biomeSource;
		this.seed = seed;
		this.mainNoise1 = new OpenSimplexNoise(seed);
		this.mainNoise2 = new OpenSimplexNoise(seed + 79);
		this.ridgedNoise = new OpenSimplexNoise(seed - 79);
		this.detailNoise = new OpenSimplexNoise(seed + 2003);
		this.cache = ThreadLocal.withInitial(() -> new BiomeGenCache(biomeSource));
	}

	@Override
	protected Codec<? extends ChunkGenerator> method_28506() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new MoonChunkGenerator(new MoonBiomeSource(seed), seed);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		// Unused.
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		int x1 = chunk.getPos().getStartX();
		int z1 = chunk.getPos().getStartZ();

		int x2 = chunk.getPos().getEndX();
		int z2 = chunk.getPos().getEndZ();

		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
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
				double noise = (this.mainNoise1.eval(x * SCALE, z * SCALE) + this.mainNoise2.eval(x * SCALE * 2, z * SCALE * 2)) / 2;

				noise += (1 - Math.abs(ridgedNoise.eval(x * SCALE * 3.24, z * SCALE * 3.24))) * 0.5;
				noise += detailNoise.eval(x * 0.05, z * 0.05) * 0.2;

				noise /= 1.7;

				int height = (int) (depth + (noise * scale));
				for (int y = 0; y <= height; ++y) {
					chunk.setBlockState(new BlockPos(x, y, z), AstromineBlocks.MOON_STONE.getDefaultState(), false);
				}
			}
		}
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	@Override
	public BlockView getColumnSample(int x, int z) {
		BlockState[] states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(states);
	}
}
