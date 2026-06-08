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
import com.github.mixinors.astromine.client.model.block.MachineModel;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;

public class AMModels {
	public static final ResourceLocation CABLE_MODEL_LOADER_ID = AMCommon.id("cable");
	public static final ResourceLocation MACHINE_MODEL_LOADER_ID = AMCommon.id("machine");

	public static final ResourceLocation PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/primitive_energy_cable_center");
	public static final ResourceLocation BASIC_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/basic_energy_cable_center");
	public static final ResourceLocation ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/advanced_energy_cable_center");
	public static final ResourceLocation ELITE_ENERGY_CABLE_CENTER_MODEL_ID = AMCommon.id("block/elite_energy_cable_center");
	public static final ResourceLocation FLUID_PIPE_CENTER_MODEL_ID = AMCommon.id("block/fluid_pipe_center");
	public static final ResourceLocation ITEM_CONDUIT_CENTER_MODEL_ID = AMCommon.id("block/item_conduit_center");
	
	public static final ResourceLocation CABLE_SIDE_ID = AMCommon.id("block/cable_side");
	public static final ResourceLocation CABLE_CONNECTOR_ID = AMCommon.id("block/cable_connector");
	public static final ResourceLocation CABLE_INSERT_CONNECTOR_ID = AMCommon.id("block/cable_connector_insert");
	public static final ResourceLocation CABLE_EXTRACT_CONNECTOR_ID = AMCommon.id("block/cable_connector_extract");
	public static final ResourceLocation CABLE_INSERT_EXTRACT_CONNECTOR_ID = AMCommon.id("block/cable_connector_insert_extract");
	public static final ResourceLocation SIDING_OVERLAY_INSERT_MODEL_ID = AMCommon.id("block/siding_overlay_insert");
	public static final ResourceLocation SIDING_OVERLAY_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_extract");
	public static final ResourceLocation SIDING_OVERLAY_INSERT_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_insert_extract");
	public static final ResourceLocation SIDING_OVERLAY_ITEM_INSERT_MODEL_ID = AMCommon.id("block/siding_overlay_item_insert");
	public static final ResourceLocation SIDING_OVERLAY_ITEM_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_item_extract");
	public static final ResourceLocation SIDING_OVERLAY_ITEM_INSERT_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_item_insert_extract");
	public static final ResourceLocation SIDING_OVERLAY_FLUID_INSERT_MODEL_ID = AMCommon.id("block/siding_overlay_fluid_insert");
	public static final ResourceLocation SIDING_OVERLAY_FLUID_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_fluid_extract");
	public static final ResourceLocation SIDING_OVERLAY_FLUID_INSERT_EXTRACT_MODEL_ID = AMCommon.id("block/siding_overlay_fluid_insert_extract");
	
	public static final ModelResourceLocation PRIMITIVE_ENERGY_CABLE_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/primitive_energy_cable"));
	public static final ModelResourceLocation BASIC_ENERGY_CABLE_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/basic_energy_cable"));
	public static final ModelResourceLocation ADVANCED_ENERGY_CABLE_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/advanced_energy_cable"));
	public static final ModelResourceLocation ELITE_ENERGY_CABLE_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/elite_energy_cable"));
	public static final ModelResourceLocation FLUID_PIPE_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/fluid_pipe"));
	public static final ModelResourceLocation ITEM_CONDUIT_BLOCK_MODEL = ModelResourceLocation.standalone(AMCommon.id("block/item_conduit"));
	
	public static void init(IEventBus modBus) {
		modBus.addListener(AMModels::registerGeometryLoaders);
		modBus.addListener(AMModels::registerAdditionalModels);
	}
	
	private static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
		event.register(CABLE_MODEL_LOADER_ID, CableModel.Loader.INSTANCE);
		event.register(MACHINE_MODEL_LOADER_ID, MachineModel.Loader.INSTANCE);
	}
	
	private static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
		event.register(ModelResourceLocation.standalone(PRIMITIVE_ENERGY_CABLE_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(BASIC_ENERGY_CABLE_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(ADVANCED_ENERGY_CABLE_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(ELITE_ENERGY_CABLE_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(FLUID_PIPE_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(ITEM_CONDUIT_CENTER_MODEL_ID));
		event.register(ModelResourceLocation.standalone(CABLE_SIDE_ID));
		event.register(ModelResourceLocation.standalone(CABLE_CONNECTOR_ID));
		event.register(ModelResourceLocation.standalone(CABLE_INSERT_CONNECTOR_ID));
		event.register(ModelResourceLocation.standalone(CABLE_EXTRACT_CONNECTOR_ID));
		event.register(ModelResourceLocation.standalone(CABLE_INSERT_EXTRACT_CONNECTOR_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_INSERT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_EXTRACT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_INSERT_EXTRACT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_ITEM_INSERT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_ITEM_EXTRACT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_ITEM_INSERT_EXTRACT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_FLUID_INSERT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_FLUID_EXTRACT_MODEL_ID));
		event.register(ModelResourceLocation.standalone(SIDING_OVERLAY_FLUID_INSERT_EXTRACT_MODEL_ID));
	}

	private static CableModel cable(ResourceLocation centerModelId) {
		return new CableModel(centerModelId, CABLE_SIDE_ID, CABLE_CONNECTOR_ID, CABLE_INSERT_CONNECTOR_ID, CABLE_EXTRACT_CONNECTOR_ID, CABLE_INSERT_EXTRACT_CONNECTOR_ID);
	}

	public static ResourceLocation sidingOverlayModel(StorageType storageType, StorageSiding siding, boolean split) {
		if (!split) {
			return switch (siding) {
				case INSERT -> SIDING_OVERLAY_INSERT_MODEL_ID;
				case EXTRACT -> SIDING_OVERLAY_EXTRACT_MODEL_ID;
				case INSERT_EXTRACT -> SIDING_OVERLAY_INSERT_EXTRACT_MODEL_ID;
				case NONE -> SIDING_OVERLAY_INSERT_MODEL_ID;
			};
		}

		return switch (storageType) {
			case ITEM -> switch (siding) {
				case INSERT -> SIDING_OVERLAY_ITEM_INSERT_MODEL_ID;
				case EXTRACT -> SIDING_OVERLAY_ITEM_EXTRACT_MODEL_ID;
				case INSERT_EXTRACT -> SIDING_OVERLAY_ITEM_INSERT_EXTRACT_MODEL_ID;
				case NONE -> SIDING_OVERLAY_ITEM_INSERT_MODEL_ID;
			};
			case FLUID -> switch (siding) {
				case INSERT -> SIDING_OVERLAY_FLUID_INSERT_MODEL_ID;
				case EXTRACT -> SIDING_OVERLAY_FLUID_EXTRACT_MODEL_ID;
				case INSERT_EXTRACT -> SIDING_OVERLAY_FLUID_INSERT_EXTRACT_MODEL_ID;
				case NONE -> SIDING_OVERLAY_FLUID_INSERT_MODEL_ID;
			};
			case ENERGY -> SIDING_OVERLAY_INSERT_MODEL_ID;
		};
	}
}
