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
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class AstromineFoundationsFeatures extends AstromineFeatures {
	public static final Identifier FOUNDATIONS_FEATURES_ID = AstromineCommon.identifier("foundations_features");
	public static final Identifier METEOR_ID = AstromineCommon.identifier("meteor");
	public static final StructurePieceType METEOR_STRUCTURE = register(MeteorGenerator::new, METEOR_ID);
	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, METEOR_ID);

	public static final Identifier CRUDE_OIL_ID = AstromineCommon.identifier("crude_oil");
	public static final Feature<DefaultFeatureConfig> CRUDE_OIL = register(new CrudeOilFeature(DefaultFeatureConfig.CODEC), CRUDE_OIL_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> CRUDE_OIL_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, CRUDE_OIL_ID);

	@SuppressWarnings("deprecation")
	public static void initialize() {
		BiomeModification modification = BiomeModifications.create(FOUNDATIONS_FEATURES_ID);

		if (AstromineConfig.get().meteorGeneration) {
			MeteorFeature structure = new MeteorFeature(DefaultFeatureConfig.CODEC);
			FabricStructureBuilder.create(METEOR_ID, structure)
				.step(GenerationStep.Feature.RAW_GENERATION)
				.defaultConfig(new StructureConfig(32, 8, 12345))
				.superflatFeature(structure.configure(DefaultFeatureConfig.INSTANCE))
				.register();

			modification.add(ModificationPhase.ADDITIONS, context -> {
				Biome biome = context.getBiome();
				return (AstromineConfig.get().netherMeteorGeneration || biome.getCategory() != Biome.Category.NETHER) && (AstromineConfig.get().endMeteorGeneration || biome.getCategory() != Biome.Category.THEEND);
			}, context -> {
				context.getGenerationSettings().addStructure(METEOR_KEY);
			});
		}

		if (AstromineConfig.get().crudeOilWells) {
			modification.add(ModificationPhase.ADDITIONS, context -> {
				Biome biome = context.getBiome();
				return (biome.getCategory() == Biome.Category.OCEAN && AstromineConfig.get().oceanicCrudeOilWells) || (biome.getCategory() == Biome.Category.DESERT && AstromineConfig.get().desertCrudeOilWells);
			}, context -> {
				context.getGenerationSettings().addFeature(GenerationStep.Feature.LAKES, CRUDE_OIL_KEY);
			});
		}
	}
}
