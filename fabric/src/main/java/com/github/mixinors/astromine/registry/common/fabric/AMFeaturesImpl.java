package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.common.world.feature.MeteorFeature;
import com.github.mixinors.astromine.registry.common.AMConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.function.Predicate;

import static com.github.mixinors.astromine.registry.common.AMFeatures.*;

public class AMFeaturesImpl {
	public static void postInit() {
		MeteorFeature meteor = new MeteorFeature(DefaultFeatureConfig.CODEC);
		ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> meteorStructure = meteor.configure(new DefaultFeatureConfig());
		
		FabricStructureBuilder.create(METEOR_ID, meteor)
				.step(GenerationStep.Feature.SURFACE_STRUCTURES)
				.defaultConfig(32, 8, 12345)
				.superflatFeature(meteorStructure)
				.register();
		
		BiomeModifications.create(ASTROMINE_BIOME_MODIFICATIONS)
				.add(ModificationPhase.ADDITIONS, overworldPredicate(), context -> {
					if (AMConfig.get().meteorGeneration) {
						context.getGenerationSettings().addStructure(METEOR_KEY);
					}
				})
				.add(ModificationPhase.ADDITIONS, oceanPredicate(), context -> {
					if (AMConfig.get().crudeOilWells) {
						context.getGenerationSettings().addFeature(GenerationStep.Feature.LAKES, CRUDE_OIL_KEY);
					}
				});
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}
	
	private static Predicate<BiomeSelectionContext> oceanPredicate() {
		return context -> context.getBiome().getCategory() == Biome.Category.OCEAN;
	}
}
