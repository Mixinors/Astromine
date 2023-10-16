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
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class AMFeatures {
	public static final Identifier ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES_FEATURE = register(ASTEROID_ORES_ID, new AsteroidOreFeature(DefaultFeatureConfig.CODEC));
	
	public static final Identifier OIL_WELL_ID = AMCommon.id("oil_well");
	public static final Feature<DefaultFeatureConfig> OIL_WELL_FEATURE = register(OIL_WELL_ID, new OilWellFeature(DefaultFeatureConfig.CODEC));
	
	public static void init() {
		BiomeModifications.create(OIL_WELL_ID).add(ModificationPhase.ADDITIONS, (biomeSelectionContext) -> {
			var entry = biomeSelectionContext.getBiomeRegistryEntry();
			
			return entry.isIn(ConventionalBiomeTags.OCEAN) || entry.isIn(ConventionalBiomeTags.DESERT) || entry.isIn(ConventionalBiomeTags.SNOWY_PLAINS) || entry.isIn(ConventionalBiomeTags.TAIGA);
		}, ((biomeSelectionContext, biomeModificationContext) -> {
			biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, OIL_WELL_ID));
		}));
	}
	
	public static <T extends FeatureConfig> Feature<T> register(Identifier id, Feature<T> feature) {
		return Registry.register(Registries.FEATURE, id, feature);
	}
}
