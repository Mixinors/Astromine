package com.github.chainmailstudios.astromine.discoveries.registry;

import com.github.chainmailstudios.astromine.discoveries.common.entity.placer.SpaceEntityPlacer;
import com.github.chainmailstudios.astromine.common.registry.DimensionLayerRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineDimensionLayers;
import net.minecraft.world.World;

public class AstromineDiscoveriesDimensionLayers extends AstromineDimensionLayers {
	public static void initialize() {
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.BOTTOM, AstromineDiscoveriesDimensions.EARTH_SPACE_WORLD, AstromineConfig.get().overworldTravelYLevel, World.OVERWORLD, SpaceEntityPlacer.TO_PLANET);
		DimensionLayerRegistry.INSTANCE.register(DimensionLayerRegistry.Type.TOP, World.OVERWORLD, AstromineConfig.get().spaceTravelYLevel, AstromineDiscoveriesDimensions.EARTH_SPACE_WORLD, SpaceEntityPlacer.TO_SPACE);
	}
}
