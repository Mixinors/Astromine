package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import net.minecraft.world.dimension.DimensionType;

public class AstromineDimensionLayers {
	public static void initialize() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, AstromineDimensionTypes.REGISTRY_KEY, -58, DimensionType.OVERWORLD_REGISTRY_KEY);
	}
}
