/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.client.model.PrimitiveRocketEntityModel;
import com.github.mixinors.astromine.client.render.entity.PrimitiveRocketEntityRenderer;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import com.github.mixinors.astromine.AstromineCommon;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class AstromineClientModels {
	public static final Lazy<ModelTransformation> ITEM_HANDHELD = new Lazy<>(() -> {
		try {
			Resource resource = MinecraftClient.getInstance().getResourceManager().getResource(new Identifier("minecraft:models/item/handheld.json"));
			InputStream stream = resource.getInputStream();
			ModelTransformation model = JsonUnbakedModel.deserialize(new BufferedReader(new InputStreamReader(stream))).getTransformations();
			stream.close();
			return model;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	});

	public static void initialize() {
		ModelLoadingRegistry.INSTANCE.registerAppender((resourceManager, consumer) -> {
			consumer.accept(new ModelIdentifier(new Identifier(AstromineCommon.MOD_ID, "conveyor_supports"), ""));
		});

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
	
	public static void renderRocket(PrimitiveRocketEntityModel primitiveRocketEntityModel, ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		matrices.push();
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.translate(0.66F, 0.22F, 0F);
		}
		matrices.scale(1.0F, -1.0F, -1.0F);
		if (mode == ModelTransformation.Mode.GUI) {
			matrices.scale(0.09F, 0.09F, 0.09F);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(45));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(45));
		} else {
			matrices.scale(0.3F, 0.3F, 0.3F);
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(45));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(45));
		}
		VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, primitiveRocketEntityModel.getLayer(PrimitiveRocketEntityRenderer.ID), false, stack.hasGlint());
		primitiveRocketEntityModel.render(matrices, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}
