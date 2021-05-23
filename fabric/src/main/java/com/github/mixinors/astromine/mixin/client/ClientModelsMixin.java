package com.github.mixinors.astromine.mixin.client;

import com.github.mixinors.astromine.client.model.EmptyUnbakedModel;
import com.github.mixinors.astromine.registry.client.AMClientModels;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AMClientModels.class)
public class ClientModelsMixin {
	@Overwrite(remap = false)
	@SuppressWarnings("all")
	public static void init() {
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
