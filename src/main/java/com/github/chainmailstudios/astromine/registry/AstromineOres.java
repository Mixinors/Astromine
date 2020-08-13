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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;

import com.github.chainmailstudios.astromine.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;

public class AstromineOres {
	public static void initialize() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCoalOreMinimumRange, AstromineConfig.get().asteroidCoalOreMaximumRange), Range.of(AstromineConfig.get().asteroidCoalOreMinimumSize, AstromineConfig.get().asteroidCoalOreMaximumSize), AstromineBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidIronOreMinimumRange, AstromineConfig.get().asteroidIronOreMaximumRange), Range.of(AstromineConfig.get().asteroidIronOreMinimumSize, AstromineConfig.get().asteroidIronOreMaximumSize), AstromineBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGoldOreMinimumRange, AstromineConfig.get().asteroidGoldOreMaximumRange), Range.of(AstromineConfig.get().asteroidGoldOreMinimumSize, AstromineConfig.get().asteroidGoldOreMaximumSize), AstromineBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCopperOreMinimumRange, AstromineConfig.get().asteroidCopperOreMaximumRange), Range.of(AstromineConfig.get().asteroidCopperOreMinimumSize, AstromineConfig.get().asteroidCopperOreMaximumSize), AstromineBlocks.ASTEROID_COPPER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidTinOreMinimumRange, AstromineConfig.get().asteroidTinOreMaximumRange), Range.of(AstromineConfig.get().asteroidTinOreMinimumSize, AstromineConfig.get().asteroidTinOreMaximumSize), AstromineBlocks.ASTEROID_TIN_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidSilverOreMinimumRange, AstromineConfig.get().asteroidSilverOreMaximumRange), Range.of(AstromineConfig.get().asteroidSilverOreMinimumSize, AstromineConfig.get().asteroidSilverOreMaximumSize), AstromineBlocks.ASTEROID_SILVER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidLeadOreMinimumRange, AstromineConfig.get().asteroidLeadOreMaximumRange), Range.of(AstromineConfig.get().asteroidLeadOreMinimumSize, AstromineConfig.get().asteroidLeadOreMaximumSize), AstromineBlocks.ASTEROID_LEAD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidRedstoneOreMinimumRange, AstromineConfig.get().asteroidRedstoneOreMaximumRange), Range.of(AstromineConfig.get().asteroidRedstoneOreMinimumSize, AstromineConfig.get().asteroidRedstoneOreMaximumSize), AstromineBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidLapisOreMinimumRange, AstromineConfig.get().asteroidLapisOreMaximumRange), Range.of(AstromineConfig.get().asteroidLapisOreMinimumSize, AstromineConfig.get().asteroidLapisOreMaximumSize), AstromineBlocks.ASTEROID_LAPIS_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidDiamondOreMinimumRange, AstromineConfig.get().asteroidDiamondOreMaximumRange), Range.of(AstromineConfig.get().asteroidDiamondOreMinimumSize, AstromineConfig.get().asteroidDiamondOreMaximumSize), AstromineBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidEmeraldOreMinimumRange, AstromineConfig.get().asteroidEmeraldOreMaximumRange), Range.of(AstromineConfig.get().asteroidEmeraldOreMinimumSize, AstromineConfig.get().asteroidEmeraldOreMaximumSize), AstromineBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidMetiteOreMinimumRange, AstromineConfig.get().asteroidMetiteOreMaximumRange), Range.of(AstromineConfig.get().asteroidMetiteOreMinimumSize, AstromineConfig.get().asteroidMetiteOreMaximumSize), AstromineBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidAsteriteOreMinimumRange, AstromineConfig.get().asteroidAsteriteOreMaximumRange), Range.of(AstromineConfig.get().asteroidAsteriteOreMinimumSize, AstromineConfig.get().asteroidAsteriteOreMaximumSize), AstromineBlocks.ASTEROID_ASTERITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidStellumOreMinimumRange, AstromineConfig.get().asteroidStellumOreMaximumRange), Range.of(AstromineConfig.get().asteroidStellumOreMinimumSize, AstromineConfig.get().asteroidStellumOreMaximumSize), AstromineBlocks.ASTEROID_STELLUM_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGalaxiumOreMinimumRange, AstromineConfig.get().asteroidGalaxiumOreMaximumRange), Range.of(AstromineConfig.get().asteroidGalaxiumOreMinimumSize, AstromineConfig.get().asteroidGalaxiumOreMaximumSize), AstromineBlocks.ASTEROID_GALAXIUM_ORE);

		for (Biome biome : BuiltinRegistries.BIOME) {
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
				addOresToBiome(biome);
			}
		}

		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register((i, identifier, biome) -> addOresToBiome(biome));
	}

	public static void addOresToBiome(Biome biome) {
		/*
		 * if (AstromineConfig.get().overworldCopperOre) {
		 * biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, AstromineBlocks.COPPER_ORE.getDefaultState(),
		 * AstromineConfig.get().overworldCopperOreMaximumBlocks)).decorate(
		 * Decorator.RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldCopperOreMaximumVeins, AstromineConfig.get().overworldCopperOreBottomOffset, AstromineConfig.get().overworldCopperOreTopOffset, AstromineConfig
		 * .get().overworldCopperOreMaximumLayer))));
		 * }
		 * if (AstromineConfig.get().overworldTinOre) {
		 * biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, AstromineBlocks.TIN_ORE.getDefaultState(),
		 * AstromineConfig.get().overworldTinOreMaximumBlocks)).decorate(
		 * Decorator.RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldTinOreMaximumVeins, AstromineConfig.get().overworldTinOreBottomOffset, AstromineConfig.get().overworldTinOreTopOffset,
		 * AstromineConfig.get().overworldTinOreMaximumLayer))));
		 * }
		 * if (AstromineConfig.get().overworldSilverOre) {
		 * biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, AstromineBlocks.SILVER_ORE.getDefaultState(),
		 * AstromineConfig.get().overworldSilverOreMaximumBlocks)).decorate(
		 * Decorator.RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldSilverOreMaximumVeins, AstromineConfig.get().overworldSilverOreBottomOffset, AstromineConfig.get().overworldSilverOreTopOffset, AstromineConfig
		 * .get().overworldSilverOreMaximumLayer))));
		 * }
		 * if (AstromineConfig.get().overworldLeadOre) {
		 * biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD, AstromineBlocks.LEAD_ORE.getDefaultState(),
		 * AstromineConfig.get().overworldLeadOreMaximumBlocks)).decorate(
		 * Decorator.RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldLeadOreMaximumVeins, AstromineConfig.get().overworldLeadOreBottomOffset, AstromineConfig.get().overworldLeadOreTopOffset, AstromineConfig
		 * .get().overworldLeadOreMaximumLayer))));
		 * }
		 */
	}
}
