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

import java.util.HashSet;
import java.util.Set;

import com.github.mixinors.astromine.AMCommon;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;

public class AMBiomes {
	private static final Set<RegistryKey<?>> KEYS = new HashSet<>();
	
	public static final Identifier ASTEROID_BELT_ID = AMCommon.id("asteroid_belt");
	public static final RegistryKey<Biome> ASTEROID_BELT_KEY = register(Registry.BIOME_KEY, ASTEROID_BELT_ID);

	private static Biome createAsteroidBelt() {
		// We specify what entities spawn and what features generate in the biome.
		// Aside from some structures, trees, rocks, plants and
		//   custom entities, these are mostly the same for each biome.
		// Vanilla configured features for biomes are defined in DefaultBiomeFeatures.
		
		var spawnSettings = new SpawnSettings.Builder()
				.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(AMEntityTypes.SPACE_SLIME.get(), 10, 3, 8))
				.build();
		
		var generationSettings = new GenerationSettings.Builder()
				.feature(GenerationStep.Feature.UNDERGROUND_ORES, AMFeatures.ASTEROID_ORES_PLACED_FEATURE)
				.build();

		return (new Biome.Builder())
			.precipitation(Biome.Precipitation.RAIN)
			.category(Biome.Category.NONE)
			.temperature(0F)
			.downfall(0F)
			.effects((new BiomeEffects.Builder())
				.waterColor(0x3f76e4)
				.waterFogColor(0x050533)
				.fogColor(0xc0d8ff)
				.skyColor(0x77adff)
				.build())
			.spawnSettings(spawnSettings)
			.generationSettings(generationSettings)
			.build();
	}

	public static void init() {
		Registry.register(BuiltinRegistries.BIOME, ASTEROID_BELT_KEY.getValue(), createAsteroidBelt());
	}

	public static <T> RegistryKey<T> register(RegistryKey<Registry<T>> registry, Identifier identifier) {
		var key = RegistryKey.of(registry, identifier);
		KEYS.add(key);
		return key;
	}

	public static boolean isAstromine(RegistryKey<?> key) {
		return KEYS.contains(key);
	}
}
