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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.screenhandler.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

public class AMScreenHandlers {
	public static final ScreenHandlerType<RecipeCreatorScreenHandler> RECIPE_CREATOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("recipe_creator"), ((syncId, inventory, buffer) -> {
		return new RecipeCreatorScreenHandler(syncId, inventory.player);
	}));
	
	public static final ScreenHandlerType<PrimitiveRocketScreenHandler> ROCKET = ScreenHandlerRegistry.registerExtended(AMCommon.id("rocket"), ((syncId, inventory, buffer) -> {
		return new PrimitiveRocketScreenHandler(syncId, inventory.player, buffer.readInt());
	}));
	
	public static final ScreenHandlerType<FluidCollectorScreenHandler> FLUID_EXTRACTOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("fluid_collector"), ((syncId, inventory, buffer) -> {
		return new FluidCollectorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<FluidPlacerScreenHandler> FLUID_INSERTER = ScreenHandlerRegistry.registerExtended(AMCommon.id("fluid_placer"), ((syncId, inventory, buffer) -> {
		return new FluidPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<BlockBreakerScreenHandler> BLOCK_BREAKER = ScreenHandlerRegistry.registerExtended(AMCommon.id("block_breaker"), ((syncId, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<BlockPlacerScreenHandler> BLOCK_PLACER = ScreenHandlerRegistry.registerExtended(AMCommon.id("block_placer"), ((syncId, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<FluidGeneratorScreenHandler> LIQUID_GENERATOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("fluid_generator"), ((syncId, inventory, buffer) -> {
		return new FluidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<SolidGeneratorScreenHandler> SOLID_GENERATOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("solid_generator"), ((syncId, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<VentScreenHandler> VENT = ScreenHandlerRegistry.registerExtended(AMCommon.id("vent"), ((syncId, inventory, buffer) -> {
		return new VentScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<TankScreenHandler> TANK = ScreenHandlerRegistry.registerExtended(AMCommon.id("tank"), ((syncId, inventory, buffer) -> {
		return new TankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<NuclearWarheadScreenHandler> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AMCommon.id("nuclear_warhead"), ((syncId, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<CapacitorScreenHandler> CAPACITOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("capacitor"), ((syncId, inventory, buffer) -> {
		return new CapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<CreativeCapacitorScreenHandler> CREATIVE_CAPACITOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("creative_capacitor"), ((syncId, inventory, buffer) -> {
		return new CreativeCapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<CreativeTankScreenHandler> CREATIVE_TANK = ScreenHandlerRegistry.registerExtended(AMCommon.id("creative_tank"), ((syncId, inventory, buffer) -> {
		return new CreativeTankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<CreativeBufferScreenHandler> CREATIVE_BUFFER = ScreenHandlerRegistry.registerExtended(AMCommon.id("creative_buffer"), ((syncId, inventory, buffer) -> {
		return new CreativeBufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<BufferScreenHandler> BUFFER = ScreenHandlerRegistry.registerExtended(AMCommon.id("buffer"), ((syncId, inventory, buffer) -> {
		return new BufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<TrituratorScreenHandler> TRITURATOR = ScreenHandlerRegistry.registerExtended(AMCommon.id("triturator"), ((syncId, inventory, buffer) -> {
		return new TrituratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<PressScreenHandler> PRESSER = ScreenHandlerRegistry.registerExtended(AMCommon.id("press"), ((syncId, inventory, buffer) -> {
		return new PressScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<WireMillScreenHandler> WIREMILL = ScreenHandlerRegistry.registerExtended(AMCommon.id("wire_mill"), ((syncId, inventory, buffer) -> {
		return new WireMillScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<ElectricFurnaceScreenHandler> ELECTRIC_FURNACE = ScreenHandlerRegistry.registerExtended(AMCommon.id("electric_furnace"), ((syncId, inventory, buffer) -> {
		return new ElectricFurnaceScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<ElectrolyzerScreenHandler> ELECTROLYZER = ScreenHandlerRegistry.registerExtended(AMCommon.id("electrolyzer"), ((syncId, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<RefineryScreenHandler> REFINERY = ScreenHandlerRegistry.registerExtended(AMCommon.id("refinery"), ((syncId, inventory, buffer) -> {
		return new RefineryScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<FluidMixerScreenHandler> FLUID_MIXER = ScreenHandlerRegistry.registerExtended(AMCommon.id("fluid_mixer"), ((syncId, inventory, buffer) -> {
		return new FluidMixerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<AlloySmelterScreenHandler> ALLOY_SMELTER = ScreenHandlerRegistry.registerExtended(AMCommon.id("alloy_smelter"), ((syncId, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<SolidifierScreenHandler> SOLIDIFIER = ScreenHandlerRegistry.registerExtended(AMCommon.id("solidifier"), ((syncId, inventory, buffer) -> {
		return new SolidifierScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final ScreenHandlerType<MelterScreenHandler> MELTER = ScreenHandlerRegistry.registerExtended(AMCommon.id("melter"), ((syncId, inventory, buffer) -> {
		return new MelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static void init() {

	}
}
