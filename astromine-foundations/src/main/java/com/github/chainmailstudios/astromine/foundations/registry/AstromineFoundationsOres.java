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

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.foundations.AstromineFoundationsCommon;
import me.shedaniel.cloth.api.dynamic.registry.v1.BiomesRegistry;
import me.shedaniel.cloth.api.dynamic.registry.v1.DynamicRegistryCallback;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class AstromineFoundationsOres {
	public static final ResourceLocation TIN_ORE_ID = AstromineCommon.identifier("tin_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, TIN_ORE_ID);

	public static final ResourceLocation COPPER_ORE_ID = AstromineCommon.identifier("copper_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, COPPER_ORE_ID);

	public static final ResourceLocation LEAD_ORE_ID = AstromineCommon.identifier("lead_ore");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, LEAD_ORE_ID);

	public static void initialize() {
		DynamicRegistryCallback.callback(Registry.BIOME_REGISTRY).register((manager, key, biome) -> {
			if (biome.getBiomeCategory() != Biome.BiomeCategory.NETHER && biome.getBiomeCategory() != Biome.BiomeCategory.THEEND) {
				if (AstromineFoundationsConfig.get().overworldTinOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Decoration.UNDERGROUND_ORES, TIN_ORE_KEY);
				if (AstromineFoundationsConfig.get().overworldCopperOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Decoration.UNDERGROUND_ORES, COPPER_ORE_KEY);
				if (AstromineFoundationsConfig.get().overworldLeadOre)
					BiomesRegistry.registerFeature(manager, biome, GenerationStep.Decoration.UNDERGROUND_ORES, LEAD_ORE_KEY);
			}
		});
	}
}
