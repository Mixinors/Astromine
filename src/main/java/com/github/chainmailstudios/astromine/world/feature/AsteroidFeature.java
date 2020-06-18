package com.github.chainmailstudios.astromine.world.feature;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.common.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AsteroidFeature extends Feature<DefaultFeatureConfig> {
	public AsteroidFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos fP, DefaultFeatureConfig config) {
		if (random.nextInt(4) != 3) {
			return false;
		}

		fP = new BlockPos(fP.getX(), random.nextInt(256), fP.getZ());

		int baseSize = 4;
		if (random.nextInt(4) == 0) baseSize = 12;

		double xSize = baseSize + random.nextDouble() * 8;
		double ySize = baseSize + random.nextDouble() * 8;
		double zSize = baseSize + random.nextDouble() * 8;

		if (fP.getY() + ySize >= 255) {
			return false;
		}

		if (fP.getY() - ySize < 0) {
			return false;
		}

		Shape asteroid = Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize)
		                       .applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 720, random.nextDouble() * 720, random.nextDouble() * 720, true)))
		                       .applyLayer(TranslateLayer.of(Position.of(fP)));

		for (Position vP : asteroid.stream().collect(Collectors.toSet())) {
			world.setBlockState(vP.toBlockPos(), AstromineBlocks.ASTEROID_STONE.getDefaultState(), 0b0110100);
		}

		List<Block> ores = AsteroidOreRegistry.INSTANCE.get(random.nextInt(100));

		if (ores.isEmpty()) {
			return true;
		}

		Collections.shuffle(ores);

		for (int i = 0; i < Math.min(2, random.nextInt(ores.size())); ++i) {
			Shape vein = Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize)
			                   .applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true)))
			                   .applyLayer(TranslateLayer.of(Position.of(fP)));

			for (Position vP : vein.stream().collect(Collectors.toSet())) {
				BlockPos cP = vP.toBlockPos();

				if (world.getBlockState(cP).getBlock() == AstromineBlocks.ASTEROID_STONE) {
					world.setBlockState(cP, ores.get(i).getDefaultState(), 0b0110100);
				}
			}
		}

		return true;
	}
}
