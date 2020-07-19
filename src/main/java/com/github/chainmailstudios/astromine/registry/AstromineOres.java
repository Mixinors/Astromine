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
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_COAL_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_IRON_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_GOLD_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_COPPER_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_TIN_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 32), AstromineBlocks.ASTEROID_REDSTONE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 32), AstromineBlocks.ASTEROID_LAPIS_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 16), AstromineBlocks.ASTEROID_DIAMOND_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 16), AstromineBlocks.ASTEROID_EMERALD_ORE);

		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 48), AstromineBlocks.ASTEROID_METITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 32), AstromineBlocks.ASTEROID_ASTERITE_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 8), AstromineBlocks.ASTEROID_STELLUM_ORE);
		AsteroidOreRegistry.INSTANCE.register(Range.of(0, 6), AstromineBlocks.ASTEROID_GALAXIUM_ORE);

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
					new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.COPPER_ORE.getDefaultState(), 14)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(
					new RangeDecoratorConfig(20, 0, 0, 64))));
		}
		if (AstromineConfig.get().overworldTinOre) {
			biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Feature.ORE.configure(
					new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, AstromineBlocks.TIN_ORE.getDefaultState(), 11)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(
					new RangeDecoratorConfig(10, 0, 0, 64))));
		}
	}
}
