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

package com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class GenericOreFeatureWorldGenGenerator extends GenericFeatureWorldGenGenerator {
	public GenericOreFeatureWorldGenGenerator(Block oreBlock, int bottomOffset, int topOffset, int maximum, int count, int size) {
		this(oreBlock, new RangeDecoratorConfig(bottomOffset, topOffset, maximum), count, size, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD);
	}

	public GenericOreFeatureWorldGenGenerator(Block oreBlock, RangeDecoratorConfig range, int count, int size) {
		this(oreBlock, range, count, size, OreFeatureConfig.Rules.BASE_STONE_OVERWORLD);
	}

	public GenericOreFeatureWorldGenGenerator(Block oreBlock, RangeDecoratorConfig range, int count, int size, RuleTest ruleTest) {
		this(oreBlock.getDefaultState(), range, count, size, ruleTest);
	}

	public GenericOreFeatureWorldGenGenerator(BlockState oreBlockState, RangeDecoratorConfig range, int count, int size, RuleTest ruleTest) {
		super(Registry.BLOCK.getId(oreBlockState.getBlock()), Feature.ORE.configure(new OreFeatureConfig(ruleTest, oreBlockState, size))
				.decorate(Decorator.RANGE.configure(range)).spreadHorizontally().repeat(count));
	}

	@Override
	public String getGeneratorName() {
		return "generic_ore_feature";
	}
}
