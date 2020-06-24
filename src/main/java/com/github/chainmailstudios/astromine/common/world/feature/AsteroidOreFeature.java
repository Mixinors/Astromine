package com.github.chainmailstudios.astromine.common.world.feature;

import com.github.chainmailstudios.astromine.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AsteroidOreFeature extends Feature<DefaultFeatureConfig> {
	public AsteroidOreFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos featurePosition, DefaultFeatureConfig config) {
		featurePosition = new BlockPos(featurePosition.getX(), random.nextInt(256), featurePosition.getZ());

		double xSize = 8 + random.nextDouble() * 48;
		double ySize = 8 + random.nextDouble() * 48;
		double zSize = 8 + random.nextDouble() * 48;

		List<Block> ores = Lists.newArrayList(AsteroidOreRegistry.INSTANCE.get(random.nextInt(100)));

		if (ores.isEmpty()) {
			return true;
		}

		Collections.shuffle(ores);

		Block ore = ores.get(random.nextInt(ores.size()));

		Shape vein = Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize)
				.applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true)))
				.applyLayer(TranslateLayer.of(Position.of(featurePosition)));

		for (Position streamPosition : vein.stream().collect(Collectors.toSet())) {
			BlockPos orePosition = streamPosition.toBlockPos();

			if (world.getBlockState(orePosition).getBlock() == AstromineBlocks.ASTEROID_STONE) {
				world.setBlockState(orePosition, ore.getDefaultState(), 0b0110100);
			}
		}

		return true;
	}
}
