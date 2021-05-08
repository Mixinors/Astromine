/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

import java.util.function.Predicate;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES = register(new AsteroidOreFeature(DefaultFeatureConfig.CODEC), ASTEROID_ORES_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ASTEROID_ORES_ID);
	
	public static final Identifier MOON_CRATER_ID = AMCommon.id("moon_crater");
	public static final Feature<DefaultFeatureConfig> MOON_CRATER = register(new MoonCraterFeature(DefaultFeatureConfig.CODEC), MOON_CRATER_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_CRATER_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_CRATER_ID);
	
	public static final Identifier MOON_LAKE_ID = AMCommon.id("moon_lake");
	public static final Feature<DefaultFeatureConfig> MOON_LAKE = register(new MoonLakeFeature(DefaultFeatureConfig.CODEC), MOON_LAKE_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_LAKE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_LAKE_ID);
	
	public static final Identifier MOON_ORE_ID = AMCommon.id("moon_ore");
	public static final Feature<DefaultFeatureConfig> MOON_ORE = register(new MoonOreFeature(DefaultFeatureConfig.CODEC), MOON_ORE_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_ORE_ID);
	
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	public static final StructurePieceType METEOR_STRUCTURE = register(MeteorGenerator::new, METEOR_ID);
	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, METEOR_ID);
	
	public static final Identifier CRUDE_OIL_ID = AMCommon.id("crude_oil");
	public static final Feature<DefaultFeatureConfig> CRUDE_OIL = register(new CrudeOilFeature(DefaultFeatureConfig.CODEC), CRUDE_OIL_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CRUDE_OIL_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, CRUDE_OIL_ID);
	
	public static final Identifier ASTROMINE_BIOME_MODIFICATIONS = AMCommon.id("biome_modifications");
	
	public static void init() {
		MeteorFeature meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> meteorStructure = meteor.configure(new DefaultFeatureConfig());
		FabricStructureBuilder.create(METEOR_ID, meteor)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES)
				.defaultConfig(32, 8, 12345)
				.superflatFeature(meteorStructure)
				.register();
		
		BiomeModifications.create(ASTROMINE_BIOME_MODIFICATIONS)
				.add(ModificationPhase.ADDITIONS, overworldPredicate(), context -> {
					if (AMConfig.get().meteorGeneration) {
						context.getGenerationSettings().addStructure(METEOR_KEY);
					}
				})
				.add(ModificationPhase.ADDITIONS, oceanPredicate(), context -> {
					if (AMConfig.get().crudeOilWells) {
						context.getGenerationSettings().addFeature(GenerationStep.Feature.LAKES, CRUDE_OIL_KEY);
					}
				});
	}
	
	public static <T extends FeatureConfig> Feature<T> register(Feature<T> feature, Identifier id) {
		return Registry.register(Registry.FEATURE, id, feature);
	}

	public static StructurePieceType register(StructurePieceType pieceType, Identifier id) {
		return Registry.register(Registry.STRUCTURE_PIECE, id, pieceType);
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}
	
	private static Predicate<BiomeSelectionContext> oceanPredicate() {
		return context -> context.getBiome().getCategory() == Biome.Category.OCEAN;
	}
}
