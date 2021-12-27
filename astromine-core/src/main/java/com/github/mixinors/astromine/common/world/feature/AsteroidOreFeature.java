/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.client.registry.AsteroidOreRegistry;
import com.github.mixinors.astromine.common.util.WeightedList;
import com.github.mixinors.astromine.common.util.data.Range;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMConfig;
import com.mojang.serialization.Codec;
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.api.Quaternion;
import com.terraformersmc.terraform.shapes.api.Shape;
import com.terraformersmc.terraform.shapes.impl.Shapes;
import com.terraformersmc.terraform.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
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
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
		var world = context.getWorld();
		var random = context.getRandom();
		var featurePosition = context.getOrigin();
		var config = context.getConfig();
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

	private void chances(Random random, WeightedList<Block> ores) {
		for (var entry : AsteroidOreRegistry.INSTANCE.diameters.reference2ReferenceEntrySet()) {
			var pair = entry.getValue();
			if (pair != null) {
				ores.add(entry.getKey(), (int) ((pair.getLeft().maximum() - pair.getLeft().minimum()) * Objects.requireNonNull(random, "random").nextFloat() + pair.getLeft().minimum()));
			}
		}
	}

	private void place(StructureWorldAccess world, Random random, BlockPos featurePosition, Block ore, float xSize, float ySize, float zSize) {
		var vein = Shapes.ellipsoid(xSize, ySize, zSize).applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true))).applyLayer(TranslateLayer.of(Position.of(
				featurePosition)));

		for (var streamPosition : vein.stream().collect(Collectors.toSet())) {
			var orePosition = streamPosition.toBlockPos();

			if (world.getBlockState(orePosition).getBlock() == AMBlocks.ASTEROID_STONE.get()) {
				if (random.nextInt(AMConfig.get().asteroidOreThreshold) == 0) {
					world.setBlockState(orePosition, ore.getDefaultState(), 0b0110100);
				}
			}
		}
	}
}
