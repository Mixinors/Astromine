package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.AstromineCommon;
import com.github.mixinors.astromine.client.registry.AsteroidOreRegistry;
import com.github.mixinors.astromine.common.utilities.data.Range;
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

public class AstromineOres {
	public static final Identifier ASTROMINE_FOUNDATIONS_MODIFICATIONS_ORES = AstromineCommon.identifier("foundations_odifications_ores");
	public static final Identifier TIN_ORE_ID = AstromineCommon.identifier("tin_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, TIN_ORE_ID);
	
	public static final Identifier COPPER_ORE_ID = AstromineCommon.identifier("copper_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> COPPER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, COPPER_ORE_ID);
	
	public static final Identifier SILVER_ORE_ID = AstromineCommon.identifier("silver_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, SILVER_ORE_ID);
	
	public static final Identifier LEAD_ORE_ID = AstromineCommon.identifier("lead_ore");
	public static final RegistryKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, LEAD_ORE_ID);
	
	
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
		
		BiomeModifications.create(ASTROMINE_FOUNDATIONS_MODIFICATIONS_ORES)
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AstromineConfig.get().overworldTinOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, TIN_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AstromineConfig.get().overworldCopperOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, COPPER_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AstromineConfig.get().overworldSilverOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, SILVER_ORE_KEY);
				})
				.add(ModificationPhase.ADDITIONS, overworldPredicate().and(context -> AstromineConfig.get().overworldLeadOre), context -> {
					context.getGenerationSettings().addFeature(GenerationStep.Feature.UNDERGROUND_ORES, LEAD_ORE_KEY);
				});
	}
	
	private static Predicate<BiomeSelectionContext> overworldPredicate() {
		return context -> context.getBiome().getCategory() != Biome.Category.NETHER && context.getBiome().getCategory() != Biome.Category.THEEND;
	}
}
