/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.technologies.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.world.inventory.MenuType;
import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.*;

public class AstromineTechnologiesScreenHandlers extends AstromineScreenHandlers {
	public static final MenuType<FluidCollectorScreenHandler> FLUID_EXTRACTOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_collector"), ((syncId, inventory, buffer) -> {
		return new FluidCollectorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<FluidPlacerScreenHandler> FLUID_INSERTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_placer"), ((syncId, inventory, buffer) -> {
		return new FluidPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<BlockBreakerScreenHandler> BLOCK_BREAKER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_breaker"), ((syncId, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<BlockPlacerScreenHandler> BLOCK_PLACER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_placer"), ((syncId, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<FluidGeneratorScreenHandler> LIQUID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_generator"), ((syncId, inventory, buffer) -> {
		return new FluidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<SolidGeneratorScreenHandler> SOLID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("solid_generator"), ((syncId, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<VentScreenHandler> VENT = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("vent"), ((syncId, inventory, buffer) -> {
		return new VentScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<TankScreenHandler> TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("tank"), ((syncId, inventory, buffer) -> {
		return new TankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<NuclearWarheadScreenHandler> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("nuclear_warhead"), ((syncId, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<CapacitorScreenHandler> CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("capacitor"), ((syncId, inventory, buffer) -> {
		return new CapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<CreativeCapacitorScreenHandler> CREATIVE_CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_capacitor"), ((syncId, inventory, buffer) -> {
		return new CreativeCapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<CreativeTankScreenHandler> CREATIVE_TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_tank"), ((syncId, inventory, buffer) -> {
		return new CreativeTankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<CreativeBufferScreenHandler> CREATIVE_BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_buffer"), ((syncId, inventory, buffer) -> {
		return new CreativeBufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<BufferScreenHandler> BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("buffer"), ((syncId, inventory, buffer) -> {
		return new BufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<TrituratorScreenHandler> TRITURATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("triturator"), ((syncId, inventory, buffer) -> {
		return new TrituratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<PressScreenHandler> PRESSER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("press"), ((syncId, inventory, buffer) -> {
		return new PressScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<WireMillScreenHandler> WIRE_MILL = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("wire_mill"), ((syncId, inventory, buffer) -> {
		return new WireMillScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<ElectricFurnaceScreenHandler> ELECTRIC_FURNACE = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electric_furnace"), ((syncId, inventory, buffer) -> {
		return new ElectricFurnaceScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<ElectrolyzerScreenHandler> ELECTROLYZER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electrolyzer"), ((syncId, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<RefineryScreenHandler> REFINERY = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("refinery"), ((syncId, inventory, buffer) -> {
		return new RefineryScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<FluidMixerScreenHandler> FLUID_MIXER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_mixer"), ((syncId, inventory, buffer) -> {
		return new FluidMixerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<AlloySmelterScreenHandler> ALLOY_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("alloy_smelter"), ((syncId, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<SolidifierScreenHandler> SOLIDIFIER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("solidifier"), ((syncId, inventory, buffer) -> {
		return new SolidifierScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final MenuType<MelterScreenHandler> MELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("melter"), ((syncId, inventory, buffer) -> {
		return new MelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static void initialize() {

	}
}
