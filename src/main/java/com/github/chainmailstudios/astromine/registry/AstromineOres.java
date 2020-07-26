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

import com.github.chainmailstudios.astromine.client.registry.AsteroidOreRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class AstromineOres {
	public static void initialize() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCoalOreMinimumRange, AstromineConfig.get().asteroidCoalOreMaximumRange), AstromineBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidIronOreMinimumRange, AstromineConfig.get().asteroidIronOreMaximumRange), AstromineBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGoldOreMinimumRange, AstromineConfig.get().asteroidGoldOreMaximumRange), AstromineBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCopperOreMinimumRange, AstromineConfig.get().asteroidCopperOreMaximumRange), AstromineBlocks.ASTEROID_COPPER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidTinOreMinimumRange, AstromineConfig.get().asteroidTinOreMaximumRange), AstromineBlocks.ASTEROID_TIN_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidSilverOreMinimumRange, AstromineConfig.get().asteroidSilverOreMaximumRange), AstromineBlocks.ASTEROID_SILVER_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidRedstoneOreMinimumRange, AstromineConfig.get().asteroidRedstoneOreMaximumRange), AstromineBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidLapisOreMinimumRange, AstromineConfig.get().asteroidLapisOreMaximumRange), AstromineBlocks.ASTEROID_LAPIS_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidDiamondOreMinimumRange, AstromineConfig.get().asteroidDiamondOreMaximumRange), AstromineBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidEmeraldOreMinimumRange, AstromineConfig.get().asteroidEmeraldOreMaximumRange), AstromineBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidMetiteOreMinimumRange, AstromineConfig.get().asteroidMetiteOreMaximumRange), AstromineBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidAsteriteOreMinimumRange, AstromineConfig.get().asteroidAsteriteOreMaximumRange), AstromineBlocks.ASTEROID_ASTERITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidStellumOreMinimumRange, AstromineConfig.get().asteroidStellumOreMaximumRange), AstromineBlocks.ASTEROID_STELLUM_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGalaxiumOreMinimumRange, AstromineConfig.get().asteroidGalaxiumOreMaximumRange), AstromineBlocks.ASTEROID_GALAXIUM_ORE);

		for (Biome biome : Registry.BIOME) {
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
				addOresToBiome(biome);
			}
		}

		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> addOresToBiome(biome));
	}

	public static void addOresToBiome(Biome biome) {
		if (AstromineConfig.get().overworldCopperOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
				Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.COPPER_ORE.getDefaultState(), AstromineConfig.get().overworldCopperOreMaximumBlocks))
					.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldCopperOreMaximumVeins, AstromineConfig.get().overworldCopperOreBottomOffset,
						AstromineConfig.get().overworldCopperOreTopOffset, AstromineConfig.get().overworldCopperOreMaximumLayer))));
		}
		if (AstromineConfig.get().overworldTinOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
				Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.TIN_ORE.getDefaultState(), AstromineConfig.get().overworldTinOreMaximumBlocks))
					.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldTinOreMaximumVeins, AstromineConfig.get().overworldTinOreBottomOffset,
						AstromineConfig.get().overworldTinOreTopOffset, AstromineConfig.get().overworldTinOreMaximumLayer))));
		}
		if (AstromineConfig.get().overworldSilverOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES,
				Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.SILVER_ORE.getDefaultState(), AstromineConfig.get().overworldSilverOreMaximumBlocks))
					.createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(AstromineConfig.get().overworldSilverOreMaximumVeins, AstromineConfig.get().overworldSilverOreBottomOffset,
						AstromineConfig.get().overworldSilverOreTopOffset, AstromineConfig.get().overworldSilverOreMaximumLayer))));
		}
	}
}
