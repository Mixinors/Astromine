package com.github.mixinors.astromine.registry.client.fabric;

import com.github.mixinors.astromine.client.model.EmptyUnbakedModel;
import com.github.mixinors.astromine.registry.client.AMClientModels;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class AMClientModelsImpl {
	public static void postInit() {
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, consumer) -> {
			consumer.accept(AMClientModels.CONVEYOR_SUPPORTS);
		});
		
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if (modelIdentifier.equals(AMClientModels.ROCKET_INVENTORY)) {
				return new EmptyUnbakedModel();
			}
			
			return null;
		});
	}
}
