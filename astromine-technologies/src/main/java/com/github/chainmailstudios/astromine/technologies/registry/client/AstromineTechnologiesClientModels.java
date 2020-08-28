package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.client.AstromineClientModels;
import com.github.chainmailstudios.astromine.technologies.client.model.RocketEntityModel;
import com.github.chainmailstudios.astromine.technologies.client.render.entity.RocketEntityRenderer;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class AstromineTechnologiesClientModels extends AstromineClientModels {
	public static void initialize() {
		ModelIdentifier rocketItemId = new ModelIdentifier(AstromineCommon.identifier("rocket"), "inventory");
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if (modelIdentifier.equals(rocketItemId)) {
				return new UnbakedModel() {
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
						return new BuiltinBakedModel(ITEM_HANDHELD.get(), ModelOverrideList.EMPTY, null, true);
					}
				};
			}
			return null;
		});
	}

	public static void renderRocket(RocketEntityModel rocketEntityModel, ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		matrices.push();
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.translate(0.5F, 0.1F, 0F);
		}
		matrices.scale(1.0F, -1.0F, -1.0F);
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.scale(0.06F, 0.06F, 0.06F);
		} else {
			matrices.scale(0.3F, 0.3F, 0.3F);
		}
		VertexConsumer vertexConsumer2 = ItemRenderer.getDirectGlintVertexConsumer(vertexConsumerProvider, rocketEntityModel.getLayer(RocketEntityRenderer.ID), false, stack.hasGlint());
		rocketEntityModel.render(matrices, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}
