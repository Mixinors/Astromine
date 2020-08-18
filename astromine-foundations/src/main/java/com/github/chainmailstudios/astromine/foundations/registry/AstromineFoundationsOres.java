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

import com.github.chainmailstudios.astromine.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.github.chainmailstudios.astromine.foundations.AstromineFoundations;
import me.shedaniel.cloth.api.dynamic.registry.v1.BiomesRegistry;
import me.shedaniel.cloth.api.dynamic.registry.v1.DynamicRegistryCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class AstromineFoundationsOres {
	public static final Identifier TIN_ORE_ID = AstromineFoundations.identifier("tin_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, TIN_ORE_ID);

	public static final Identifier COPPER_ORE_ID = AstromineFoundations.identifier("copper_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, COPPER_ORE_ID);

	public static final Identifier SILVER_ORE_ID = AstromineFoundations.identifier("silver_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, SILVER_ORE_ID);

	public static final Identifier LEAD_ORE_ID = AstromineFoundations.identifier("lead_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, LEAD_ORE_ID);

	public static void initialize() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidCoalOreMinimumRange, AstromineFoundationsConfig.get().asteroidCoalOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidCoalOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidCoalOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidIronOreMinimumRange, AstromineFoundationsConfig.get().asteroidIronOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidIronOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidIronOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidGoldOreMinimumRange, AstromineFoundationsConfig.get().asteroidGoldOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidGoldOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidGoldOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidCopperOreMinimumRange, AstromineFoundationsConfig.get().asteroidCopperOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidCopperOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidCopperOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_COPPER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidTinOreMinimumRange, AstromineFoundationsConfig.get().asteroidTinOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidTinOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidTinOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_TIN_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidSilverOreMinimumRange, AstromineFoundationsConfig.get().asteroidSilverOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidSilverOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidSilverOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_SILVER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidLeadOreMinimumRange, AstromineFoundationsConfig.get().asteroidLeadOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidLeadOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidLeadOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_LEAD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidRedstoneOreMinimumRange, AstromineFoundationsConfig.get().asteroidRedstoneOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidRedstoneOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidRedstoneOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidLapisOreMinimumRange, AstromineFoundationsConfig.get().asteroidLapisOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidLapisOreMinimumSize, AstromineFoundationsConfig
			.get().asteroidLapisOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_LAPIS_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidDiamondOreMinimumRange, AstromineFoundationsConfig.get().asteroidDiamondOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidDiamondOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidDiamondOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidEmeraldOreMinimumRange, AstromineFoundationsConfig.get().asteroidEmeraldOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidEmeraldOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidEmeraldOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidMetiteOreMinimumRange, AstromineFoundationsConfig.get().asteroidMetiteOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidMetiteOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidMetiteOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidAsteriteOreMinimumRange, AstromineFoundationsConfig.get().asteroidAsteriteOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidAsteriteOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidAsteriteOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_ASTERITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidStellumOreMinimumRange, AstromineFoundationsConfig.get().asteroidStellumOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidStellumOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidStellumOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_STELLUM_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineFoundationsConfig.get().asteroidGalaxiumOreMinimumRange, AstromineFoundationsConfig.get().asteroidGalaxiumOreMaximumRange), Range.of(AstromineFoundationsConfig.get().asteroidGalaxiumOreMinimumSize,
			AstromineFoundationsConfig.get().asteroidGalaxiumOreMaximumSize), AstromineFoundationsBlocks.ASTEROID_GALAXIUM_ORE);

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
