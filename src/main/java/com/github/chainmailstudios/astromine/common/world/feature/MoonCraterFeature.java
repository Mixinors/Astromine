package com.github.chainmailstudios.astromine.common.world.feature;

import java.util.Random;

import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class MoonCraterFeature extends Feature<DefaultFeatureConfig> {
	private static final double SCALE = 1 / 19.42;
	private long seed = 0;
	private OpenSimplexNoise noise = new OpenSimplexNoise(0);

	public MoonCraterFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (this.seed != world.getSeed()) {
			this.noise = new OpenSimplexNoise(world.getSeed());
			this.seed = world.getSeed();
		}

		int radius = random.nextInt(7) + 4;

		int radiusSquared = radius * radius;

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {

				for (int y = -radius; y <= radius; y++) {
					int squareDistance = (x * x) + (y * y) + (z * z);

					if (squareDistance <= radiusSquared) {
						BlockPos local = pos.add(x, y, z);

						double noiseX = local.getX() * SCALE;
						double noiseY = local.getY() * SCALE;
						double noiseZ = local.getZ() * SCALE;

						double noise = this.noise.sample(noiseX, noiseY, noiseZ);
						noise += computeNoiseFalloff(y, radius);

						if (noise > 0 && !world.getBlockState(local).isAir()) {
							world.setBlockState(local, Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
		}

		return true;
	}

	private double computeNoiseFalloff(int y, int radius) {
		return -(2.0 / (y + radius + 0.5));
	}
}
