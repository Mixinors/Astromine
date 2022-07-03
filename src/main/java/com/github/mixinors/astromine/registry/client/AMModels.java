/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.model.block.CableModel;
import com.github.mixinors.astromine.client.model.block.TintedModel;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class AMModels {
	public static final Identifier PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/primitive_energy_cable_center");
	
	public static final Identifier BASIC_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/basic_energy_cable_center");
	
	public static final Identifier ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/advanced_energy_cable_center");
	
	public static final Identifier ELITE_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/elite_energy_cable_center");
	
	public static final Identifier FLUID_PIPE_CENTER_MODEL_ID = AMCommon.id("block/fluid_pipe_center");
	
	public static final Identifier ITEM_CONDUIT_CENTER_MODEL_ID = AMCommon.id("block/item_conduit_center");
	
	public static final Identifier CABLE_SIDE_ID = AMCommon.id("block/cable_side");
	
	public static final Identifier CABLE_CONNECTOR_ID = AMCommon.id("block/cable_connector");
	public static final Identifier CABLE_INSERT_CONNECTOR_ID = AMCommon.id("block/cable_connector_insert");
	public static final Identifier CABLE_EXTRACT_CONNECTOR_ID = AMCommon.id("block/cable_connector_extract");
	public static final Identifier CABLE_INSERT_EXTRACT_CONNECTOR_ID = AMCommon.id("block/cable_connector_insert_extract");
	
	public static final ModelIdentifier PRIMITIVE_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier("astromine:block/primitive_energy_cable");
	public static final ModelIdentifier BASIC_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier("astromine:block/basic_energy_cable");
	public static final ModelIdentifier ADVANCED_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier("astromine:block/advanced_energy_cable");
	public static final ModelIdentifier ELITE_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier("astromine:block/elite_energy_cable");
	
	public static final ModelIdentifier FLUID_PIPE_BLOCK_MODEL = new ModelIdentifier("astromine:block/fluid_pipe");
	
	public static final ModelIdentifier ITEM_CONDUIT_BLOCK_MODEL = new ModelIdentifier("astromine:block/item_conduit");
	
	public static final ModelIdentifier ROCKET_INVENTORY = new ModelIdentifier(AMCommon.id("rocket"), "inventory");
	
	public static final Lazy<ModelTransformation> ITEM_HANDHELD_TRANSFORMATION = new Lazy<>(() -> {
		try {
			var resource = InstanceUtil.getClient().getResourceManager().getResource(new Identifier("minecraft:models/item/handheld.json"));
			
			var stream = resource.getInputStream();
			
			var model = JsonUnbakedModel.deserialize(new BufferedReader(new InputStreamReader(stream))).getTransformations();
			
			stream.close();
			
			return model;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	});
	
	
	public static void init() {
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if (modelIdentifier.equals(ROCKET_INVENTORY)) {
				return new UnbakedModel() {
					@Override
					public Collection<Identifier> getModelDependencies() {
						return ImmutableList.of();
					}
					
					@Override
					public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
						return ImmutableList.of();
					}
					
					@Override
					public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
						return new BuiltinBakedModel(ITEM_HANDHELD_TRANSFORMATION.get(), ModelOverrideList.EMPTY, null, true);
					}
				};
			}
			
			return null;
		});
		
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resourceManager -> (modelIdentifier, modelProviderContext) -> {
			if (modelIdentifier.equals(PRIMITIVE_ENERGY_CABLE_BLOCK_MODEL)) {
				return new CableModel(PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.equals(BASIC_ENERGY_CABLE_BLOCK_MODEL)) {
				return new CableModel(BASIC_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.equals(ADVANCED_ENERGY_CABLE_BLOCK_MODEL)) {
				return new CableModel(ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.equals(ELITE_ENERGY_CABLE_BLOCK_MODEL)) {
				return new CableModel(ELITE_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.equals(FLUID_PIPE_BLOCK_MODEL)) {
				return new CableModel(FLUID_PIPE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.equals(ITEM_CONDUIT_BLOCK_MODEL)) {
				return new CableModel(ITEM_CONDUIT_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
			}
			
			if (modelIdentifier.getNamespace().equals(AMCommon.MOD_ID)) {
				if (modelIdentifier.getPath().contains("moon") && !modelIdentifier.getPath().contains("item")) {
					return new TintedModel(AMCommon.id(modelIdentifier.getPath()));
				}
			}
			
			return null;
		});
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(BASIC_ENERGY_CABLE_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(ELITE_ENERGY_CABLE_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(FLUID_PIPE_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(ITEM_CONDUIT_CENTER_MODEL_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(CABLE_SIDE_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(CABLE_CONNECTOR_ID));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(CABLE_INSERT_CONNECTOR_ID));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(CABLE_EXTRACT_CONNECTOR_ID));
		ModelLoadingRegistry.INSTANCE.registerModelProvider((resourceManager, out) -> out.accept(CABLE_INSERT_EXTRACT_CONNECTOR_ID));
		
		ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
			for (var id : manager.findResources("models/", s -> s.contains("moon") && !s.contains("item"))) {
				if (!id.toString().contains("item")) {
					out.accept(id);
				}
			}
		});
	}
}
