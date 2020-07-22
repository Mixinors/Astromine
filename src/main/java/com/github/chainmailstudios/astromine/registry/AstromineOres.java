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
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCoalOreMinimumRange, AstromineConfig.get().asteroidCoalOreMaximumRange), AstromineConfig.get().asteroidCoalOreMinimumSize, AstromineConfig.get().asteroidCoalOreMaximumSize, AstromineBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidIronOreMinimumRange, AstromineConfig.get().asteroidIronOreMaximumRange), AstromineConfig.get().asteroidIronOreMinimumSize, AstromineConfig.get().asteroidIronOreMaximumSize, AstromineBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGoldOreMinimumRange, AstromineConfig.get().asteroidGoldOreMaximumRange), AstromineConfig.get().asteroidGoldOreMinimumSize, AstromineConfig.get().asteroidGoldOreMaximumSize, AstromineBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidCopperOreMinimumRange, AstromineConfig.get().asteroidCopperOreMaximumRange), AstromineConfig.get().asteroidCopperOreMinimumSize, AstromineConfig.get().asteroidCopperOreMaximumSize, AstromineBlocks.ASTEROID_COPPER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidTinOreMinimumRange, AstromineConfig.get().asteroidTinOreMaximumRange), AstromineConfig.get().asteroidTinOreMinimumSize, AstromineConfig.get().asteroidTinOreMaximumSize, AstromineBlocks.ASTEROID_TIN_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidRedstoneOreMinimumRange, AstromineConfig.get().asteroidRedstoneOreMaximumRange), AstromineConfig.get().asteroidRedstoneOreMinimumSize, AstromineConfig.get().asteroidRedstoneOreMaximumSize, AstromineBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidLapisOreMinimumRange, AstromineConfig.get().asteroidLapisOreMaximumRange), AstromineConfig.get().asteroidLapisOreMinimumSize, AstromineConfig.get().asteroidLapisOreMaximumSize, AstromineBlocks.ASTEROID_LAPIS_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidDiamondOreMinimumRange, AstromineConfig.get().asteroidDiamondOreMaximumRange), AstromineConfig.get().asteroidDiamondOreMinimumSize, AstromineConfig.get().asteroidDiamondOreMaximumSize, AstromineBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidEmeraldOreMinimumRange, AstromineConfig.get().asteroidEmeraldOreMaximumRange), AstromineConfig.get().asteroidEmeraldOreMinimumSize, AstromineConfig.get().asteroidEmeraldOreMaximumSize, AstromineBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidMetiteOreMinimumRange, AstromineConfig.get().asteroidMetiteOreMaximumRange), AstromineConfig.get().asteroidMetiteOreMinimumSize, AstromineConfig.get().asteroidMetiteOreMaximumSize, AstromineBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidAsteriteOreMinimumRange, AstromineConfig.get().asteroidAsteriteOreMaximumRange), AstromineConfig.get().asteroidAsteriteOreMinimumSize, AstromineConfig.get().asteroidAsteriteOreMaximumSize, AstromineBlocks.ASTEROID_ASTERITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidStellumOreMinimumRange, AstromineConfig.get().asteroidStellumOreMaximumRange), AstromineConfig.get().asteroidStellumOreMinimumSize, AstromineConfig.get().asteroidStellumOreMaximumSize, AstromineBlocks.ASTEROID_STELLUM_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(AstromineConfig.get().asteroidGalaxiumOreMinimumRange, AstromineConfig.get().asteroidGalaxiumOreMaximumRange), AstromineConfig.get().asteroidGalaxiumOreMinimumSize, AstromineConfig.get().asteroidGalaxiumOreMaximumSize, AstromineBlocks.ASTEROID_GALAXIUM_ORE);

		for (Biome biome : Registry.BIOME) {
			if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
				addOresToBiome(biome);
			}
		}

		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> addOresToBiome(biome));
	}

	public static void addOresToBiome(Biome biome) {
		if (AstromineConfig.get().overworldCopperOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
					new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.COPPER_ORE.getDefaultState(), AstromineConfig.get().overworldCopperOreMaximumBlocks)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(
					new RangeDecoratorConfig(AstromineConfig.get().overworldCopperOreMaximumVeins, AstromineConfig.get().overworldCopperOreBottomOffset, AstromineConfig.get().overworldCopperOreTopOffset, AstromineConfig.get().overworldCopperOreMaximumLayer))));
		}
		if (AstromineConfig.get().overworldTinOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
					new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.TIN_ORE.getDefaultState(), AstromineConfig.get().overworldTinOreMaximumBlocks)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(
					new RangeDecoratorConfig(AstromineConfig.get().overworldTinOreMaximumVeins, AstromineConfig.get().overworldTinOreBottomOffset, AstromineConfig.get().overworldTinOreTopOffset, AstromineConfig.get().overworldTinOreMaximumLayer))));
		}
	}
}
