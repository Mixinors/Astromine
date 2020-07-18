package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.dimension.EarthSpaceDimensionType;
import com.github.chainmailstudios.astromine.common.entity.placer.SpaceEntityPlacer;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import net.minecraft.world.dimension.DimensionType;

public class AstromineDimensionLayers {
	public static void initialize() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, EarthSpaceDimensionType.EARTH_SPACE_REGISTRY_KEY, -58, DimensionType.OVERWORLD_REGISTRY_KEY, SpaceEntityPlacer.TO_PLANET);
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.TOP, DimensionType.OVERWORLD_REGISTRY_KEY, 1024, EarthSpaceDimensionType.EARTH_SPACE_REGISTRY_KEY, SpaceEntityPlacer.TO_SPACE);
	}
}
