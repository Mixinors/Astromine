package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.model.PrimitiveRocketEntityModel;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class AMEntityModelLayers {
	public static final EntityModelLayer ROCKET = register("rocket", PrimitiveRocketEntityModel::getTexturedModelData);

	public static void init() {

	}

	public static EntityModelLayer register(String id, EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider) {
		EntityModelLayer entityModelLayer = new EntityModelLayer(AMCommon.id(id), "main");
		EntityModelLayerRegistry.registerModelLayer(entityModelLayer, texturedModelDataProvider);
		return entityModelLayer;
	}
}
