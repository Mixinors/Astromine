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
import com.github.mixinors.astromine.client.model.block.DynamicModel;
import com.google.common.base.Suppliers;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.render.model.BuiltinBakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

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
	
	public static final ModelIdentifier PRIMITIVE_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/primitive_energy_cable"), "");
	public static final ModelIdentifier BASIC_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/basic_energy_cable"), "");
	public static final ModelIdentifier ADVANCED_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/advanced_energy_cable"), "");
	public static final ModelIdentifier ELITE_ENERGY_CABLE_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/elite_energy_cable"), "");
	
	public static final ModelIdentifier FLUID_PIPE_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/fluid_pipe"), "");
	
	public static final ModelIdentifier ITEM_CONDUIT_BLOCK_MODEL = new ModelIdentifier(new Identifier("astromine:block/item_conduit"), "");
	
	public static final ModelIdentifier ROCKET_INVENTORY = new ModelIdentifier(AMCommon.id("rocket"), "inventory");
	
	private static final String MOON = "moon";
	private static final String ITEM = "item";
	
	private static final String MODELS_PREFIX = "models/";
	
	public static final Supplier<ModelTransformation> ITEM_HANDHELD_TRANSFORMATION = Suppliers.memoize(() -> {
		try {
			var resource = InstanceUtil.getClient().getResourceManager().getResource(new Identifier("minecraft:models/item/handheld.json")).orElse(null);
			// TODO: Handle?
			
			var stream = resource.getInputStream();
			
			var model = JsonUnbakedModel.deserialize(new BufferedReader(new InputStreamReader(stream))).getTransformations();
			
			stream.close();
			
			return model;
		} catch (Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	});
	
	
	public static void init() {
		PreparableModelLoadingPlugin.register((resourceManager, executor) -> CompletableFuture.completedFuture(resourceManager), (manager, pluginContext) -> {
			pluginContext.modifyModelAfterBake().register((model, context) -> {
				if (context.id().equals(ROCKET_INVENTORY)) {
					return new BuiltinBakedModel(ITEM_HANDHELD_TRANSFORMATION.get(), ModelOverrideList.EMPTY, null, true);
				}
				
				return model;
			});
			
			pluginContext.resolveModel().register(context -> {
				if (context.id().equals(PRIMITIVE_ENERGY_CABLE_BLOCK_MODEL)) {
					return new CableModel(PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().equals(BASIC_ENERGY_CABLE_BLOCK_MODEL)) {
					return new CableModel(BASIC_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().equals(ADVANCED_ENERGY_CABLE_BLOCK_MODEL)) {
					return new CableModel(ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().equals(ELITE_ENERGY_CABLE_BLOCK_MODEL)) {
					return new CableModel(ELITE_ENERGY_CABLE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().equals(FLUID_PIPE_BLOCK_MODEL)) {
					return new CableModel(FLUID_PIPE_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().equals(ITEM_CONDUIT_BLOCK_MODEL)) {
					return new CableModel(ITEM_CONDUIT_CENTER_MODEL_ID, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
				}
				
				if (context.id().getNamespace().equals(AMCommon.MOD_ID)) {
					if (context.id().getPath().contains(MOON)) {
						return new DynamicModel(AMCommon.id(context.id().getPath()));
					}
				}
				
				return null;
			});
			
			pluginContext.addModels(
					PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID,
					BASIC_ENERGY_CABLE_CENTER_MODEL_ID,
					ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID,
					ELITE_ENERGY_CABLE_CENTER_MODEL_ID,
					FLUID_PIPE_CENTER_MODEL_ID,
					ITEM_CONDUIT_CENTER_MODEL_ID,
					CABLE_SIDE_ID,
					CABLE_CONNECTOR_ID,
					CABLE_INSERT_CONNECTOR_ID,
					CABLE_EXTRACT_CONNECTOR_ID,
					CABLE_INSERT_EXTRACT_CONNECTOR_ID
			);
			
			pluginContext.addModels(manager.findResources(MODELS_PREFIX, resource -> resource.getPath().contains(MOON)).keySet());
		});
	}
}
