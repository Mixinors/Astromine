package com.github.chainmailstudios.astromine.discoveries.common.world.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class MoonOreFeature extends Feature<DefaultFeatureConfig> {
	public MoonOreFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {
		if (world.getBlockState(pos).isOpaque()) {
			world.setBlockState(pos, Blocks.DIAMOND_ORE.getDefaultState(), 3);
		}

		for (Direction direction : Direction.values()) {
			BlockPos local = pos.offset(direction);

			if (world.getBlockState(local).isOpaque()) {
				// TODO: replace with lunum
				world.setBlockState(local, Blocks.DIAMOND_ORE.getDefaultState(), 3);
			}
		}

		return false;
	}
}
