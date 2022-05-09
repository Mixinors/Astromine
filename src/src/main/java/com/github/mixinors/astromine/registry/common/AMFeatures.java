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
import com.github.mixinors.astromine.common.world.feature.AsteroidOreFeature;
import com.github.mixinors.astromine.common.world.feature.OilWellFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorGenerator;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.List;
import java.util.function.Supplier;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES_FEATURE = registerFeature(ASTEROID_ORES_ID, new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> ASTEROID_ORES_CONFIGURED_FEATURE = registerConfiguredFeature(ASTEROID_ORES_ID, ASTEROID_ORES_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> ASTEROID_ORES_PLACED_FEATURE = registerPlacedFeature(ASTEROID_ORES_ID, ASTEROID_ORES_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(25), SquarePlacementModifier.of(), BiomePlacementModifier.of());
	
	public static final Identifier OIL_WELL_ID = AMCommon.id("oil_well");
	
	public static final Feature<DefaultFeatureConfig> OIL_WELL_FEATURE = registerFeature(OIL_WELL_ID, new OilWellFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> OIL_WELL_CONFIGURED_FEATURE = registerConfiguredFeature(OIL_WELL_ID, OIL_WELL_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> OIL_WELL_PLACED_FEATURE = registerPlacedFeature(OIL_WELL_ID, OIL_WELL_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(5), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
	
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	
	public static final StructureFeature<DefaultFeatureConfig> METEOR_STRUCTURE_FEATURE = registerStructureFeature(METEOR_ID, new MeteorFeature(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES);
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
	
	public static void init() {
		BiomeModifications.create(OIL_WELL_ID).add(ModificationPhase.ADDITIONS, (biomeSelectionContext) -> {
			var entry = biomeSelectionContext.getBiomeRegistryEntry();
			
			return entry.isIn(ConventionalBiomeTags.OCEAN) || entry.isIn(ConventionalBiomeTags.DESERT) || entry.isIn(ConventionalBiomeTags.SNOWY_PLAINS) || entry.isIn(ConventionalBiomeTags.TAIGA);
		}, ((biomeSelectionContext, biomeModificationContext) -> {
			biomeModificationContext.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.SURFACE_STRUCTURES, OIL_WELL_PLACED_FEATURE.value());
		}));
	}
	
	public static <T extends FeatureConfig> Feature<T> registerFeature(Identifier id, Feature<T> feature) {
		return Feature.register(id.toString(), feature);
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
	
	public static <T extends FeatureConfig> StructureFeature<T> registerStructureFeature(Identifier id, StructureFeature<T> feature, GenerationStep.Feature step) {
		return StructureFeature.register(id.toString(), feature, step);
	}
	
	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece(Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).register(id, pieceType);
	}
}
