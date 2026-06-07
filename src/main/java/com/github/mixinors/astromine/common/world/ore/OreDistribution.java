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

package com.github.mixinors.astromine.common.world.ore;

import com.github.mixinors.astromine.registry.common.AMFeatures;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public record OreDistribution(
		int veinSize,
		IntProvider veinsPerChunk,
		VerticalAnchor min,
		VerticalAnchor max,
		float discardOnAirChance,
		HeightRangePlacementModifierType type
) {
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
	
	public static final OreDistribution TIN = trapezoid(10, 14, VerticalAnchor.absolute(-24), VerticalAnchor.absolute(88));
	public static final OreDistribution TIN_SMALL = uniform(4, 2, VerticalAnchor.absolute(-24), VerticalAnchor.absolute(64), 0.1f);
	public static final OreDistribution SILVER = trapezoid(9, 6, VerticalAnchor.absolute(-56), VerticalAnchor.absolute(64), 0.2f);
	public static final OreDistribution SILVER_LOWER = uniform(9, UniformInt.of(0, 2), VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-16), 0.2f);
	public static final OreDistribution LEAD = trapezoid(8, 8, VerticalAnchor.absolute(-32), VerticalAnchor.absolute(48));
	public static final OreDistribution LEAD_SMALL = uniform(4, 8, VerticalAnchor.bottom(), VerticalAnchor.absolute(56));
	
	/**
	 * Creates a new uniform Ore Distribution.
	 *
	 * @param veinSize           the maximum size of each vein
	 * @param veinsPerChunk      the maximum amount of veins per chunk
	 * @param min                the minimum y offset
	 * @param max                the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution uniform(int veinSize, IntProvider veinsPerChunk, VerticalAnchor min, VerticalAnchor max, float discardOnAirChance) {
		return new OreDistribution(veinSize, veinsPerChunk, min, max, discardOnAirChance, HeightRangePlacementModifierType.UNIFORM);
	}
	
	/**
	 * Creates a new uniform Ore Distribution.
	 *
	 * @param veinSize      the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min           the minimum y offset
	 * @param max           the maximum y offset
	 */
	public static OreDistribution uniform(int veinSize, IntProvider veinsPerChunk, VerticalAnchor min, VerticalAnchor max) {
		return uniform(veinSize, veinsPerChunk, min, max, 0.0f);
	}
	
	/**
	 * Creates a new uniform Ore Distribution.
	 *
	 * @param veinSize           the maximum size of each vein
	 * @param veinsPerChunk      the maximum amount of veins per chunk
	 * @param min                the minimum y offset
	 * @param max                the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution uniform(int veinSize, int veinsPerChunk, VerticalAnchor min, VerticalAnchor max, float discardOnAirChance) {
		return uniform(veinSize, ConstantInt.of(veinsPerChunk), min, max, discardOnAirChance);
	}
	
	/**
	 * Creates a new uniform Ore Distribution.
	 *
	 * @param veinSize      the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min           the minimum y offset
	 * @param max           the maximum y offset
	 */
	public static OreDistribution uniform(int veinSize, int veinsPerChunk, VerticalAnchor min, VerticalAnchor max) {
		return uniform(veinSize, veinsPerChunk, min, max, 0.0f);
	}
	
	/**
	 * Creates a new trapezoid Ore Distribution.
	 *
	 * @param veinSize           the maximum size of each vein
	 * @param veinsPerChunk      the maximum amount of veins per chunk
	 * @param min                the minimum y offset
	 * @param max                the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution trapezoid(int veinSize, IntProvider veinsPerChunk, VerticalAnchor min, VerticalAnchor max, float discardOnAirChance) {
		return new OreDistribution(veinSize, veinsPerChunk, min, max, discardOnAirChance, HeightRangePlacementModifierType.TRAPEZOID);
	}
	
	/**
	 * Creates a new trapezoid Ore Distribution.
	 *
	 * @param veinSize      the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min           the minimum y offset
	 * @param max           the maximum y offset
	 */
	public static OreDistribution trapezoid(int veinSize, IntProvider veinsPerChunk, VerticalAnchor min, VerticalAnchor max) {
		return trapezoid(veinSize, veinsPerChunk, min, max, 0.0f);
	}
	
	/**
	 * Creates a new trapezoid Ore Distribution.
	 *
	 * @param veinSize           the maximum size of each vein
	 * @param veinsPerChunk      the maximum amount of veins per chunk
	 * @param min                the minimum y offset
	 * @param max                the maximum y offset
	 * @param discardOnAirChance the chance that an ore block won't generate if directly exposed to air
	 */
	public static OreDistribution trapezoid(int veinSize, int veinsPerChunk, VerticalAnchor min, VerticalAnchor max, float discardOnAirChance) {
		return trapezoid(veinSize, ConstantInt.of(veinsPerChunk), min, max, discardOnAirChance);
	}
	
	/**
	 * Creates a new trapezoid Ore Distribution.
	 *
	 * @param veinSize      the maximum size of each vein
	 * @param veinsPerChunk the maximum amount of veins per chunk
	 * @param min           the minimum y offset
	 * @param max           the maximum y offset
	 */
	public static OreDistribution trapezoid(int veinSize, int veinsPerChunk, VerticalAnchor min, VerticalAnchor max) {
		return trapezoid(veinSize, veinsPerChunk, min, max, 0.0f);
	}
	
	private Holder<ConfiguredFeature<OreConfiguration, ?>> registerConfiguredFeature(ResourceLocation id, Block stoneOre, Block deepslateOre) {
		var targets = ImmutableList.of(
				OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES), stoneOre.defaultBlockState()),
				OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslateOre.defaultBlockState())
		);
		
		return AMFeatures.registerConfiguredFeature(id, Feature.ORE, new OreConfiguration(targets, veinSize, discardOnAirChance));
	}
	
	/**
	 * Creates and registers the placed feature for this ore distribution.
	 *
	 * @param id           the id of the placed feature to register
	 * @param stoneOre     the stone variant of the ore
	 * @param deepslateOre the deepslate variant of the ore
	 */
	public Holder<PlacedFeature> registerPlacedFeature(ResourceLocation id, Block stoneOre, Block deepslateOre) {
		return AMFeatures.registerPlacedFeature(id, registerConfiguredFeature(id, stoneOre, deepslateOre), modifiers());
	}
	
	private HeightRangePlacement heightRangePlacementModifier() {
		return switch (type) {
			case UNIFORM -> HeightRangePlacement.uniform(min, max);
			case TRAPEZOID -> HeightRangePlacement.triangle(min, max);
		};
	}
	
	private CountPlacement countPlacementModifier() {
		return CountPlacement.of(veinsPerChunk);
	}
	
	private ImmutableList<PlacementModifier> modifiers() {
		return ImmutableList.of(countPlacementModifier(), InSquarePlacement.spread(), heightRangePlacementModifier(), BiomeFilter.biome());
	}
	
	public enum HeightRangePlacementModifierType {
		UNIFORM,
		TRAPEZOID
	}
}
