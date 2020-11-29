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

package com.github.chainmailstudios.astromine.discoveries.common.world.feature;

import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.discoveries.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.behavior.WeightedList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AsteroidOreFeature extends Feature<NoneFeatureConfiguration> {
	public AsteroidOreFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator generator, Random random, BlockPos featurePosition, NoneFeatureConfiguration config) {
		featurePosition = new BlockPos(featurePosition.getX(), random.nextInt(256), featurePosition.getZ());

		WeightedList<Block> ores = new WeightedList<>();

		for (Map.Entry<Block, @Nullable Tuple<Range<Integer>, Range<Integer>>> entry : AsteroidOreRegistry.INSTANCE.diameters.reference2ReferenceEntrySet()) {
			Tuple<Range<Integer>, Range<Integer>> pair = entry.getValue();

			if (pair != null) {
				ores.add(entry.getKey(), (int) ((pair.getA().getMaximum() - pair.getA().getMinimum()) * random.nextFloat() + pair.getA().getMinimum()));
			}
		}

		if (ores.isEmpty()) {
			return true;
		}

		Block ore = ores.getOne(random);

		double xSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double ySize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double zSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);

		if (xSize > 0 && ySize > 0 && zSize > 0) {
			Shape vein = Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize).applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true))).applyLayer(TranslateLayer.of(Position.of(featurePosition)));

			vein.stream().forEach(it -> {
				if (world.getBlockState(it.toBlockPos()).getBlock() == AstromineDiscoveriesBlocks.ASTEROID_STONE) {
					if (random.nextInt(AstromineConfig.get().asteroidOreThreshold) == 0) {
						world.setBlock(it.toBlockPos(), ore.defaultBlockState(), 0b0110100);
					}
				}
			});
		}

		return true;
	}
}
