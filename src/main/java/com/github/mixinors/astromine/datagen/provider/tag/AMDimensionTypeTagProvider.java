package com.github.mixinors.astromine.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class AMDimensionTypeTagProvider extends FabricTagProvider.DynamicRegistryTagProvider<DimensionType> {
	public AMDimensionTypeTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator, Registry.DIMENSION_TYPE_KEY);
	}
	
	@Override
	protected void generateTags() {

	}
}