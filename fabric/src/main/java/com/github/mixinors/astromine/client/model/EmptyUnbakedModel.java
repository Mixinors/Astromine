package com.github.mixinors.astromine.client.model;

import com.github.mixinors.astromine.registry.client.AMClientModels;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class EmptyUnbakedModel implements UnbakedModel {
	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}
	
	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptyList();
	}
	
	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		return new BuiltinBakedModel(AMClientModels.ITEM_HANDHELD_TRANSFORMATION.get(), ModelOverrideList.EMPTY, null, true);
	}
}
