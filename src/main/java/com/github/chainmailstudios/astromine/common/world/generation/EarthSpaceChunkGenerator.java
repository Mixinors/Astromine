package com.github.chainmailstudios.astromine.common.world.generation;

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
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import java.util.Arrays;

public class EarthSpaceChunkGenerator extends ChunkGenerator {
	public static Codec<EarthSpaceChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BiomeSource.field_24713.fieldOf("biome_source").forGetter(gen -> gen.biomeSource), Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed))
			.apply(instance, EarthSpaceChunkGenerator::new));

	private final BiomeSource biomeSource;
	private final long seed;

	private final OpenSimplexNoise noise;

	public EarthSpaceChunkGenerator(BiomeSource biomeSource, long seed) {
		super(biomeSource, new StructuresConfig(false));
		this.biomeSource = biomeSource;
		this.seed = seed;
		this.noise = new OpenSimplexNoise(seed);
	}

	@Override
	protected Codec<? extends ChunkGenerator> method_28506() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new EarthSpaceChunkGenerator(new EarthSpaceBiomeSource(seed), seed);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		int x1 = chunk.getPos().getStartX();
		int z1 = chunk.getPos().getStartZ();
		int y1 = 0;

		int x2 = chunk.getPos().getEndX();
		int z2 = chunk.getPos().getEndZ();
		int y2 = 256;

		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
				for (int y = y1; y <= y2; ++y) {
					double value = noise.eval(x * 0.01, y * 0.01, z * 0.01);

					if (value > 0.65) {
						chunk.setBlockState(new BlockPos(x, y, z), AstromineBlocks.ASTEROID_STONE.getDefaultState(), false);
					}
				}
			}
		}
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		// Unused.
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
