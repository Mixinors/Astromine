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
import com.github.mixinors.astromine.common.screen.handler.NuclearWarheadScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.RecipeCreatorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.body.BodySelectorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.machine.*;
import com.github.mixinors.astromine.common.screen.handler.machine.generator.FluidGeneratorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.machine.generator.SolidGeneratorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.rocket.RocketControllerScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.station.StationControllerScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.storage.BufferScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.storage.CapacitorScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.storage.TankScreenHandler;
import com.github.mixinors.astromine.common.screen.handler.utility.*;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AMScreenHandlers {
	public static final RegistrySupplier<ScreenHandlerType<RecipeCreatorScreenHandler>> RECIPE_CREATOR = registerExtended(AMCommon.id("recipe_creator"), ((syncId, inventory, buffer) -> {
		return new RecipeCreatorScreenHandler(syncId, inventory.player);
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<BodySelectorScreenHandler>> BODY_SELECTOR = registerExtended(AMCommon.id("body_selector"), ((syncId, inventory, buffer) -> {
		return new BodySelectorScreenHandler(syncId, inventory.player);
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<FluidCollectorScreenHandler>> FLUID_EXTRACTOR = registerExtended(AMCommon.id("fluid_collector"), ((syncId, inventory, buffer) -> {
		return new FluidCollectorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<FluidPlacerScreenHandler>> FLUID_INSERTER = registerExtended(AMCommon.id("fluid_placer"), ((syncId, inventory, buffer) -> {
		return new FluidPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<BlockBreakerScreenHandler>> BLOCK_BREAKER = registerExtended(AMCommon.id("block_breaker"), ((syncId, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<BlockPlacerScreenHandler>> BLOCK_PLACER = registerExtended(AMCommon.id("block_placer"), ((syncId, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<PumpScreenHandler>> PUMP = registerExtended(AMCommon.id("pump"), ((syncId, inventory, buffer) -> {
		return new PumpScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<FluidGeneratorScreenHandler>> LIQUID_GENERATOR = registerExtended(AMCommon.id("fluid_generator"), ((syncId, inventory, buffer) -> {
		return new FluidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<SolidGeneratorScreenHandler>> SOLID_GENERATOR = registerExtended(AMCommon.id("solid_generator"), ((syncId, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<TankScreenHandler>> TANK = registerExtended(AMCommon.id("tank"), ((syncId, inventory, buffer) -> {
		return new TankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<NuclearWarheadScreenHandler>> NUCLEAR_WARHEAD = registerExtended(AMCommon.id("nuclear_warhead"), ((syncId, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<CapacitorScreenHandler>> CAPACITOR = registerExtended(AMCommon.id("capacitor"), ((syncId, inventory, buffer) -> {
		return new CapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<BufferScreenHandler>> BUFFER = registerExtended(AMCommon.id("buffer"), ((syncId, inventory, buffer) -> {
		return new BufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<TrituratorScreenHandler>> TRITURATOR = registerExtended(AMCommon.id("triturator"), ((syncId, inventory, buffer) -> {
		return new TrituratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<PresserScreenHandler>> PRESSER = registerExtended(AMCommon.id("press"), ((syncId, inventory, buffer) -> {
		return new PresserScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<WireMillScreenHandler>> WIRE_MILL = registerExtended(AMCommon.id("wire_mill"), ((syncId, inventory, buffer) -> {
		return new WireMillScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<ElectricFurnaceScreenHandler>> ELECTRIC_FURNACE = registerExtended(AMCommon.id("electric_furnace"), ((syncId, inventory, buffer) -> {
		return new ElectricFurnaceScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<ElectrolyzerScreenHandler>> ELECTROLYZER = registerExtended(AMCommon.id("electrolyzer"), ((syncId, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<RefineryScreenHandler>> REFINERY = registerExtended(AMCommon.id("refinery"), ((syncId, inventory, buffer) -> {
		return new RefineryScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<FluidMixerScreenHandler>> FLUID_MIXER = registerExtended(AMCommon.id("fluid_mixer"), ((syncId, inventory, buffer) -> {
		return new FluidMixerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<AlloySmelterScreenHandler>> ALLOY_SMELTER = registerExtended(AMCommon.id("alloy_smelter"), ((syncId, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<SolidifierScreenHandler>> SOLIDIFIER = registerExtended(AMCommon.id("solidifier"), ((syncId, inventory, buffer) -> {
		return new SolidifierScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<MelterScreenHandler>> MELTER = registerExtended(AMCommon.id("melter"), ((syncId, inventory, buffer) -> {
		return new MelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<RocketControllerScreenHandler>> ROCKET_CONTROLLER = registerExtended(AMCommon.id("rocket_controller"), ((syncId, inventory, buffer) -> {
		return new RocketControllerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final RegistrySupplier<ScreenHandlerType<StationControllerScreenHandler>> STATION_CONTROLLER = registerExtended(AMCommon.id("station_controller"), ((syncId, inventory, buffer) -> {
		return new StationControllerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static void init() {
	}
	
	public static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> registerExtended(Identifier id, MenuRegistry.ExtendedMenuTypeFactory<T> factory) {
		return AMCommon.registry(Registry.MENU_KEY).register(id, () -> MenuRegistry.ofExtended(factory));
	}
}
