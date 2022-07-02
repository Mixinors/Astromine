package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.registry.common.AMTagKeys;
import com.github.mixinors.astromine.registry.common.AMWorlds;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class AMDimensionTypeTagProvider extends FabricTagProvider.DynamicRegistryTagProvider<DimensionType> {
	public AMDimensionTypeTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator, Registry.DIMENSION_TYPE_KEY, "dimension_type", "Dimension Type Tags");
	}
	
	@Override
	protected void generateTags() {
		var isSpaceTag = getOrCreateTagBuilder(AMTagKeys.DimensionTypeTags.IS_SPACE);
		
		isSpaceTag.add(AMWorlds.EARTH_ORBIT_TYPE_KEY);
		
		var isAtmosphericTag = getOrCreateTagBuilder(AMTagKeys.DimensionTypeTags.IS_ATMOSPHERIC);
	}
}
