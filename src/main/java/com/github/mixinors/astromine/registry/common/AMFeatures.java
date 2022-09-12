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
import com.github.mixinors.astromine.common.world.feature.*;
import com.github.mixinors.astromine.common.world.structure.CraterStructure;
import com.github.mixinors.astromine.common.world.structure.MeteorStructure;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES_FEATURE = registerFeature(ASTEROID_ORES_ID, new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> ASTEROID_ORES_CONFIGURED_FEATURE = registerConfiguredFeature(ASTEROID_ORES_ID, ASTEROID_ORES_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> ASTEROID_ORES_PLACED_FEATURE = registerPlacedFeature(ASTEROID_ORES_ID, ASTEROID_ORES_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(20), SquarePlacementModifier.of(), BiomePlacementModifier.of());
	
	public static final Identifier OIL_WELL_ID = AMCommon.id("oil_well");
	
	public static final Feature<DefaultFeatureConfig> OIL_WELL_FEATURE = registerFeature(OIL_WELL_ID, new OilWellFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryEntry<ConfiguredFeature<DefaultFeatureConfig, ?>> OIL_WELL_CONFIGURED_FEATURE = registerConfiguredFeature(OIL_WELL_ID, OIL_WELL_FEATURE, DefaultFeatureConfig.INSTANCE);
	public static final RegistryEntry<PlacedFeature> OIL_WELL_PLACED_FEATURE = registerPlacedFeature(OIL_WELL_ID, OIL_WELL_CONFIGURED_FEATURE, RarityFilterPlacementModifier.of(100), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
	
	public static final Identifier METEOR_ID = AMCommon.id("meteor");

	public static final RegistryEntry<Structure> METEOR_STRUCTURE_FEATURE = registerStructure(METEOR_ID, new MeteorStructure(createConfig(BiomeTags.IS_OVERWORLD, StructureTerrainAdaptation.NONE)));
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE_PIECE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
	
	public static final Identifier CRATER_ID = AMCommon.id("crater");
	
	public static final RegistryEntry<Structure> CRATER_STRUCTURE_FEATURE = registerStructure(CRATER_ID, new CraterStructure(createConfig(AMTagKeys.BiomeTags.IS_MOON, StructureTerrainAdaptation.NONE)));
	public static final RegistrySupplier<StructurePieceType> CRATER_STRUCTURE_PIECE = registerStructurePiece(CRATER_ID, () -> (StructurePieceType.Simple) CraterGenerator::new);
	
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
	
	public static <T extends FeatureConfig> RegistryEntry<Structure> registerStructure(Identifier id, Structure structure) {
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE, RegistryKey.of(Registry.STRUCTURE_KEY, id), structure);
	}
	
	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece(Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).register(id, pieceType);
	}
	
	private static Structure.Config createConfig(TagKey<Biome> biomeTag, Map<SpawnGroup, StructureSpawns> spawns, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
		return new Structure.Config(getOrCreateBiomeTag(biomeTag), spawns, featureStep, terrainAdaptation);
	}
	
	private static Structure.Config createConfig(TagKey<Biome> biomeTag, GenerationStep.Feature featureStep, StructureTerrainAdaptation terrainAdaptation) {
		return createConfig(biomeTag, Map.of(), featureStep, terrainAdaptation);
	}
	
	private static Structure.Config createConfig(TagKey<Biome> biomeTag, StructureTerrainAdaptation terrainAdaptation) {
		return createConfig(biomeTag, Map.of(), GenerationStep.Feature.SURFACE_STRUCTURES, terrainAdaptation);
	}
	
	private static RegistryEntry<Structure> register(RegistryKey<Structure> key, Structure structure) {
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE, key, structure);
	}
	
	private static RegistryEntryList<Biome> getOrCreateBiomeTag(TagKey<Biome> key) {
		return BuiltinRegistries.BIOME.getOrCreateEntryList(key);
	}
}
