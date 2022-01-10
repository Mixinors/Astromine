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

import java.util.function.Predicate;
import java.util.function.Supplier;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.world.feature.AsteroidOreFeature;
import com.github.mixinors.astromine.common.world.feature.CrudeOilFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorGenerator;
import dev.architectury.registry.registries.RegistrySupplier;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	public static final RegistrySupplier<Feature<DefaultFeatureConfig>> ASTEROID_ORES = registerFeature(ASTEROID_ORES_ID, () -> new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, ASTEROID_ORES_ID);
	
	//
	//
	
	public static final Identifier METEOR_ID = AMCommon.id("meteor");
	public static final RegistrySupplier<StructurePieceType> METEOR_STRUCTURE = registerStructurePiece(METEOR_ID, () -> (StructurePieceType.Simple) MeteorGenerator::new);
	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, METEOR_ID);
	
	public static final Identifier CRUDE_OIL_ID = AMCommon.id("crude_oil");
	public static final RegistrySupplier<Feature<DefaultFeatureConfig>> CRUDE_OIL = registerFeature(CRUDE_OIL_ID, () -> new CrudeOilFeature(DefaultFeatureConfig.CODEC));
	public static final RegistryKey<PlacedFeature> CRUDE_OIL_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, CRUDE_OIL_ID);
	
	public static final Identifier ASTROMINE_BIOME_MODIFICATIONS = AMCommon.id("biome_modifications");
	
	public static void init() {
		var meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		var meteorStructure = meteor.configure(new DefaultFeatureConfig());
		FabricStructureBuilder.create(METEOR_ID, meteor)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES)
				.defaultConfig(32, 8, 12345)
				// .superflatFeature(meteorStructure)
				.register();

		/* TODO: Fix stuff
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
		 */
	}
	
	public static <T extends FeatureConfig> RegistrySupplier<Feature<T>> registerFeature(Identifier id, Supplier<Feature<T>> feature) {
		return AMCommon.registry(Registry.FEATURE_KEY).register(id, feature);
	}

	public static <T extends StructurePieceType> RegistrySupplier<T> registerStructurePiece( Identifier id, Supplier<T> pieceType) {
		return AMCommon.registry(Registry.STRUCTURE_PIECE_KEY).register(id, pieceType);
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}
	
	private static Predicate<BiomeSelectionContext> oceanPredicate() {
		return context -> context.getBiome().getCategory() == Biome.Category.OCEAN;
	}
}
