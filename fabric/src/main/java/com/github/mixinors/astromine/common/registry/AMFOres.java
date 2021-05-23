package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.registry.common.AMConfig;
import com.github.mixinors.astromine.registry.common.AMOres;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;

import java.util.function.Predicate;

public class AMFOres extends AMOres {
	public static void init() {
		BiomeModifications.create(ORES_ID)
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldTinOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldCopperOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, COPPER_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldSilverOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AMConfig.get().overworldLeadOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
				});
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}
}
