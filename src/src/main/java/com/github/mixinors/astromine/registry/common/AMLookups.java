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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.item.base.FluidStorageItem;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidItemStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import team.reborn.energy.api.EnergyStorage;

public class AMLookups {
	public static void init() {
		ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
					if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
						var itemStorage = extendedBlockEntity.getItemStorage();
						
						var sidings = itemStorage.getSidings();
						
						var siding = sidings[direction.ordinal()];
						
						return switch (siding) {
							case INSERT -> itemStorage.getInsertableProxy();
							case EXTRACT -> itemStorage.getExtractableProxy();
							
							case INSERT_EXTRACT -> itemStorage;
							
							default -> null;
						};
					}
					
					return null;
				},
				AMBlockEntityTypes.PRIMITIVE_BUFFER.get(),
				AMBlockEntityTypes.BASIC_BUFFER.get(),
				AMBlockEntityTypes.ADVANCED_BUFFER.get(),
				AMBlockEntityTypes.ELITE_BUFFER.get(),
				AMBlockEntityTypes.CREATIVE_BUFFER.get(),
				
				AMBlockEntityTypes.PRIMITIVE_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.BASIC_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.ADVANCED_SOLID_GENERATOR.get(),
				AMBlockEntityTypes.ELITE_SOLID_GENERATOR.get(),
				
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
				
				AMBlockEntityTypes.BLOCK_BREAKER.get(),
				AMBlockEntityTypes.BLOCK_PLACER.get()
		);
		
		FluidStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
					if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
						var fluidStorage = extendedBlockEntity.getFluidStorage();
						
						var sidings = fluidStorage.getSidings();
						
						var siding = sidings[direction.ordinal()];
						
						return switch (siding) {
							case INSERT -> fluidStorage.getInsertableProxy();
							case EXTRACT -> fluidStorage.getExtractableProxy();
							
							case INSERT_EXTRACT -> fluidStorage;
							
							default -> null;
						};
					}
					
					return null;
				},
				AMBlockEntityTypes.PRIMITIVE_TANK.get(),
				AMBlockEntityTypes.BASIC_TANK.get(),
				AMBlockEntityTypes.ADVANCED_TANK.get(),
				AMBlockEntityTypes.ELITE_TANK.get(),
				
				AMBlockEntityTypes.PRIMITIVE_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.BASIC_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.ADVANCED_LIQUID_GENERATOR.get(),
				AMBlockEntityTypes.ELITE_LIQUID_GENERATOR.get(),
				
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
				
				AMBlockEntityTypes.FLUID_COLLECTOR.get(),
				AMBlockEntityTypes.FLUID_PLACER.get(),
				
				AMBlockEntityTypes.DRAIN.get()
		);
		
		EnergyStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
					if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
						return extendedBlockEntity.getEnergyStorage();
					}
					
					return null;
				},
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
				AMBlockEntityTypes.BLOCK_PLACER.get()
		);
		
		FluidStorage.ITEM.registerForItems((storage, context) -> {
					if (storage.getItem() instanceof FluidStorageItem fluidStorageItem) {
						return new SimpleFluidItemStorage(context, fluidStorageItem.getCapacity());
					}
					
					return null;
				},
				AMItems.PORTABLE_TANK.get(),
				AMItems.LARGE_PORTABLE_TANK.get()
		);
	}
}
