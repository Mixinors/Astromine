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

import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import com.mojang.serialization.Codec;

import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.discoveries.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.discoveries.registry.AstromineDiscoveriesBlocks;
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

public class AsteroidOreFeature extends Feature<DefaultFeatureConfig> {
	public AsteroidOreFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos featurePosition, DefaultFeatureConfig config) {
		featurePosition = new BlockPos(featurePosition.getX(), random.nextInt(256), featurePosition.getZ());

		WeightedList<Block> ores = new WeightedList<>();

		for (Map.Entry<Block, @Nullable Pair<Range<Integer>, Range<Integer>>> entry : AsteroidOreRegistry.INSTANCE.diameters.reference2ReferenceEntrySet()) {
			Pair<Range<Integer>, Range<Integer>> pair = entry.getValue();
			if (pair != null) {
				ores.add(entry.getKey(), (int) ((pair.getLeft().getMaximum() - pair.getLeft().getMinimum()) * Objects.requireNonNull(random, "random").nextFloat() + pair.getLeft().getMinimum()));
			}
		}

		if (ores.isEmpty()) {
			return true;
		}

		Block ore = ores.pickRandom(random);

		double xSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double ySize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		double zSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);

		if (xSize > 0 && ySize > 0 && zSize > 0) {
			Shape vein = Shapes.ellipsoid((float) xSize, (float) ySize, (float) zSize).applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true))).applyLayer(TranslateLayer.of(Position.of(featurePosition)));

			for (Position streamPosition : vein.stream().collect(Collectors.toSet())) {
				BlockPos orePosition = streamPosition.toBlockPos();

				if (world.getBlockState(orePosition).getBlock() == AstromineDiscoveriesBlocks.ASTEROID_STONE) {
					if (random.nextInt(AstromineConfig.get().asteroidOreThreshold) == 0) { // Only 1 in 8 blocks is ore, essentially anti-veinminer
						world.setBlockState(orePosition, ore.getDefaultState(), 0b0110100);
					}
				}
			}
		}

		return true;
	}
}
