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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.registry.common.AMEntityTypes;
import com.github.mixinors.astromine.registry.common.AMFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AMBiomeProvider extends FabricDynamicRegistryProvider {
	public AMBiomeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();
	
	public static final Identifier ASTEROID_BELT_ID = AMCommon.id("asteroid_belt");
	public static final RegistryKey<Biome> ASTEROID_BELT_KEY = register(RegistryKeys.BIOME, ASTEROID_BELT_ID);
	
	public static final Identifier MOON_LIGHT_SIDE_ID = AMCommon.id("moon_light_side");
	public static final RegistryKey<Biome> MOON_LIGHT_SIDE_KEY = register(RegistryKeys.BIOME, MOON_LIGHT_SIDE_ID);
	
	public static final Identifier MOON_DARK_SIDE_ID = AMCommon.id("moon_dark_side");
	public static final RegistryKey<Biome> MOON_DARK_SIDE_KEY = register(RegistryKeys.BIOME, MOON_DARK_SIDE_ID);
	
	public static final Identifier MOON_CRATER_FIELD_ID = AMCommon.id("moon_crater_field");
	public static final RegistryKey<Biome> MOON_CRATER_FIELD_KEY = register(RegistryKeys.BIOME, MOON_CRATER_FIELD_ID);
	
	public static final Identifier ROCKET_ID = AMCommon.id("rocket");
	public static final RegistryKey<Biome> ROCKET_KEY = register(RegistryKeys.BIOME, ROCKET_ID);
	
	// We specify what entities spawn and what features generate in the biome.
	// Aside from some structures, trees, rocks, plants and
	// custom entities, these are mostly the same for each biome.
	// Vanilla configured features for biomes are defined in DefaultBiomeFeatures.
	
	// Applies for all create<X> methods.
	private static Biome createAsteroidBelt(Entries entries) {
		var spawnSettings = new SpawnSettings.Builder()
				.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var asteroidFeatureEntry = entries.ref(RegistryKey.of(RegistryKeys.PLACED_FEATURE, AMFeatures.ASTEROID_ORES_ID));
		var generationSettings = new GenerationSettings.Builder()
				.feature(GenerationStep.Feature.UNDERGROUND_ORES, asteroidFeatureEntry)
				.build();
		
		return (new Biome.Builder())
				.precipitation(false)
				.temperature(0.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.spawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createMoonLightSide() {
		var spawnSettings = new SpawnSettings.Builder()
				.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = new GenerationSettings.Builder()
				.build();
		
		return (new Biome.Builder())
				.precipitation(false)
				.temperature(0.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.spawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	// TODO: Add Dark versions of mobs to this biome!
	private static Biome createMoonDarkSide() {
		var spawnSettings = new SpawnSettings.Builder()
				.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = new GenerationSettings.Builder()
				.build();
		
		return (new Biome.Builder())
				.precipitation(false)
				.temperature(0.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.spawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createMoonCraterField() {
		var spawnSettings = new SpawnSettings.Builder()
				.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = new GenerationSettings.Builder()
				.build();
		
		return (new Biome.Builder())
				.precipitation(false)
				.temperature(0.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.spawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createRocket() {
		var spawnSettings = new SpawnSettings.Builder().build();
		
		// TODO: Generate a rocket feature
		var generationSettings = new GenerationSettings.Builder().build();
		
		return (new Biome.Builder())
				.precipitation(false)
				.temperature(0.0F)
				.downfall(0.0F)
				.effects((new BiomeEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.spawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		entries.add(ASTEROID_BELT_KEY, createAsteroidBelt(entries));
		
		entries.add(MOON_LIGHT_SIDE_KEY, createMoonLightSide());
		entries.add(MOON_DARK_SIDE_KEY, createMoonDarkSide());
		entries.add(MOON_CRATER_FIELD_KEY, createMoonCraterField());
		
		entries.add(ROCKET_KEY, createRocket());
	}
	
	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		var key = RegistryKey.of(registry, identifier);
		
		KEYS.add(key);
		
		return key;
	}
	
	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}
	
	@Override
	public String getName() {
		return "Astromine Biomes";
	}
}
