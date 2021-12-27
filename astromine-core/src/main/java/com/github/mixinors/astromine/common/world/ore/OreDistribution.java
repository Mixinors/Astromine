package com.github.mixinors.astromine.common.world.ore;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountPlacementModifier;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

public record OreDistribution(
		int veinSize, IntProvider veinsPerChunk,
		YOffset min, YOffset max,
		float discardOnAirChance, HeightRangePlacementModifierType type) {

	/*
		For reference, vanilla values:
		- COAL_UPPER = uniform(17, 30, YOffset.fixed(136), YOffset.getTop());
		- COAL_LOWER = trapezoid(17, 20, YOffset.fixed(0), YOffset.fixed(192), 0.5f);
		- IRON_UPPER = trapezoid(9, 90, YOffset.fixed(80), YOffset.fixed(384));
		- IRON_MIDDLE = trapezoid(9, 10, YOffset.fixed(-24), YOffset.fixed(56));
		- IRON_SMALL = uniform(4, 10, YOffset.getBottom(), YOffset.fixed(72));
		- GOLD_EXTRA = uniform(9, 50, YOffset.fixed(32), YOffset.fixed(256)); // only in badlands
		- GOLD = trapezoid(9, 4, YOffset.fixed(-64), YOffset.fixed(32), 0.5f);
		- GOLD_LOWER = uniform(9, UniformIntProvider.create(0, 1), YOffset.fixed(-64), YOffset.fixed(-48), 0.5f);
		- REDSTONE = uniform(8, 4, YOffset.getBottom(), YOffset.fixed(15));
		- REDSTONE_LOWER = trapezoid(8, 8, YOffset.aboveBottom(-32), YOffset.aboveBottom(32));
		- DIAMOND = trapezoid(4, 7, YOffset.aboveBottom(-80), YOffset.aboveBottom(80), 0.5f);
		- DIAMOND_LARGE = trapezoid(12, 9, YOffset.aboveBottom(-80), YOffset.aboveBottom(80), 0.7f);
		- DIAMOND_BURIED = trapezoid(8, 4, YOffset.aboveBottom(-80), YOffset.aboveBottom(80), 1.0f);
		- LAPIS = trapezoid(7, 2, YOffset.fixed(-32), YOffset.fixed(32));
		- LAPIS_BURIED = uniform(7, 4, YOffset.getBottom(), YOffset.fixed(64), 1.0f);
		- EMERALD = trapezoid(3, 100, YOffset.fixed(-16), YOffset.fixed(480)); // only in mountains
		- COPPER = trapezoid(10, 16, YOffset.fixed(-16), YOffset.fixed(112));
		- COPPER_LARGE = trapezoid(20, 16, YOffset.fixed(-16), YOffset.fixed(112)); // only in dripstone caves
	 */

	public static final OreDistribution TIN = trapezoid(10, 14, YOffset.fixed(-24), YOffset.fixed(88));
	public static final OreDistribution TIN_SMALL = uniform(4, 2, YOffset.fixed(-24), YOffset.fixed(64), 0.1f);
	public static final OreDistribution SILVER = trapezoid(9, 6, YOffset.fixed(-56), YOffset.fixed(64), 0.2f);
	public static final OreDistribution SILVER_LOWER = uniform(9, UniformIntProvider.create(0, 2), YOffset.fixed(-64), YOffset.fixed(-16), 0.2f);
	public static final OreDistribution LEAD = trapezoid(8, 8, YOffset.fixed(-32), YOffset.fixed(48));
	public static final OreDistribution LEAD_SMALL = uniform(4, 8, YOffset.getBottom(), YOffset.fixed(56));

	/**
	 * Creates a new uniform Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution uniform(int veinSize, IntProvider veinsPerChunk, YOffset min, YOffset max, float discardOnAirChance) {
		return new OreDistribution(veinSize, veinsPerChunk, min, max, discardOnAirChance, HeightRangePlacementModifierType.UNIFORM);
	}

	/**
	 * Creates a new uniform Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 */
	public static OreDistribution uniform(int veinSize, IntProvider veinsPerChunk, YOffset min, YOffset max) {
		return uniform(veinSize, veinsPerChunk, min, max, 0.0f);
	}

	/**
	 * Creates a new uniform Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution uniform(int veinSize, int veinsPerChunk, YOffset min, YOffset max, float discardOnAirChance) {
		return uniform(veinSize, ConstantIntProvider.create(veinsPerChunk), min, max, discardOnAirChance);
	}

	/**
	 * Creates a new uniform Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 */
	public static OreDistribution uniform(int veinSize, int veinsPerChunk, YOffset min, YOffset max) {
		return uniform(veinSize, veinsPerChunk, min, max, 0.0f);
	}

	/**
	 * Creates a new trapezoid Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution trapezoid(int veinSize, IntProvider veinsPerChunk, YOffset min, YOffset max, float discardOnAirChance) {
		return new OreDistribution(veinSize, veinsPerChunk, min, max, discardOnAirChance, HeightRangePlacementModifierType.TRAPEZOID);
	}

	/**
	 * Creates a new trapezoid Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 */
	public static OreDistribution trapezoid(int veinSize, IntProvider veinsPerChunk, YOffset min, YOffset max) {
		return trapezoid(veinSize, veinsPerChunk, min, max, 0.0f);
	}

	/**
	 * Creates a new trapezoid Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution trapezoid(int veinSize, int veinsPerChunk, YOffset min, YOffset max, float discardOnAirChance) {
		return trapezoid(veinSize, ConstantIntProvider.create(veinsPerChunk), min, max, discardOnAirChance);
	}

	/**
	 * Creates a new trapezoid Ore Distribution.
	 * @param veinSize the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min the minimum y offset
	 * @param max the maximum y offset
	 */
	public static OreDistribution trapezoid(int veinSize, int veinsPerChunk, YOffset min, YOffset max) {
		return trapezoid(veinSize, veinsPerChunk, min, max, 0.0f);
	}

	private ConfiguredFeature<?, ?> registerConfiguredFeature(Identifier id, Block stoneOre, Block deepslateOre) {
		var targets = List.of(
				OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, stoneOre.getDefaultState()),
				OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.getDefaultState())
		);
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, Feature.ORE.configure(new OreFeatureConfig(targets, veinSize, discardOnAirChance)));
	}

	/**
	 * Creates and registers the placed feature for this ore distribution.
	 * @param id the id of the placed feature to register
	 * @param stoneOre the stone variant of the ore
	 * @param deepslateOre the deepslate variant of the ore
	 */
	public PlacedFeature registerPlacedFeature(Identifier id, Block stoneOre, Block deepslateOre) {
		return Registry.register(BuiltinRegistries.PLACED_FEATURE, id, registerConfiguredFeature(id, stoneOre, deepslateOre).withPlacement(modifiers()));
	}

	private HeightRangePlacementModifier heightRangePlacementModifier() {
		return switch(type) {
			case UNIFORM -> HeightRangePlacementModifier.uniform(min, max);
			case TRAPEZOID -> HeightRangePlacementModifier.trapezoid(min, max);
		};
	}

	private CountPlacementModifier countPlacementModifier() {
		return CountPlacementModifier.of(veinsPerChunk);
	}

	private List<PlacementModifier> modifiers() {
		return List.of(countPlacementModifier(), SquarePlacementModifier.of(), heightRangePlacementModifier(), BiomePlacementModifier.of());
	}

	public enum HeightRangePlacementModifierType {
		UNIFORM,
		TRAPEZOID
	}
}
