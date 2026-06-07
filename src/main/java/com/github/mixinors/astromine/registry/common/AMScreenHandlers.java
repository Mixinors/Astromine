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
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMScreenHandlers {
	private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, AMCommon.MOD_ID);
	
	public static final DeferredHolder<MenuType<?>, MenuType<RecipeCreatorScreenHandler>> RECIPE_CREATOR = registerExtended(AMCommon.id("recipe_creator"), ((syncId, inventory, buffer) -> {
		return new RecipeCreatorScreenHandler(syncId, inventory.player);
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<BodySelectorScreenHandler>> BODY_SELECTOR = registerExtended(AMCommon.id("body_selector"), ((syncId, inventory, buffer) -> {
		return new BodySelectorScreenHandler(syncId, inventory.player);
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<FluidCollectorScreenHandler>> FLUID_EXTRACTOR = registerExtended(AMCommon.id("fluid_collector"), ((syncId, inventory, buffer) -> {
		return new FluidCollectorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<FluidPlacerScreenHandler>> FLUID_INSERTER = registerExtended(AMCommon.id("fluid_placer"), ((syncId, inventory, buffer) -> {
		return new FluidPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<BlockBreakerScreenHandler>> BLOCK_BREAKER = registerExtended(AMCommon.id("block_breaker"), ((syncId, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<BlockPlacerScreenHandler>> BLOCK_PLACER = registerExtended(AMCommon.id("block_placer"), ((syncId, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<PumpScreenHandler>> PUMP = registerExtended(AMCommon.id("pump"), ((syncId, inventory, buffer) -> {
		return new PumpScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<FluidGeneratorScreenHandler>> LIQUID_GENERATOR = registerExtended(AMCommon.id("fluid_generator"), ((syncId, inventory, buffer) -> {
		return new FluidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<SolidGeneratorScreenHandler>> SOLID_GENERATOR = registerExtended(AMCommon.id("solid_generator"), ((syncId, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<TankScreenHandler>> TANK = registerExtended(AMCommon.id("tank"), ((syncId, inventory, buffer) -> {
		return new TankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<NuclearWarheadScreenHandler>> NUCLEAR_WARHEAD = registerExtended(AMCommon.id("nuclear_warhead"), ((syncId, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<CapacitorScreenHandler>> CAPACITOR = registerExtended(AMCommon.id("capacitor"), ((syncId, inventory, buffer) -> {
		return new CapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<BufferScreenHandler>> BUFFER = registerExtended(AMCommon.id("buffer"), ((syncId, inventory, buffer) -> {
		return new BufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<TrituratorScreenHandler>> TRITURATOR = registerExtended(AMCommon.id("triturator"), ((syncId, inventory, buffer) -> {
		return new TrituratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<PresserScreenHandler>> PRESSER = registerExtended(AMCommon.id("press"), ((syncId, inventory, buffer) -> {
		return new PresserScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<WireMillScreenHandler>> WIRE_MILL = registerExtended(AMCommon.id("wire_mill"), ((syncId, inventory, buffer) -> {
		return new WireMillScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<ElectricFurnaceScreenHandler>> ELECTRIC_FURNACE = registerExtended(AMCommon.id("electric_furnace"), ((syncId, inventory, buffer) -> {
		return new ElectricFurnaceScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<ElectrolyzerScreenHandler>> ELECTROLYZER = registerExtended(AMCommon.id("electrolyzer"), ((syncId, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<RefineryScreenHandler>> REFINERY = registerExtended(AMCommon.id("refinery"), ((syncId, inventory, buffer) -> {
		return new RefineryScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<FluidMixerScreenHandler>> FLUID_MIXER = registerExtended(AMCommon.id("fluid_mixer"), ((syncId, inventory, buffer) -> {
		return new FluidMixerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<AlloySmelterScreenHandler>> ALLOY_SMELTER = registerExtended(AMCommon.id("alloy_smelter"), ((syncId, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<SolidifierScreenHandler>> SOLIDIFIER = registerExtended(AMCommon.id("solidifier"), ((syncId, inventory, buffer) -> {
		return new SolidifierScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<MelterScreenHandler>> MELTER = registerExtended(AMCommon.id("melter"), ((syncId, inventory, buffer) -> {
		return new MelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<RocketControllerScreenHandler>> ROCKET_CONTROLLER = registerExtended(AMCommon.id("rocket_controller"), ((syncId, inventory, buffer) -> {
		return new RocketControllerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static final DeferredHolder<MenuType<?>, MenuType<StationControllerScreenHandler>> STATION_CONTROLLER = registerExtended(AMCommon.id("station_controller"), ((syncId, inventory, buffer) -> {
		return new StationControllerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
	}
	
	public static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerExtended(ResourceLocation id, ExtendedMenuFactory<T> factory) {
		return REGISTRY.register(id.getPath(), () -> new MenuType<>((IContainerFactory<T>) factory::create, FeatureFlags.VANILLA_SET));
	}
	
	@FunctionalInterface
	public interface ExtendedMenuFactory<T extends AbstractContainerMenu> {
		T create(int syncId, Inventory inventory, RegistryFriendlyByteBuf buffer);
	}
}
