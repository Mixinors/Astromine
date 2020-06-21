package com.github.chainmailstudios.astromine.registry;

import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;

public class AstromineDimensionLayers {
	public static void initialize() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, AstromineDimensionTypes.SPACE_REGISTRY_KEY, -58, DimensionType.OVERWORLD_REGISTRY_KEY);
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.TOP, AstromineDimensionTypes.OVERWORLD_REGISTRY_KEY, 1024, AstromineDimensionTypes.SPACE_REGISTRY_KEY);
	}
}
