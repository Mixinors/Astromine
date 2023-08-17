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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.registry.base.RegistryEntry;
import com.github.mixinors.astromine.common.world.feature.AsteroidOreFeature;
import com.github.mixinors.astromine.common.world.feature.OilWellFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.List;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES_FEATURE = registerFeature(ASTEROID_ORES_ID, new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> ASTEROID_ORES_CONFIGURED_FEATURE = registerConfiguredFeature(ASTEROID_ORES_ID, ASTEROID_ORES_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> ASTEROID_ORES_PLACED_FEATURE = registerPlacedFeature(ASTEROID_ORES_ID, ASTEROID_ORES_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(20), SquarePlacementModifier.of(), BiomePlacementModifier.of());
	
	public static final Identifier OIL_WELL_ID = AMCommon.id("oil_well");
	
	public static final Feature<DefaultFeatureConfig> OIL_WELL_FEATURE = registerFeature(OIL_WELL_ID, new OilWellFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> OIL_WELL_CONFIGURED_FEATURE = registerConfiguredFeature(OIL_WELL_ID, OIL_WELL_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> OIL_WELL_PLACED_FEATURE = registerPlacedFeature(OIL_WELL_ID, OIL_WELL_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(100), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
	
	public static void init() {
		BiomeModifications.create(OIL_WELL_ID).add(ModificationPhase.ADDITIONS, (biomeSelectionContext) -> {
			var entry = biomeSelectionContext.getBiomeRegistryEntry();
			
			return entry.isIn(ConventionalBiomeTags.OCEAN) || entry.isIn(ConventionalBiomeTags.DESERT) || entry.isIn(ConventionalBiomeTags.SNOWY_PLAINS) || entry.isIn(ConventionalBiomeTags.TAIGA);
		}, ((biomeSelectionContext, biomeModificationContext) -> {
			biomeModificationContext.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.SURFACE_STRUCTURES, OIL_WELL_PLACED_FEATURE.value());
		}));
	}
	
	public static <T extends FeatureConfig> Feature<T> registerFeature(Identifier id, Feature<T> feature) {
		return Registry.register(Registry.FEATURE, id, feature);
	}
	
	public static <T extends FeatureConfig, F extends Feature<T>> RegistryEntry<ConfiguredFeature<T, ?>> registerConfiguredFeature(Identifier id, F feature, T config) {
		return BuiltinRegistries.addCasted(BuiltinRegistries.CONFIGURED_FEATURE, id.toString(), new ConfiguredFeature<>(feature, config));
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier id, RegistryEntry<ConfiguredFeature<T, ?>> feature, PlacementModifier... mods) {
		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(RegistryEntry.upcast(feature), Arrays.asList(mods)));
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier id, RegistryEntry<ConfiguredFeature<T, ?>> feature, List<PlacementModifier> mods) {
		return BuiltinRegistries.add(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(RegistryEntry.upcast(feature), List.copyOf(mods)));
	}
	
}
