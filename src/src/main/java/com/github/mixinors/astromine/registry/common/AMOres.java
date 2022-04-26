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

package com.github.mixinors.astromine.registry.common;

import java.util.function.Predicate;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.registry.AsteroidOreRegistry;
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.util.data.Range;
import com.github.mixinors.astromine.common.world.ore.OreDistribution;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;

public class AMOres {
	public static final Identifier ORES_ID = AMCommon.id("ores");

	public static final Identifier TIN_ORE_ID = AMCommon.id("tin_ore");
	public static final Identifier TIN_ORE_SMALL_ID = AMCommon.id("tin_ore_small");
	public static final Identifier SILVER_ORE_ID = AMCommon.id("silver_ore");
	public static final Identifier SILVER_ORE_LOWER_ID = AMCommon.id("silver_ore_lower");
	public static final Identifier LEAD_ORE_ID = AMCommon.id("lead_ore");
	public static final Identifier LEAD_ORE_SMALL_ID = AMCommon.id("lead_ore_small");

	public static final PlacedFeature TIN_ORE_PLACED_FEATURE = OreDistribution.TIN.registerPlacedFeature(TIN_ORE_ID, AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get());
	public static final PlacedFeature TIN_ORE_SMALL_PLACED_FEATURE = OreDistribution.TIN_SMALL.registerPlacedFeature(TIN_ORE_SMALL_ID, AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get());
	public static final PlacedFeature SILVER_ORE_PLACED_FEATURE = OreDistribution.SILVER.registerPlacedFeature(SILVER_ORE_ID, AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get());
	public static final PlacedFeature SILVER_ORE_LOWER_PLACED_FEATURE = OreDistribution.SILVER_LOWER.registerPlacedFeature(SILVER_ORE_LOWER_ID, AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get());
	public static final PlacedFeature LEAD_ORE_PLACED_FEATURE = OreDistribution.LEAD.registerPlacedFeature(LEAD_ORE_ID, AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get());
	public static final PlacedFeature LEAD_ORE_SMALL_PLACED_FEATURE = OreDistribution.LEAD_SMALL.registerPlacedFeature(LEAD_ORE_SMALL_ID, AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get());

	public static final RegistryKey<PlacedFeature> TIN_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, TIN_ORE_ID);
	public static final RegistryKey<PlacedFeature> TIN_ORE_SMALL_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, TIN_ORE_SMALL_ID);
	public static final RegistryKey<PlacedFeature> SILVER_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, SILVER_ORE_ID);
	public static final RegistryKey<PlacedFeature> SILVER_ORE_LOWER_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, SILVER_ORE_LOWER_ID);
	public static final RegistryKey<PlacedFeature> LEAD_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, LEAD_ORE_ID);
	public static final RegistryKey<PlacedFeature> LEAD_ORE_SMALL_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, LEAD_ORE_SMALL_ID);

	public static void init() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidCoalOre.minRange, AMConfig.get().world.ores.asteroidCoalOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidCoalOre.minSize, AMConfig.get().world.ores.asteroidCoalOre.maxSize), AMBlocks.ASTEROID_COAL_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidIronOre.minRange, AMConfig.get().world.ores.asteroidIronOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidIronOre.minSize, AMConfig.get().world.ores.asteroidIronOre.maxSize), AMBlocks.ASTEROID_IRON_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidGoldOre.minRange, AMConfig.get().world.ores.asteroidGoldOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidGoldOre.minSize, AMConfig.get().world.ores.asteroidGoldOre.maxSize), AMBlocks.ASTEROID_GOLD_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidCopperOre.minRange, AMConfig.get().world.ores.asteroidCopperOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidCopperOre.minSize, AMConfig.get().world.ores.asteroidCopperOre.maxSize), AMBlocks.ASTEROID_COPPER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidTinOre.minRange, AMConfig.get().world.ores.asteroidTinOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidTinOre.minSize, AMConfig.get().world.ores.asteroidTinOre.maxSize), AMBlocks.ASTEROID_TIN_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidSilverOre.minRange, AMConfig.get().world.ores.asteroidSilverOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidSilverOre.minSize, AMConfig.get().world.ores.asteroidSilverOre.maxSize), AMBlocks.ASTEROID_SILVER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidLeadOre.minRange, AMConfig.get().world.ores.asteroidLeadOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidLeadOre.minSize, AMConfig.get().world.ores.asteroidLeadOre.maxSize), AMBlocks.ASTEROID_LEAD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidRedstoneOre.minRange, AMConfig.get().world.ores.asteroidRedstoneOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidRedstoneOre.minSize, AMConfig.get().world.ores.asteroidRedstoneOre.maxSize), AMBlocks.ASTEROID_REDSTONE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidLapisOre.minRange, AMConfig.get().world.ores.asteroidLapisOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidLapisOre.minSize, AMConfig.get().world.ores.asteroidLapisOre.maxSize), AMBlocks.ASTEROID_LAPIS_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidDiamondOre.minRange, AMConfig.get().world.ores.asteroidDiamondOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidDiamondOre.minSize, AMConfig.get().world.ores.asteroidDiamondOre.maxSize), AMBlocks.ASTEROID_DIAMOND_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidEmeraldOre.minRange, AMConfig.get().world.ores.asteroidEmeraldOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidEmeraldOre.minSize, AMConfig.get().world.ores.asteroidEmeraldOre.maxSize), AMBlocks.ASTEROID_EMERALD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidMetiteOre.minRange, AMConfig.get().world.ores.asteroidMetiteOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidMetiteOre.minSize, AMConfig.get().world.ores.asteroidMetiteOre.maxSize), AMBlocks.ASTEROID_METITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidAsteriteOre.minRange, AMConfig.get().world.ores.asteroidAsteriteOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidAsteriteOre.minSize, AMConfig.get().world.ores.asteroidAsteriteOre.maxSize), AMBlocks.ASTEROID_ASTERITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidStellumOre.minRange, AMConfig.get().world.ores.asteroidStellumOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidStellumOre.minSize, AMConfig.get().world.ores.asteroidStellumOre.maxSize), AMBlocks.ASTEROID_STELLUM_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().world.ores.asteroidGalaxiumOre.minRange, AMConfig.get().world.ores.asteroidGalaxiumOre.maxRange), Range.of(AMConfig.get().world.ores.asteroidGalaxiumOre.minSize, AMConfig.get().world.ores.asteroidGalaxiumOre.maxSize), AMBlocks.ASTEROID_GALAXIUM_ORE.get());

		BiomeModifications.create(ORES_ID)
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().world.ores.overworldTinOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_SMALL_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().world.ores.overworldSilverOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_LOWER_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().world.ores.overworldLeadOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_SMALL_KEY);
				});
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> switch (context.getBiome().getCategory()) {
			case NETHER, THEEND, NONE -> false;
			default -> true;
		};
	}
}
