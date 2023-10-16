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
import dev.vini2003.hammer.core.api.common.function.TriFunction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;

public class AMScreenHandlers {
    public static final RegistrySupplier<ScreenHandlerType<RecipeCreatorScreenHandler>> RECIPE_CREATOR = register("recipe_creator", RecipeCreatorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<BodySelectorScreenHandler>> BODY_SELECTOR = register("body_selector", BodySelectorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<FluidCollectorScreenHandler>> FLUID_EXTRACTOR = register("fluid_collector", FluidCollectorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<FluidPlacerScreenHandler>> FLUID_INSERTER = register("fluid_placer", FluidPlacerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<BlockBreakerScreenHandler>> BLOCK_BREAKER = register("block_breaker", BlockBreakerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<BlockPlacerScreenHandler>> BLOCK_PLACER = register("block_placer", BlockPlacerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<PumpScreenHandler>> PUMP = register("pump", PumpScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<FluidGeneratorScreenHandler>> LIQUID_GENERATOR = register("fluid_generator", FluidGeneratorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<SolidGeneratorScreenHandler>> SOLID_GENERATOR = register("solid_generator", SolidGeneratorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<TankScreenHandler>> TANK = register("tank", TankScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<NuclearWarheadScreenHandler>> NUCLEAR_WARHEAD = register("nuclear_warhead", NuclearWarheadScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<CapacitorScreenHandler>> CAPACITOR = register("capacitor", CapacitorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<BufferScreenHandler>> BUFFER = register("buffer", BufferScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<TrituratorScreenHandler>> TRITURATOR = register("triturator", TrituratorScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<PresserScreenHandler>> PRESSER = register("press", PresserScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<WireMillScreenHandler>> WIRE_MILL = register("wire_mill", WireMillScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<ElectricFurnaceScreenHandler>> ELECTRIC_FURNACE = register("electric_furnace", ElectricFurnaceScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<ElectrolyzerScreenHandler>> ELECTROLYZER = register("electrolyzer", ElectrolyzerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<RefineryScreenHandler>> REFINERY = register("refinery", RefineryScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<FluidMixerScreenHandler>> FLUID_MIXER = register("fluid_mixer", FluidMixerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<AlloySmelterScreenHandler>> ALLOY_SMELTER = register("alloy_smelter", AlloySmelterScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<SolidifierScreenHandler>> SOLIDIFIER = register("solidifier", SolidifierScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<MelterScreenHandler>> MELTER = register("melter", MelterScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<RocketControllerScreenHandler>> ROCKET_CONTROLLER = register("rocket_controller", RocketControllerScreenHandler::new);
    public static final RegistrySupplier<ScreenHandlerType<StationControllerScreenHandler>> STATION_CONTROLLER = register("station_controller", StationControllerScreenHandler::new);

    public static void init() {
        
    }

    public static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> register(String name, BiFunction<Integer, PlayerEntity, T> factory) {
        return AMCommon.registry(RegistryKeys.SCREEN_HANDLER).register(AMCommon.id(name), () -> MenuRegistry.ofExtended((id, inventory, buf) -> {
            return factory.apply(id, inventory.player);
        }));
    }

    public static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> register(String name, TriFunction<Integer, PlayerEntity, BlockPos, T> factory) {
        return AMCommon.registry(RegistryKeys.SCREEN_HANDLER).register(AMCommon.id(name), () -> MenuRegistry.ofExtended((id, inventory, buf) -> {
            return factory.apply(id, inventory.player, buf.readBlockPos());
        }));
    }
}
