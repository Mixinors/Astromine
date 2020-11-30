package com.github.chainmailstudios.astromine.datagen.generator.worldgen.onetime;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class GenericOreFeatureWorldGenGenerator extends GenericFeatureWorldGenGenerator {
	public GenericOreFeatureWorldGenGenerator(Block oreBlock, int bottomOffset, int topOffset, int maximum, int count, int size) {
		this(oreBlock, new RangeDecoratorConfiguration(bottomOffset, topOffset, maximum), count, size, OreConfiguration.Predicates.NATURAL_STONE);
	}

	public GenericOreFeatureWorldGenGenerator(Block oreBlock, RangeDecoratorConfiguration range, int count, int size) {
		this(oreBlock, range, count, size, OreConfiguration.Predicates.NATURAL_STONE);
	}

	public GenericOreFeatureWorldGenGenerator(Block oreBlock, RangeDecoratorConfiguration range, int count, int size, RuleTest ruleTest) {
		this(oreBlock.defaultBlockState(), range, count, size, ruleTest);
	}

	public GenericOreFeatureWorldGenGenerator(BlockState oreBlockState, RangeDecoratorConfiguration range, int count, int size, RuleTest ruleTest) {
		super(Registry.BLOCK.getKey(oreBlockState.getBlock()), Feature.ORE.configured(new OreConfiguration(ruleTest, oreBlockState, size))
			.decorated(FeatureDecorator.RANGE.configured(range)).squared().count(count));
	}

	@Override
	public String getGeneratorName() {
		return "generic_ore_feature";
	}
}
