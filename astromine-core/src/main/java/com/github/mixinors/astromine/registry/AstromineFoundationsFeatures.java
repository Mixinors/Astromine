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

package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.AstromineCommon;
import com.github.mixinors.astromine.common.world.feature.CrudeOilFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorFeature;
import com.github.mixinors.astromine.common.world.feature.MeteorGenerator;
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

public class AstromineFoundationsFeatures extends AstromineFeatures {
	public static final Identifier ASTROMINE_FOUNDATIONS_MODIFICATIONS_FEATURES = AstromineFoundationsCommon.identifier("foundations_odifications_features");
	public static final Identifier METEOR_ID = AstromineCommon.identifier("meteor");
	public static final StructurePieceType METEOR_STRUCTURE = register(MeteorGenerator::new, METEOR_ID);
	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, METEOR_ID);

	public static final Identifier CRUDE_OIL_ID = AstromineCommon.identifier("crude_oil");
	public static final Feature<DefaultFeatureConfig> CRUDE_OIL = register(new CrudeOilFeature(DefaultFeatureConfig.CODEC), CRUDE_OIL_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CRUDE_OIL_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, CRUDE_OIL_ID);

	public static void initialize() {
		MeteorFeature meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> meteorStructure = meteor.configure(new DefaultFeatureConfig());
		FabricStructureBuilder.create(METEOR_ID, meteor)
			.step(GenerationStep.Feature.SURFACE_STRUCTURES)
			.defaultConfig(32, 8, 12345)
			.superflatFeature(meteorStructure)
			.register();

		BiomeModifications.create(ASTROMINE_FOUNDATIONS_MODIFICATIONS_FEATURES)
			.add(ModificationPhase.ADDITIONS, overworldPredicate(), context -> {
				if (AstromineConfig.get().meteorGeneration) {
					context.getGenerationSettings().addStructure(METEOR_KEY);
				}
			})
			.add(ModificationPhase.ADDITIONS, oceanPredicate(), context -> {
				if (AstromineConfig.get().crudeOilWells) {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.LAKES, CRUDE_OIL_KEY);
				}
			});
	}

	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}

	private static Predicate<BiomeSelectionContext> oceanPredicate() {
		return context -> context.getBiome().getCategory() == Biome.Category.OCEAN;
	}
}
