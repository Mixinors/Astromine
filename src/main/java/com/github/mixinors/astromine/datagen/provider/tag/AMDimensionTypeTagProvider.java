package com.github.mixinors.astromine.datagen.provider.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.dimension.DimensionType;

import java.util.concurrent.CompletableFuture;

public class AMDimensionTypeTagProvider extends FabricTagProvider<DimensionType> {
	public AMDimensionTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, RegistryKeys.DIMENSION_TYPE, completableFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup lookup) {

	}
}