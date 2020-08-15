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

package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.world.feature.AsteroidOreFeature;
import com.github.chainmailstudios.astromine.common.world.feature.MeteorFeature;
import com.github.chainmailstudios.astromine.common.world.feature.MeteorGenerator;
import com.github.chainmailstudios.astromine.common.world.feature.MoonCraterFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import me.shedaniel.cloth.api.dynamic.registry.v1.DynamicRegistryCallback;
import net.earthcomputer.libstructure.LibStructure;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.*;

import java.util.List;
import java.util.function.Supplier;

public class AstromineFeatures {
	public static final Identifier ASTEROID_ORES_ID = AstromineCommon.identifier("asteroid_ores");
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES = register(new AsteroidOreFeature(DefaultFeatureConfig.CODEC), ASTEROID_ORES_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ASTEROID_ORES_ID);

	public static final Identifier MOON_CRATER_ID = AstromineCommon.identifier("moon_crater");
	public static final Feature<DefaultFeatureConfig> MOON_CRATER = register(new MoonCraterFeature(DefaultFeatureConfig.CODEC), MOON_CRATER_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_CRATER_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_CRATER_ID);

	public static final Identifier TIN_ORE_ID = AstromineCommon.identifier("tin_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, TIN_ORE_ID);

	public static final Identifier COPPER_ORE_ID = AstromineCommon.identifier("copper_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, COPPER_ORE_ID);

	public static final Identifier SILVER_ORE_ID = AstromineCommon.identifier("silver_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, SILVER_ORE_ID);

	public static final Identifier LEAD_ORE_ID = AstromineCommon.identifier("lead_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, LEAD_ORE_ID);

	public static final Identifier METEOR_ID = AstromineCommon.identifier("meteor");
	public static final StructurePieceType METEOR_STRUCTURE = register(MeteorGenerator::new, METEOR_ID);
	public static final RegistryKey<ConfiguredStructureFeature<?, ?>> METEOR_KEY = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN, METEOR_ID);

	public static <T extends FeatureConfig> Feature<T> register(Feature<T> feature, Identifier id) {
		return Registry.register(Registry.FEATURE, id, feature);
	}

	public static StructurePieceType register(StructurePieceType pieceType, Identifier id) {
		return Registry.register(Registry.STRUCTURE_PIECE, id, pieceType);
	}

	public static void initialize() {
		// initialize meteor structure/feature
		MeteorFeature meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> meteorStructure = meteor.configure(new DefaultFeatureConfig());
		LibStructure.registerStructure(METEOR_ID, meteor, GenerationStep.Feature.RAW_GENERATION, new StructureConfig(32, 8, 12345), meteorStructure);

		DynamicRegistryCallback.callback(Registry.BIOME_KEY).register((manager, key, biome) -> {
			registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
			registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, COPPER_ORE_KEY);
			registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
			registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
			registerStructure(manager, biome, () -> meteorStructure);
		});
	}

	public static void registerFeature(DynamicRegistryManager manager, Biome biome, GenerationStep.Feature generationStep, RegistryKey<ConfiguredFeature<?, ?>> configuredFeature) {
		registerFeature(manager, biome, generationStep, () -> manager.get(Registry.CONFIGURED_FEATURE_WORLDGEN).get(configuredFeature));
	}

	public static void registerFeature(DynamicRegistryManager manager, Biome biome, GenerationStep.Feature generationStep, Supplier<ConfiguredFeature<?, ?>> configuredFeature) {
		GenerationSettings generationSettings = biome.getGenerationSettings();

		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = generationSettings.features;
		if (features instanceof ImmutableList) {
			features = generationSettings.features = Lists.newArrayList(features);
		}

		for (int i = features.size(); i <= generationStep.ordinal(); i++) {
			features.add(Lists.newArrayList());
		}

		List<Supplier<ConfiguredFeature<?, ?>>> suppliers = features.get(generationStep.ordinal());
		if (suppliers instanceof ImmutableList) {
			features.set(generationStep.ordinal(), suppliers = Lists.newArrayList(suppliers));
		}

		suppliers.add(configuredFeature);
	}

	public static void registerStructure(DynamicRegistryManager manager, Biome biome, RegistryKey<ConfiguredStructureFeature<?, ?>> configuredStructure) {
		registerStructure(manager, biome, () -> manager.get(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN).get(configuredStructure));
	}

	public static void registerStructure(DynamicRegistryManager manager, Biome biome, Supplier<ConfiguredStructureFeature<?, ?>> configuredStructure) {
		GenerationSettings generationSettings = biome.getGenerationSettings();

		List<Supplier<ConfiguredStructureFeature<?, ?>>> structures = generationSettings.structureFeatures;
		if (structures instanceof ImmutableList) {
			structures = generationSettings.structureFeatures = Lists.newArrayList(structures);
		}

		structures.add(configuredStructure);
	}
}
