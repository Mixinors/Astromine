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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import com.github.mixinors.astromine.common.transfer.storage.FluidStorageItem;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidStorage;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class AMLookups {
	public static void init() {
		AMCommon.modEventBus().addListener(AMLookups::registerCapabilities);
	}
	
	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_SOLID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_SOLID_GENERATOR.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_LIQUID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_LIQUID_GENERATOR.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_ELECTRIC_FURNACE.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_ELECTRIC_FURNACE.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_ELECTRIC_FURNACE.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_ALLOY_SMELTER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_ALLOY_SMELTER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_ALLOY_SMELTER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_TRITURATOR.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_TRITURATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_TRITURATOR.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_TRITURATOR.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_PRESSER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_PRESSER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_PRESSER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_PRESSER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_WIRE_MILL.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_WIRE_MILL.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_WIRE_MILL.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_WIRE_MILL.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_ELECTROLYZER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_ELECTROLYZER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_ELECTROLYZER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_ELECTROLYZER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_REFINERY.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_REFINERY.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_REFINERY.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_REFINERY.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_FLUID_MIXER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_FLUID_MIXER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_FLUID_MIXER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_FLUID_MIXER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_SOLIDIFIER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_SOLIDIFIER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_SOLIDIFIER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_SOLIDIFIER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_MELTER.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_MELTER.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_MELTER.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_MELTER.get());
		
		registerEnergy(event, AMBlockEntityTypes.PRIMITIVE_CAPACITOR.get());
		registerEnergy(event, AMBlockEntityTypes.BASIC_CAPACITOR.get());
		registerEnergy(event, AMBlockEntityTypes.ADVANCED_CAPACITOR.get());
		registerEnergy(event, AMBlockEntityTypes.ELITE_CAPACITOR.get());
		registerEnergy(event, AMBlockEntityTypes.CREATIVE_CAPACITOR.get());
		
		registerEnergy(event, AMBlockEntityTypes.FLUID_COLLECTOR.get());
		registerEnergy(event, AMBlockEntityTypes.FLUID_PLACER.get());
		registerEnergy(event, AMBlockEntityTypes.BLOCK_BREAKER.get());
		registerEnergy(event, AMBlockEntityTypes.BLOCK_PLACER.get());
		registerEnergy(event, AMBlockEntityTypes.PUMP.get());
		
		registerEnergyItem(event,
				AMItems.PRIMITIVE_BATTERY.get(),
				AMItems.BASIC_BATTERY.get(),
				AMItems.ADVANCED_BATTERY.get(),
				AMItems.ELITE_BATTERY.get(),
				AMItems.CREATIVE_BATTERY.get(),
				AMItems.PRIMITIVE_BATTERY_PACK.get(),
				AMItems.BASIC_BATTERY_PACK.get(),
				AMItems.ADVANCED_BATTERY_PACK.get(),
				AMItems.ELITE_BATTERY_PACK.get(),
				AMItems.CREATIVE_BATTERY_PACK.get(),
				AMItems.PRIMITIVE_DRILL.get(),
				AMItems.BASIC_DRILL.get(),
				AMItems.ADVANCED_DRILL.get(),
				AMItems.ELITE_DRILL.get(),
				AMItems.GRAVITY_GAUNTLET.get(),
				AMItems.SPACE_SUIT_CHESTPLATE.get()
		);
		
		registerFluidItem(event,
				AMItems.PORTABLE_TANK.get(),
				AMItems.LARGE_PORTABLE_TANK.get(),
				AMItems.SPACE_SUIT_CHESTPLATE.get()
		);
		
		registerBlockStorage(event,
				AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.BASIC_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.ELITE_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.BASIC_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.ELITE_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.PRIMITIVE_ELECTRIC_FURNACE.get(),
				AMBlockEntityTypes.BASIC_ELECTRIC_FURNACE.get(),
				AMBlockEntityTypes.ADVANCED_ELECTRIC_FURNACE.get(),
				AMBlockEntityTypes.ELITE_ELECTRIC_FURNACE.get(),
				AMBlockEntityTypes.PRIMITIVE_ALLOY_SMELTER.get(),
				AMBlockEntityTypes.BASIC_ALLOY_SMELTER.get(),
				AMBlockEntityTypes.ADVANCED_ALLOY_SMELTER.get(),
				AMBlockEntityTypes.ELITE_ALLOY_SMELTER.get(),
				AMBlockEntityTypes.PRIMITIVE_TRITURATOR.get(),
				AMBlockEntityTypes.BASIC_TRITURATOR.get(),
				AMBlockEntityTypes.ADVANCED_TRITURATOR.get(),
				AMBlockEntityTypes.ELITE_TRITURATOR.get(),
				AMBlockEntityTypes.PRIMITIVE_PRESSER.get(),
				AMBlockEntityTypes.BASIC_PRESSER.get(),
				AMBlockEntityTypes.ADVANCED_PRESSER.get(),
				AMBlockEntityTypes.ELITE_PRESSER.get(),
				AMBlockEntityTypes.PRIMITIVE_WIRE_MILL.get(),
				AMBlockEntityTypes.BASIC_WIRE_MILL.get(),
				AMBlockEntityTypes.ADVANCED_WIRE_MILL.get(),
				AMBlockEntityTypes.ELITE_WIRE_MILL.get(),
				AMBlockEntityTypes.PRIMITIVE_ELECTROLYZER.get(),
				AMBlockEntityTypes.BASIC_ELECTROLYZER.get(),
				AMBlockEntityTypes.ADVANCED_ELECTROLYZER.get(),
				AMBlockEntityTypes.ELITE_ELECTROLYZER.get(),
				AMBlockEntityTypes.PRIMITIVE_REFINERY.get(),
				AMBlockEntityTypes.BASIC_REFINERY.get(),
				AMBlockEntityTypes.ADVANCED_REFINERY.get(),
				AMBlockEntityTypes.ELITE_REFINERY.get(),
				AMBlockEntityTypes.PRIMITIVE_FLUID_MIXER.get(),
				AMBlockEntityTypes.BASIC_FLUID_MIXER.get(),
				AMBlockEntityTypes.ADVANCED_FLUID_MIXER.get(),
				AMBlockEntityTypes.ELITE_FLUID_MIXER.get(),
				AMBlockEntityTypes.PRIMITIVE_SOLIDIFIER.get(),
				AMBlockEntityTypes.BASIC_SOLIDIFIER.get(),
				AMBlockEntityTypes.ADVANCED_SOLIDIFIER.get(),
				AMBlockEntityTypes.ELITE_SOLIDIFIER.get(),
				AMBlockEntityTypes.PRIMITIVE_MELTER.get(),
				AMBlockEntityTypes.BASIC_MELTER.get(),
				AMBlockEntityTypes.ADVANCED_MELTER.get(),
				AMBlockEntityTypes.ELITE_MELTER.get(),
				AMBlockEntityTypes.PRIMITIVE_CAPACITOR.get(),
				AMBlockEntityTypes.BASIC_CAPACITOR.get(),
				AMBlockEntityTypes.ADVANCED_CAPACITOR.get(),
				AMBlockEntityTypes.ELITE_CAPACITOR.get(),
				AMBlockEntityTypes.CREATIVE_CAPACITOR.get(),
				AMBlockEntityTypes.FLUID_COLLECTOR.get(),
				AMBlockEntityTypes.FLUID_PLACER.get(),
				AMBlockEntityTypes.BLOCK_BREAKER.get(),
				AMBlockEntityTypes.BLOCK_PLACER.get(),
				AMBlockEntityTypes.PUMP.get(),
				AMBlockEntityTypes.DRAIN.get(),
				AMBlockEntityTypes.PRIMITIVE_TANK.get(),
				AMBlockEntityTypes.BASIC_TANK.get(),
				AMBlockEntityTypes.ADVANCED_TANK.get(),
				AMBlockEntityTypes.ELITE_TANK.get(),
				AMBlockEntityTypes.CREATIVE_TANK.get(),
				AMBlockEntityTypes.PRIMITIVE_BUFFER.get(),
				AMBlockEntityTypes.BASIC_BUFFER.get(),
				AMBlockEntityTypes.ADVANCED_BUFFER.get(),
				AMBlockEntityTypes.ELITE_BUFFER.get(),
				AMBlockEntityTypes.CREATIVE_BUFFER.get()
		);
	}
	
	private static <T extends ExtendedBlockEntity> void registerEnergy(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
		event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type, (blockEntity, direction) -> blockEntity.getEnergyStorage());
	}
	
	private static void registerEnergyItem(RegisterCapabilitiesEvent event, Item... items) {
		event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> {
			if (stack.getItem() instanceof EnergyStorageItem energyStorageItem) {
				return energyStorageItem.createEnergyStorage(stack);
			}
			
			return null;
		}, items);
	}
	
	private static void registerFluidItem(RegisterCapabilitiesEvent event, Item... items) {
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> {
			if (stack.getItem() instanceof FluidStorageItem fluidStorageItem) {
				return fluidStorageItem.createFluidStorage(stack);
			}
			
			return null;
		}, items);
	}
	
	@SafeVarargs
	private static <T extends ExtendedBlockEntity> void registerBlockStorage(RegisterCapabilitiesEvent event, BlockEntityType<? extends T>... types) {
		for (var type : types) {
			event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type, (blockEntity, direction) -> getSidedItemStorage(blockEntity, direction));
			event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type, (blockEntity, direction) -> getSidedFluidStorage(blockEntity, direction));
		}
	}
	
	private static SimpleItemStorage getSidedItemStorage(ExtendedBlockEntity blockEntity, Direction direction) {
		var storage = blockEntity.getItemStorage();
		
		if (storage == null) {
			return null;
		}
		
		if (direction == null) {
			return storage.getWildProxy();
		}
		
		return switch (storage.getSidings()[direction.ordinal()]) {
			case INSERT -> storage.getInsertableProxy();
			case EXTRACT -> storage.getExtractableProxy();
			case INSERT_EXTRACT -> storage.getWildProxy();
			case NONE -> null;
		};
	}
	
	private static SimpleFluidStorage getSidedFluidStorage(ExtendedBlockEntity blockEntity, Direction direction) {
		var storage = blockEntity.getFluidStorage();
		
		if (storage == null) {
			return null;
		}
		
		if (direction == null) {
			return storage.getWildProxy();
		}
		
		return switch (storage.getSidings()[direction.ordinal()]) {
			case INSERT -> storage.getInsertableProxy();
			case EXTRACT -> storage.getExtractableProxy();
			case INSERT_EXTRACT -> storage.getWildProxy();
			case NONE -> null;
		};
	}
}
