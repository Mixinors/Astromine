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

		List<Block> ores = Lists.newArrayList(AsteroidOreRegistry.INSTANCE.get(random.nextInt(100)));

		if (ores.isEmpty()) {
			return true;
		}

		Collections.shuffle(ores);

		Block ore = ores.get(random.nextInt(ores.size()));

		double xSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double ySize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double zSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);

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
