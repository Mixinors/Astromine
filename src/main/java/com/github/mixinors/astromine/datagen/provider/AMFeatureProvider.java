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

package com.github.mixinors.astromine.datagen.provider;

import com.github.mixinors.astromine.common.world.ore.OreDistribution;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFeatures;
import com.github.mixinors.astromine.registry.common.AMOres;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AMFeatureProvider extends FabricDynamicRegistryProvider {
	public AMFeatureProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		// Register configured features
		var asteroidConfigured = registerConfiguredFeature(entries, AMFeatures.ASTEROID_ORES_ID, AMFeatures.ASTEROID_ORES_FEATURE, DefaultFeatureConfig.INSTANCE);
		var oilWellConfigured = registerConfiguredFeature(entries, AMFeatures.OIL_WELL_ID, AMFeatures.OIL_WELL_FEATURE, DefaultFeatureConfig.INSTANCE);
		
		// Register placed features
		registerPlacedFeature(entries, AMFeatures.ASTEROID_ORES_ID, asteroidConfigured,
				RarityFilterPlacementModifier.of(20), SquarePlacementModifier.of(), BiomePlacementModifier.of());
		registerPlacedFeature(entries, AMFeatures.OIL_WELL_ID, oilWellConfigured,
				RarityFilterPlacementModifier.of(100), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
		
		// Ores
		OreDistribution.TIN.configure(AMOres.TIN_ORE_ID, AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get(), entries);
		OreDistribution.TIN_SMALL.configure(AMOres.TIN_ORE_SMALL_ID, AMBlocks.TIN_ORE.get(), AMBlocks.DEEPSLATE_TIN_ORE.get(), entries);
		OreDistribution.SILVER.configure(AMOres.SILVER_ORE_ID, AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get(), entries);
		OreDistribution.SILVER_LOWER.configure(AMOres.SILVER_ORE_LOWER_ID, AMBlocks.SILVER_ORE.get(), AMBlocks.DEEPSLATE_SILVER_ORE.get(), entries);
		OreDistribution.LEAD.configure(AMOres.LEAD_ORE_ID, AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get(), entries);
		OreDistribution.LEAD_SMALL.configure(AMOres.LEAD_ORE_SMALL_ID, AMBlocks.LEAD_ORE.get(), AMBlocks.DEEPSLATE_LEAD_ORE.get(), entries);
	}
	
	public static <T extends FeatureConfig, F extends Feature<T>> RegistryEntry<ConfiguredFeature<?, ?>> registerConfiguredFeature(Entries entries, Identifier id, F feature, T config) {
		return entries.add(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id), new ConfiguredFeature<>(feature, config));
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Entries entries, Identifier id, RegistryKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... mods) {
		return registerPlacedFeature(entries, id, resolveFeatureEntry(entries, feature), mods);
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Entries entries, Identifier id, RegistryKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> mods) {
		return registerPlacedFeature(entries, id, resolveFeatureEntry(entries, feature), mods);
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Entries entries, Identifier id, RegistryEntry<ConfiguredFeature<?, ?>> feature, PlacementModifier... mods) {
		return entries.add(RegistryKey.of(RegistryKeys.PLACED_FEATURE, id), new PlacedFeature(feature, Arrays.asList(mods)));
	}
	
	public static <T extends FeatureConfig> RegistryEntry<PlacedFeature> registerPlacedFeature(Entries entries, Identifier id, RegistryEntry<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> mods) {
		return entries.add(RegistryKey.of(RegistryKeys.PLACED_FEATURE, id), new PlacedFeature(feature, List.copyOf(mods)));
	}
	
	private static RegistryEntry.Reference<ConfiguredFeature<?, ?>> resolveFeatureEntry(Entries entries, RegistryKey<ConfiguredFeature<?, ?>> feature) {
		RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatureLookup = entries.getLookup(RegistryKeys.CONFIGURED_FEATURE);
		return configuredFeatureLookup.getOrThrow(feature);
	}
	
	@Override
	public String getName() {
		return "Astromine Features";
	}
}
