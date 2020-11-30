/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.foundations.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.common.world.feature.CrudeOilFeature;
import com.github.chainmailstudios.astromine.foundations.common.world.feature.MeteorFeature;
import com.github.chainmailstudios.astromine.foundations.common.world.feature.MeteorGenerator;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class AstromineFoundationsFeatures extends AstromineFeatures {
	public static final ResourceLocation FOUNDATIONS_FEATURES_ID = AstromineCommon.identifier("foundations_features");
	public static final ResourceLocation METEOR_ID = AstromineCommon.identifier("meteor");
	public static final StructurePieceType METEOR_STRUCTURE = register(MeteorGenerator::new, METEOR_ID);
	public static final ResourceKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, METEOR_ID);

	public static final ResourceLocation CRUDE_OIL_ID = AstromineCommon.identifier("crude_oil");
	public static final Feature<NoneFeatureConfiguration> CRUDE_OIL = register(new CrudeOilFeature(NoneFeatureConfiguration.CODEC), CRUDE_OIL_ID);
	public static final ResourceKey<ConfiguredFeature<?, ?>> CRUDE_OIL_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, CRUDE_OIL_ID);

	@SuppressWarnings("deprecation")
	public static void initialize() {
		BiomeModification modification = BiomeModifications.create(FOUNDATIONS_FEATURES_ID);
		
		if (AstromineConfig.get().meteorGeneration) {
			MeteorFeature structure = new MeteorFeature(NoneFeatureConfiguration.CODEC);
			FabricStructureBuilder.create(METEOR_ID, structure)
				.step(GenerationStep.Decoration.RAW_GENERATION)
				.defaultConfig(new StructureFeatureConfiguration(32, 8, 12345))
				.superflatFeature(structure.configured(new NoneFeatureConfiguration()))
				.register();

			modification.add(ModificationPhase.ADDITIONS, context -> {
				Biome biome = context.getBiome();
				return (AstromineConfig.get().netherMeteorGeneration || biome.getBiomeCategory() != Biome.BiomeCategory.NETHER) && (AstromineConfig.get().endMeteorGeneration || biome.getBiomeCategory() != Biome.BiomeCategory.THEEND);
			}, context -> {
				context.getGenerationSettings().addStructure(METEOR_KEY);
			});
		}

		if (AstromineConfig.get().crudeOilWells) {
			modification.add(ModificationPhase.ADDITIONS, context -> {
				Biome biome = context.getBiome();
				return (biome.getBiomeCategory() == Biome.BiomeCategory.OCEAN && AstromineConfig.get().oceanicCrudeOilWells) || (biome.getBiomeCategory() == Biome.BiomeCategory.DESERT && AstromineConfig.get().desertCrudeOilWells);
			}, context -> {
				context.getGenerationSettings().addFeature(GenerationStep.Decoration.LAKES, CRUDE_OIL_KEY);
			});
		}
	}
}
