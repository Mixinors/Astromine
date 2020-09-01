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

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import com.github.chainmailstudios.astromine.foundations.AstromineFoundationsCommon;
import me.shedaniel.cloth.api.dynamic.registry.v1.BiomesRegistry;
import me.shedaniel.cloth.api.dynamic.registry.v1.DynamicRegistryCallback;

public class AstromineFoundationsOres {
	public static final Identifier TIN_ORE_ID = AstromineFoundationsCommon.identifier("tin_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, TIN_ORE_ID);

	public static final Identifier COPPER_ORE_ID = AstromineFoundationsCommon.identifier("copper_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, COPPER_ORE_ID);

	public static final Identifier SILVER_ORE_ID = AstromineFoundationsCommon.identifier("silver_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, SILVER_ORE_ID);

	public static final Identifier LEAD_ORE_ID = AstromineFoundationsCommon.identifier("lead_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, LEAD_ORE_ID);

	public static void initialize() {
		DynamicRegistryCallback.callback(Registry.BIOME_KEY).register((manager, key, biome) -> {
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
				if (AstromineFoundationsConfig.get().overworldTinOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
				if (AstromineFoundationsConfig.get().overworldCopperOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, COPPER_ORE_KEY);
				if (AstromineFoundationsConfig.get().overworldSilverOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
				if (AstromineFoundationsConfig.get().overworldLeadOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
			}
		});
	}
}
