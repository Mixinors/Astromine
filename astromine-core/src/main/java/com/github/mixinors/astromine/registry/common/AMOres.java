package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.registry.AsteroidOreRegistry;
import com.github.mixinors.astromine.common.util.data.Range;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.function.Predicate;

public class AMOres {
	public static final Identifier ORES_ID = AMCommon.id("astromine_ores");

	public static final Identifier TIN_ORE_ID = AMCommon.id("tin_ore");
	public static final RegistryKey<PlacedFeature> TIN_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, TIN_ORE_ID);
	
	public static final Identifier COPPER_ORE_ID = AMCommon.id("copper_ore");
	public static final RegistryKey<PlacedFeature> COPPER_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, COPPER_ORE_ID);
	
	public static final Identifier SILVER_ORE_ID = AMCommon.id("silver_ore");
	public static final RegistryKey<PlacedFeature> SILVER_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, SILVER_ORE_ID);
	
	public static final Identifier LEAD_ORE_ID = AMCommon.id("lead_ore");
	public static final RegistryKey<PlacedFeature> LEAD_ORE_KEY = RegistryKey.of(Registry.PLACED_FEATURE_KEY, LEAD_ORE_ID);
	
	
	public static void init() {
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidCoalOreMinimumRange, AMConfig.get().asteroidCoalOreMaximumRange), Range.of(AMConfig.get().asteroidCoalOreMinimumSize, AMConfig.get().asteroidCoalOreMaximumSize), AMBlocks.ASTEROID_COAL_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidIronOreMinimumRange, AMConfig.get().asteroidIronOreMaximumRange), Range.of(AMConfig.get().asteroidIronOreMinimumSize, AMConfig.get().asteroidIronOreMaximumSize), AMBlocks.ASTEROID_IRON_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidGoldOreMinimumRange, AMConfig.get().asteroidGoldOreMaximumRange), Range.of(AMConfig.get().asteroidGoldOreMinimumSize, AMConfig.get().asteroidGoldOreMaximumSize), AMBlocks.ASTEROID_GOLD_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidCopperOreMinimumRange, AMConfig.get().asteroidCopperOreMaximumRange), Range.of(AMConfig.get().asteroidCopperOreMinimumSize, AMConfig.get().asteroidCopperOreMaximumSize), AMBlocks.ASTEROID_COPPER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidTinOreMinimumRange, AMConfig.get().asteroidTinOreMaximumRange), Range.of(AMConfig.get().asteroidTinOreMinimumSize, AMConfig.get().asteroidTinOreMaximumSize), AMBlocks.ASTEROID_TIN_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidSilverOreMinimumRange, AMConfig.get().asteroidSilverOreMaximumRange), Range.of(AMConfig.get().asteroidSilverOreMinimumSize, AMConfig.get().asteroidSilverOreMaximumSize), AMBlocks.ASTEROID_SILVER_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidLeadOreMinimumRange, AMConfig.get().asteroidLeadOreMaximumRange), Range.of(AMConfig.get().asteroidLeadOreMinimumSize, AMConfig.get().asteroidLeadOreMaximumSize), AMBlocks.ASTEROID_LEAD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidRedstoneOreMinimumRange, AMConfig.get().asteroidRedstoneOreMaximumRange), Range.of(AMConfig.get().asteroidRedstoneOreMinimumSize, AMConfig.get().asteroidRedstoneOreMaximumSize), AMBlocks.ASTEROID_REDSTONE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidLapisOreMinimumRange, AMConfig.get().asteroidLapisOreMaximumRange), Range.of(AMConfig.get().asteroidLapisOreMinimumSize, AMConfig.get().asteroidLapisOreMaximumSize), AMBlocks.ASTEROID_LAPIS_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidDiamondOreMinimumRange, AMConfig.get().asteroidDiamondOreMaximumRange), Range.of(AMConfig.get().asteroidDiamondOreMinimumSize, AMConfig.get().asteroidDiamondOreMaximumSize), AMBlocks.ASTEROID_DIAMOND_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidEmeraldOreMinimumRange, AMConfig.get().asteroidEmeraldOreMaximumRange), Range.of(AMConfig.get().asteroidEmeraldOreMinimumSize, AMConfig.get().asteroidEmeraldOreMaximumSize), AMBlocks.ASTEROID_EMERALD_ORE.get());
		
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidMetiteOreMinimumRange, AMConfig.get().asteroidMetiteOreMaximumRange), Range.of(AMConfig.get().asteroidMetiteOreMinimumSize, AMConfig.get().asteroidMetiteOreMaximumSize), AMBlocks.ASTEROID_METITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidAsteriteOreMinimumRange, AMConfig.get().asteroidAsteriteOreMaximumRange), Range.of(AMConfig.get().asteroidAsteriteOreMinimumSize, AMConfig.get().asteroidAsteriteOreMaximumSize), AMBlocks.ASTEROID_ASTERITE_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidStellumOreMinimumRange, AMConfig.get().asteroidStellumOreMaximumRange), Range.of(AMConfig.get().asteroidStellumOreMinimumSize, AMConfig.get().asteroidStellumOreMaximumSize), AMBlocks.ASTEROID_STELLUM_ORE.get());
		AsteroidOreRegistry.INSTANCE.register(Range.of(AMConfig.get().asteroidGalaxiumOreMinimumRange, AMConfig.get().asteroidGalaxiumOreMaximumRange), Range.of(AMConfig.get().asteroidGalaxiumOreMinimumSize, AMConfig.get().asteroidGalaxiumOreMaximumSize), AMBlocks.ASTEROID_GALAXIUM_ORE.get());
		
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
