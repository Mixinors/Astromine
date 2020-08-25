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
