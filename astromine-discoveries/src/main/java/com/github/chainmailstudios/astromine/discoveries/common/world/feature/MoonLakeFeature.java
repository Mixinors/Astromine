package com.github.chainmailstudios.astromine.discoveries.common.world.feature;

import java.util.Random;

import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class MoonLakeFeature extends Feature<DefaultFeatureConfig> {
	public MoonLakeFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {
		BlockPos.Mutable mutable = pos.mutableCopy();

		for (int x = -4; x <= 4; x++) {
		    for (int z = -4; z <= 4; z++) {
				for (int y = -4; y <= 4; y++) {
					if (!world.getBlockState(mutable.set(pos, x, y, z)).isOf(AstromineDiscoveriesBlocks.MOON_STONE)) {
						return false;
					}
				}
		    }
		}

		double radius = 4 * 4;

		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = -4; y <= 4; y++) {
					double dist = (x * x) + (y * y) + (z * z);
					if (dist <= radius) {
						if (y < -1) {
							world.setBlockState(mutable.set(pos, x, y, z), Blocks.ICE.getDefaultState(), 3);
						} else {
							world.setBlockState(mutable.set(pos, x, y, z), Blocks.AIR.getDefaultState(), 3);
						}
					}
				}
			}
		}


		return true;
	}
}
