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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.AsteroidOreFeature;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.MoonCraterFeature;
import com.github.chainmailstudios.astromine.discoveries.common.world.feature.MoonLakeFeature;
import com.github.chainmailstudios.astromine.registry.AstromineFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AstromineDiscoveriesFeatures extends AstromineFeatures {
	public static final ResourceLocation ASTEROID_ORES_ID = AstromineCommon.identifier("asteroid_ores");
	public static final Feature<NoneFeatureConfiguration> ASTEROID_ORES = register(new AsteroidOreFeature(NoneFeatureConfiguration.CODEC), ASTEROID_ORES_ID);
	public static final ResourceKey<ConfiguredFeature<?, ?>> ASTEROID_ORES_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, ASTEROID_ORES_ID);

	public static final ResourceLocation MOON_CRATER_ID = AstromineCommon.identifier("moon_crater");
	public static final Feature<NoneFeatureConfiguration> MOON_CRATER = register(new MoonCraterFeature(NoneFeatureConfiguration.CODEC), MOON_CRATER_ID);
	public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_CRATER_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, MOON_CRATER_ID);

	public static final ResourceLocation MOON_LAKE_ID = AstromineCommon.identifier("moon_lake");
	public static final Feature<NoneFeatureConfiguration> MOON_LAKE = register(new MoonLakeFeature(NoneFeatureConfiguration.CODEC), MOON_LAKE_ID);
	public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_LAKE_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, MOON_LAKE_ID);

	public static void initialize() {

	}
}
