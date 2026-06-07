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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.Arrays;
import java.util.List;

public class AMFeatures {
	private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, AMCommon.MOD_ID);
	
	public static final ResourceLocation ASTEROID_ORES_ID = AMCommon.id("asteroid_ores");
	
	public static final Feature<NoneFeatureConfiguration> ASTEROID_ORES_FEATURE = registerFeature(ASTEROID_ORES_ID, new AsteroidOreFeature(NoneFeatureConfiguration.CODEC));
	public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> ASTEROID_ORES_CONFIGURED_FEATURE = registerConfiguredFeature(ASTEROID_ORES_ID, ASTEROID_ORES_FEATURE, NoneFeatureConfiguration.INSTANCE);
	public static final Holder<PlacedFeature> ASTEROID_ORES_PLACED_FEATURE = registerPlacedFeature(ASTEROID_ORES_ID, ASTEROID_ORES_CONFIGURED_FEATURE, RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), BiomeFilter.biome());
	
	public static final ResourceLocation OIL_WELL_ID = AMCommon.id("oil_well");
	
	public static final Feature<NoneFeatureConfiguration> OIL_WELL_FEATURE = registerFeature(OIL_WELL_ID, new OilWellFeature(NoneFeatureConfiguration.CODEC));
	public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> OIL_WELL_CONFIGURED_FEATURE = registerConfiguredFeature(OIL_WELL_ID, OIL_WELL_FEATURE, NoneFeatureConfiguration.INSTANCE);
	public static final Holder<PlacedFeature> OIL_WELL_PLACED_FEATURE = registerPlacedFeature(OIL_WELL_ID, OIL_WELL_CONFIGURED_FEATURE, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
	
	public static void init() {
		FEATURES.register(AMCommon.modEventBus());
	}
	
	public static <T extends FeatureConfiguration> Feature<T> registerFeature(ResourceLocation id, Feature<T> feature) {
		FEATURES.register(id.getPath(), () -> feature);
		return feature;
	}
	
	public static <T extends FeatureConfiguration, F extends Feature<T>> Holder<ConfiguredFeature<T, ?>> registerConfiguredFeature(ResourceLocation id, F feature, T config) {
		return Holder.direct(new ConfiguredFeature<>(feature, config));
	}
	
	public static <T extends FeatureConfiguration> Holder<PlacedFeature> registerPlacedFeature(ResourceLocation id, Holder<ConfiguredFeature<T, ?>> feature, PlacementModifier... mods) {
		return Holder.direct(new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) (Holder<?>) feature, Arrays.asList(mods)));
	}
	
	public static <T extends FeatureConfiguration> Holder<PlacedFeature> registerPlacedFeature(ResourceLocation id, Holder<ConfiguredFeature<T, ?>> feature, List<PlacementModifier> mods) {
		return Holder.direct(new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) (Holder<?>) feature, List.copyOf(mods)));
	}
	
}
