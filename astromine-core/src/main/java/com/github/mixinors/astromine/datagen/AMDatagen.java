package com.github.mixinors.astromine.datagen;

import com.github.mixinors.astromine.datagen.family.AMBlockFamilies;
import com.github.mixinors.astromine.datagen.family.MaterialFamilies;
import com.github.mixinors.astromine.datagen.provider.AMModelProvider;
import com.github.mixinors.astromine.datagen.provider.AMRecipeProvider;
import com.github.mixinors.astromine.datagen.provider.AMTagProviders;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AMDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		AMBlockFamilies.init();
		MaterialFamilies.init();
		dataGenerator.addProvider(AMModelProvider::new);
		dataGenerator.addProvider(AMRecipeProvider::new);
		dataGenerator.addProvider(AMTagProviders.AMBlockTagProvider::new);
		dataGenerator.addProvider(AMTagProviders.AMItemTagProvider::new);
		dataGenerator.addProvider(AMTagProviders.AMFluidTagProvider::new);
	}
}
