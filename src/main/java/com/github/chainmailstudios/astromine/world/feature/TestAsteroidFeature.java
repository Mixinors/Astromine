package com.github.chainmailstudios.astromine.world.feature;

import com.github.chainmailstudios.astromine.misc.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class TestAsteroidFeature extends Feature<DefaultFeatureConfig> {

	private static final OpenSimplexNoise noise = new OpenSimplexNoise();

	public TestAsteroidFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos featurePos, DefaultFeatureConfig config) {
		if (random.nextInt(4) != 3) {
			return false;
		}

		featurePos = new BlockPos(featurePos.getX(), random.nextInt(256), featurePos.getZ());

		int radius = 5 + world.getRandom().nextInt(50);

		// iterate over a square with the size of the radius
		for(int x = -radius; x <= radius; x++) {
			for(int y = -radius; y <= radius; y++) {
				for(int z = -radius; z <= radius; z++) {

					// calculate distance out
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
					BlockPos testPos = featurePos.add(x, y, z);
					double extraCheck = 5 * noise.eval(testPos.getX() / 10f, testPos.getY() / 10f, testPos.getZ() / 10f);

					// see if position in square is inside asteroid circle
					if(distance <= radius + extraCheck) {
						world.setBlockState(testPos, AstromineBlocks.ASTEROID_STONE.getDefaultState(), 3);
					}
				}
			}
		}

		return true;
	}
}
