package com.github.chainmailstudios.astromine.world.feature;

import java.util.Random;

import com.cumulus.shapes.api.Position;
import com.cumulus.shapes.api.Quaternion;
import com.cumulus.shapes.impl.Shapes;
import com.cumulus.shapes.impl.layer.transform.RotateLayer;
import com.cumulus.shapes.impl.layer.transform.TranslateLayer;
import com.github.chainmailstudios.astromine.misc.SimpleFiller;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class AsteroidFeature extends Feature<DefaultFeatureConfig> {
	public AsteroidFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = new BlockPos(pos.getX(), random.nextInt(256), pos.getZ());
//
		double xSize = 4.0 + random.nextDouble() * 8;
		double ySize = 4.0 + random.nextDouble() * 8;
		double zSize = 4.0 + random.nextDouble() * 8;
//
//		double radius = xSize * zSize * ySize;
//
//		for (int x = (int) -xSize; x <= xSize; x++) {
//			for (int z = (int) -zSize; z <= zSize; z++) {
//				for (int y = (int) -ySize; y <= ySize; y++) {
//					if (x * x + z * z + y * y <= radius) {
//						world.setBlockState(pos.add(x, y, z), Blocks.STONE.getDefaultState(), 3);
//					}
//				}
//			}
//		}

		Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize)
				.applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true)))
				.applyLayer(TranslateLayer.of(Position.of(pos)))
				.fill(SimpleFiller.of(world, Blocks.STONE.getDefaultState()));

		return true;
	}
}
