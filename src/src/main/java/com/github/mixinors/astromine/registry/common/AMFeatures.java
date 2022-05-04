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
import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.world.feature.AsteroidOreFeature;
import com.github.mixinors.astromine.common.world.feature.CrudeOilFeature;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	public static final RegistrySupplier<Feature<DefaultFeatureConfig>> ASTEROID_ORES_FEATURE = registerFeature(ASTEROID_ORES_ID, () -> new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> ASTEROID_ORES_CONFIGURED_FEATURE = registerConfiguredFeature(ASTEROID_ORES_ID, ASTEROID_ORES_FEATURE.get(), DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> ASTEROID_ORES_PLACED_FEATURE = registerPlacedFeature(ASTEROID_ORES_ID, ASTEROID_ORES_CONFIGURED_FEATURE, BiomePlacementModifier.of());
	public static final RegistryKey<PlacedFeature> ASTEROID_ORES_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, ASTEROID_ORES_ID);
	
	// TODO: 1.18.2 structures
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = null;
//	public static final Identifier METEOR_ID = AMCommon.id("meteor");
//	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
//	public static final StructureFeature<DefaultFeatureConfig> METEOR_STRUCTURE_FEATURE = registerStructureFeature(METEOR_ID, new MeteorFeature(DefaultFeatureConfig.CODEC));
//	public static final ConfiguredStructureFeature<DefaultFeatureConfig, ?> METEOR_CONFIGURED_STRUCTURE_FEATURE = registerConfiguredStructureFeature(METEOR_ID, METEOR_STRUCTURE_FEATURE.configure(DefaultFeatureConfig.INSTANCE));
//	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, METEOR_ID);
	
	public static final Identifier CRUDE_OIL_ID = AMCommon.id("crude_oil");
	public static final RegistrySupplier<Feature<DefaultFeatureConfig>> CRUDE_OIL_FEATURE = registerFeature(CRUDE_OIL_ID, () -> new CrudeOilFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> CRUDE_OIL_CONFIGURED_FEATURE = registerConfiguredFeature(CRUDE_OIL_ID, CRUDE_OIL_FEATURE.get(), DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> CRUDE_OIL_PLACED_FEATURE = registerPlacedFeature(CRUDE_OIL_ID, CRUDE_OIL_CONFIGURED_FEATURE, BiomePlacementModifier.of());
	public static final RegistryKey<PlacedFeature> CRUDE_OIL_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, CRUDE_OIL_ID);
	
	public static final Identifier ASTROMINE_BIOME_MODIFICATIONS = AMCommon.id("biome_modifications");
	
	public static void init() {
		// TODO: Asteroid ores

		BiomeModifications.create(ASTROMINE_BIOME_MODIFICATIONS)
				.add(ModificationPhase.ADDITIONS, overworldPredicate(), context -> {
					if (AMConfig.get().world.meteorGeneration) {
//						context.getGenerationSettings().addStructure(METEOR_KEY);
					}
				})
				.add(ModificationPhase.ADDITIONS, oceanPredicate(), context -> {
					if (AMConfig.get().world.crudeOilWellsGeneration) {
						context.getGenerationSettings().addFeature(GenerationStep.Feature.FLUID_SPRINGS, CRUDE_OIL_KEY);
					}
				});
	}
	
	public static <T extends FeatureConfig> RegistrySupplier<Feature<T>> registerFeature(Identifier id, Supplier<Feature<T>> feature) {
		return AMCommon.registry(Registry.FEATURE_KEY).register(id, feature);
	}

	public static <T extends FeatureConfig, F extends Feature<T>> RegistryEntry<ConfiguredFeature<T, ?>> registerConfiguredFeature(Identifier id, F feature, T config) {
		return ConfiguredFeatures.register(id.toString(), feature, config);
	}

	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier id, RegistryEntry<ConfiguredFeature<T, ?>> feature, PlacementModifier... mods) {
		return PlacedFeatures.register(id.toString(), feature, mods);
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Identifier id, RegistryEntry<ConfiguredFeature<T, ?>> feature, List<PlacementModifier> mods) {
		return PlacedFeatures.register(id.toString(), feature, mods);
	}

	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece(Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).register(id, pieceType);
	}

//	public static <T extends FeatureConfig> StructureFeature<T> registerStructureFeature(Identifier id, StructureFeature<T> feature) {
//		return FabricStructureBuilder.create(id, feature)
//				.step(GenerationStep.Feature.SURFACE_STRUCTURES)
//				.defaultConfig(32, 8, 12345)
//				.enableSuperflat()
//				.register();
//	}

	public static <T extends FeatureConfig> ConfiguredStructureFeature<T, ?> registerConfiguredStructureFeature(Identifier id, ConfiguredStructureFeature<T, ?> feature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, id, feature);
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> Biome.getCategory(context.getBiomeRegistryEntry()) != Biome.Category.NETHER && Biome.getCategory(context.getBiomeRegistryEntry()) != Biome.Category.THEEND && Biome.getCategory(context.getBiomeRegistryEntry()) != Biome.Category.NONE;
	}
	
	private static Predicate<BiomeSelectionContext> oceanPredicate() {
		return context -> Biome.getCategory(context.getBiomeRegistryEntry()) == Biome.Category.OCEAN;
	}
}
