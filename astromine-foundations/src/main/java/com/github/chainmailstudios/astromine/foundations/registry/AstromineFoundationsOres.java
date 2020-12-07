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
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.Predicate;

public class AstromineFoundationsOres {
	public static final Identifier FOUNDATIONS_ORES_ID = AstromineCommon.identifier("foundations_ores");
	public static final Identifier TIN_ORE_ID = AstromineCommon.identifier("tin_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, TIN_ORE_ID);

	public static final Identifier COPPER_ORE_ID = AstromineCommon.identifier("copper_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, COPPER_ORE_ID);

	public static final Identifier LEAD_ORE_ID = AstromineCommon.identifier("lead_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, LEAD_ORE_ID);

	@SuppressWarnings("deprecation")
	public static void initialize() {
		BiomeModification modification = BiomeModifications.create(FOUNDATIONS_ORES_ID);
		Predicate<BiomeSelectionContext> baseOverworldOreContext = context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
		modification.add(ModificationPhase.ADDITIONS, baseOverworldOreContext.and(context -> AstromineFoundationsConfig.get().overworldTinOre), context -> {
			context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
		});
		modification.add(ModificationPhase.ADDITIONS, baseOverworldOreContext.and(context -> AstromineFoundationsConfig.get().overworldCopperOre), context -> {
			context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, COPPER_ORE_KEY);
		});
		modification.add(ModificationPhase.ADDITIONS, baseOverworldOreContext.and(context -> AstromineFoundationsConfig.get().overworldLeadOre), context -> {
			context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
		});
	}
}
