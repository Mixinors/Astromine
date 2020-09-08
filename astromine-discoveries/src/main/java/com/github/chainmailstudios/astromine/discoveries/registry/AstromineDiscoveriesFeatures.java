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

package com.github.chainmailstudios.astromine.discoveries.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.AsteroidOreFeature;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.MoonCraterFeature;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.MoonLakeFeature;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.MoonOreFeature;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;

public class AstromineDiscoveriesFeatures extends AstromineFeatures {
	public static final Identifier ASTEROID_ORES_ID = AstromineCommon.identifier("asteroid_ores");
	public static final Feature<DefaultFeatureConfig> ASTEROID_ORES = register(new AsteroidOreFeature(DefaultFeatureConfig.CODEC), ASTEROID_ORES_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ASTEROID_ORES_ID);

	public static final Identifier MOON_CRATER_ID = AstromineCommon.identifier("moon_crater");
	public static final Feature<DefaultFeatureConfig> MOON_CRATER = register(new MoonCraterFeature(DefaultFeatureConfig.CODEC), MOON_CRATER_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_CRATER_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_CRATER_ID);

	public static final Identifier MOON_LAKE_ID = AstromineCommon.identifier("moon_lake");
	public static final Feature<DefaultFeatureConfig> MOON_LAKE = register(new MoonLakeFeature(DefaultFeatureConfig.CODEC), MOON_LAKE_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_LAKE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_LAKE_ID);

	public static final Identifier MOON_ORE_ID = AstromineCommon.identifier("moon_ore");
	public static final Feature<DefaultFeatureConfig> MOON_ORE = register(new MoonOreFeature(DefaultFeatureConfig.CODEC), MOON_ORE_ID);
	public static final RegistryKey<ConfiguredFeature<?, ?>> MOON_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, MOON_ORE_ID);

	public static void initialize() {

	}
}
