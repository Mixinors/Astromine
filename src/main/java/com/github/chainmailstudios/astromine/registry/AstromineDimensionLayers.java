package com.github.chainmailstudios.astromine.registry;

import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.common.entity.placer.SpaceEntityPlacer;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;

public class AstromineDimensionLayers {
	public static void initialize() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, AstromineDimensionTypes.SPACE_REGISTRY_KEY, AstromineConfig.get().overworldTravelYLevel, DimensionType.OVERWORLD_REGISTRY_KEY, SpaceEntityPlacer.TO_PLANET);
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.TOP, AstromineDimensionTypes.OVERWORLD_REGISTRY_KEY, AstromineConfig.get().spaceTravelYLevel, AstromineDimensionTypes.SPACE_REGISTRY_KEY, SpaceEntityPlacer.TO_SPACE);
	}
}
