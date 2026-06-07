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
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMBiomes {
	private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, AMCommon.MOD_ID);
	
	private static final Set<ResourceKey<?>> KEYS = new HashSet<>();
	
	public static final ResourceLocation ASTEROID_BELT_ID = AMCommon.id("asteroid_belt");
	public static final ResourceKey<Biome> ASTEROID_BELT_KEY = register(Registries.BIOME, ASTEROID_BELT_ID);
	
	public static final ResourceLocation MOON_LIGHT_SIDE_ID = AMCommon.id("moon_light_side");
	public static final ResourceKey<Biome> MOON_LIGHT_SIDE_KEY = register(Registries.BIOME, MOON_LIGHT_SIDE_ID);
	
	public static final ResourceLocation MOON_DARK_SIDE_ID = AMCommon.id("moon_dark_side");
	public static final ResourceKey<Biome> MOON_DARK_SIDE_KEY = register(Registries.BIOME, MOON_DARK_SIDE_ID);
	
	public static final ResourceLocation MOON_CRATER_FIELD_ID = AMCommon.id("moon_crater_field");
	public static final ResourceKey<Biome> MOON_CRATER_FIELD_KEY = register(Registries.BIOME, MOON_CRATER_FIELD_ID);
	
	public static final ResourceLocation ROCKET_ID = AMCommon.id("rocket");
	public static final ResourceKey<Biome> ROCKET_KEY = register(Registries.BIOME, ROCKET_ID);
	
	// We specify what entities spawn and what features generate in the biome.
	// Aside from some structures, trees, rocks, plants and
	// custom entities, these are mostly the same for each biome.
	// Vanilla configured features for biomes are defined in DefaultBiomeFeatures.
	
	// Applies for all create<X> methods.
	private static Biome createAsteroidBelt() {
		var spawnSettings = new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = new BiomeGenerationSettings.PlainBuilder()
				.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AMFeatures.ASTEROID_ORES_PLACED_FEATURE)
				.build();
		
		return (new Biome.BiomeBuilder())
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createMoonLightSide() {
		var spawnSettings = new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = BiomeGenerationSettings.EMPTY;
		
		return (new Biome.BiomeBuilder())
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createMoonDarkSide() {
		var spawnSettings = new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = BiomeGenerationSettings.EMPTY;
		
		return (new Biome.BiomeBuilder())
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createMoonCraterField() {
		var spawnSettings = new MobSpawnSettings.Builder()
				.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = BiomeGenerationSettings.EMPTY;
		
		return (new Biome.BiomeBuilder())
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	private static Biome createRocket() {
		var spawnSettings = new MobSpawnSettings.Builder().build();
		
		var generationSettings = BiomeGenerationSettings.EMPTY;
		
		return (new Biome.BiomeBuilder())
				.temperature(0.0F)
				.downfall(0.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.waterColor(0x3F76E4)
						.waterFogColor(0x050533)
						.fogColor(0xC0D8FF)
						.skyColor(0x77ADFF)
						.build())
				.mobSpawnSettings(spawnSettings)
				.generationSettings(generationSettings)
				.build();
	}
	
	public static void init() {
		BIOMES.register("asteroid_belt", AMBiomes::createAsteroidBelt);
		BIOMES.register("moon_light_side", AMBiomes::createMoonLightSide);
		BIOMES.register("moon_dark_side", AMBiomes::createMoonDarkSide);
		BIOMES.register("moon_crater_field", AMBiomes::createMoonCraterField);
		BIOMES.register("rocket", AMBiomes::createRocket);
		BIOMES.register(AMCommon.modEventBus());
	}
	
	public static <T> ResourceKey<T> register(ResourceKey<? extends Registry<T>> registry, ResourceLocation identifier) {
		var key = ResourceKey.create(registry, identifier);
		
		KEYS.add(key);
		
		return key;
	}
	
	public static boolean isAstromine(ResourceKey<?> key) {
		return KEYS.contains(key);
	}
}
