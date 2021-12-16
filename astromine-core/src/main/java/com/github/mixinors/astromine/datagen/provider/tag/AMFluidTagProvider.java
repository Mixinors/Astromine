package com.github.mixinors.astromine.datagen.provider.tag;

import com.github.mixinors.astromine.datagen.AMDatagen;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class AMFluidTagProvider extends FabricTagProvider.FluidTagProvider {

	public AMFluidTagProvider(FabricDataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void generateTags() {
		AMDatagen.FLUIDS.forEach((fluid) -> {
			FabricTagBuilder<Fluid> tagBuilder = getOrCreateTagBuilder(AMDatagen.createCommonFluidTag(Registry.FLUID.getId(fluid.getStill()).getPath()));
			tagBuilder.add(fluid.getStill(), fluid.getFlowing());
		});
	}
}
