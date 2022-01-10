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
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidCoalOreMinimumRange, AMConfig.get().asteroidCoalOreMaximumRange), Range.of(AMConfig.get().asteroidCoalOreMinimumSize, AMConfig.get().asteroidCoalOreMaximumSize), AMBlocks.ASTEROID_COAL_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidIronOreMinimumRange, AMConfig.get().asteroidIronOreMaximumRange), Range.of(AMConfig.get().asteroidIronOreMinimumSize, AMConfig.get().asteroidIronOreMaximumSize), AMBlocks.ASTEROID_IRON_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidGoldOreMinimumRange, AMConfig.get().asteroidGoldOreMaximumRange), Range.of(AMConfig.get().asteroidGoldOreMinimumSize, AMConfig.get().asteroidGoldOreMaximumSize), AMBlocks.ASTEROID_GOLD_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidCopperOreMinimumRange, AMConfig.get().asteroidCopperOreMaximumRange), Range.of(AMConfig.get().asteroidCopperOreMinimumSize, AMConfig.get().asteroidCopperOreMaximumSize), AMBlocks.ASTEROID_COPPER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidTinOreMinimumRange, AMConfig.get().asteroidTinOreMaximumRange), Range.of(AMConfig.get().asteroidTinOreMinimumSize, AMConfig.get().asteroidTinOreMaximumSize), AMBlocks.ASTEROID_TIN_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidSilverOreMinimumRange, AMConfig.get().asteroidSilverOreMaximumRange), Range.of(AMConfig.get().asteroidSilverOreMinimumSize, AMConfig.get().asteroidSilverOreMaximumSize), AMBlocks.ASTEROID_SILVER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidLeadOreMinimumRange, AMConfig.get().asteroidLeadOreMaximumRange), Range.of(AMConfig.get().asteroidLeadOreMinimumSize, AMConfig.get().asteroidLeadOreMaximumSize), AMBlocks.ASTEROID_LEAD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidRedstoneOreMinimumRange, AMConfig.get().asteroidRedstoneOreMaximumRange), Range.of(AMConfig.get().asteroidRedstoneOreMinimumSize, AMConfig.get().asteroidRedstoneOreMaximumSize), AMBlocks.ASTEROID_REDSTONE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidLapisOreMinimumRange, AMConfig.get().asteroidLapisOreMaximumRange), Range.of(AMConfig.get().asteroidLapisOreMinimumSize, AMConfig.get().asteroidLapisOreMaximumSize), AMBlocks.ASTEROID_LAPIS_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidDiamondOreMinimumRange, AMConfig.get().asteroidDiamondOreMaximumRange), Range.of(AMConfig.get().asteroidDiamondOreMinimumSize, AMConfig.get().asteroidDiamondOreMaximumSize), AMBlocks.ASTEROID_DIAMOND_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidEmeraldOreMinimumRange, AMConfig.get().asteroidEmeraldOreMaximumRange), Range.of(AMConfig.get().asteroidEmeraldOreMinimumSize, AMConfig.get().asteroidEmeraldOreMaximumSize), AMBlocks.ASTEROID_EMERALD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidMetiteOreMinimumRange, AMConfig.get().asteroidMetiteOreMaximumRange), Range.of(AMConfig.get().asteroidMetiteOreMinimumSize, AMConfig.get().asteroidMetiteOreMaximumSize), AMBlocks.ASTEROID_METITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidAsteriteOreMinimumRange, AMConfig.get().asteroidAsteriteOreMaximumRange), Range.of(AMConfig.get().asteroidAsteriteOreMinimumSize, AMConfig.get().asteroidAsteriteOreMaximumSize), AMBlocks.ASTEROID_ASTERITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidStellumOreMinimumRange, AMConfig.get().asteroidStellumOreMaximumRange), Range.of(AMConfig.get().asteroidStellumOreMinimumSize, AMConfig.get().asteroidStellumOreMaximumSize), AMBlocks.ASTEROID_STELLUM_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidGalaxiumOreMinimumRange, AMConfig.get().asteroidGalaxiumOreMaximumRange), Range.of(AMConfig.get().asteroidGalaxiumOreMinimumSize, AMConfig.get().asteroidGalaxiumOreMaximumSize), AMBlocks.ASTEROID_GALAXIUM_ORE.get());

		BiomeModifications.create(ORES_ID)
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldTinOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_SMALL_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldSilverOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_LOWER_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldLeadOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_SMALL_KEY);
				});
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> switch(context.getBiome().getCategory()) {
			case NETHER, THEEND, NONE -> false;
			default -> true;
		};
	}
}
