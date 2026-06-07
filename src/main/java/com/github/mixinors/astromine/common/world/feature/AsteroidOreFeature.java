/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.world.feature;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.registry.AsteroidOreRegistry;
import com.github.mixinors.astromine.common.util.WeightedList;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.mojang.serialization.Codec;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AsteroidOreFeature extends Feature<NoneFeatureConfiguration> {
	public AsteroidOreFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		var world = context.level();
		var random = context.random();
		var featurePosition = context.origin();
		
		featurePosition = new BlockPos(featurePosition.getX(), random.nextInt(256), featurePosition.getZ());
		
		var ores = new WeightedList<Block>();
		
		chances(random, ores);
		
		if (ores.isEmpty()) {
			return true;
		}
		
		ores.shuffle(random);
		
		var ore = ores.stream().findFirst().orElse(AMBlocks.ASTEROID_STONE.get());
		
		var xSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		var ySize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		var zSize = AsteroidOreRegistry.INSTANCE.getDiameter(random, ore);
		
		if (xSize > 0 && ySize > 0 && zSize > 0) {
			this.place(world, random, featurePosition, ore, (float) xSize, (float) ySize, (float) zSize);
		}
		
		return true;
	}
	
	private void chances(RandomSource random, WeightedList<Block> ores) {
		for (var entry : AsteroidOreRegistry.INSTANCE.diameters.reference2ReferenceEntrySet()) {
			var pair = entry.getValue();
			if (pair != null) {
				ores.add(entry.getKey(), (int) ((pair.getA().maximum() - pair.getA().minimum()) * Objects.requireNonNull(random, "random").nextFloat() + pair.getA().minimum()));
			}
		}
	}
	
	private void place(WorldGenLevel world, RandomSource random, BlockPos featurePosition, Block ore, float xSize, float ySize, float zSize) {
		for (var x = (int) -xSize; x <= xSize; ++x) {
			for (var y = (int) -ySize; y <= ySize; ++y) {
				for (var z = (int) -zSize; z <= zSize; ++z) {
					var normalized = x * x / (xSize * xSize) + y * y / (ySize * ySize) + z * z / (zSize * zSize);
					
					if (normalized > 1.0F) {
						continue;
					}
					
					var orePosition = featurePosition.offset(x, y, z);
					
					if (world.getBlockState(orePosition).is(AMBlocks.ASTEROID_STONE.get()) && random.nextInt(AMConfig.get().world.asteroidOreGenerationThreshold) == 0) {
						world.setBlock(orePosition, ore.defaultBlockState(), 0b0110100);
					}
				}
			}
		}
	}
}
