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
package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.screenhandler.*;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class AstromineScreenHandlers {
	public static final ScreenHandlerType<FluidExtractorScreenHandler> FLUID_EXTRACTOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_extractor"), ((synchronizationID, inventory, buffer) -> {
		return new FluidExtractorScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidInserterScreenHandler> FLUID_INSERTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_inserter"), ((synchronizationID, inventory, buffer) -> {
		return new FluidInserterScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BlockBreakerScreenHandler> BLOCK_BREAKER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_breaker"), ((synchronizationID, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BlockPlacerScreenHandler> BLOCK_PLACER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_placer"), ((synchronizationID, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<LiquidGeneratorScreenHandler> LIQUID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("liquid_generator"), ((synchronizationID, inventory, buffer) -> {
		return new LiquidGeneratorScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<SolidGeneratorScreenHandler> SOLID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("solid_generator"), ((synchronizationID, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<VentScreenHandler> VENT = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("vent"), ((synchronizationID, inventory, buffer) -> {
		return new VentScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidTankScreenHandler> TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("tank"), ((synchronizationID, inventory, buffer) -> {
		return new FluidTankScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<NuclearWarheadScreenHandler> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("nuclear_warhead"), ((synchronizationID, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeCapacitorScreenHandler> CREATIVE_CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_capacitor"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeCapacitorScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeTankScreenHandler> CREATIVE_TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_tank"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeTankScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeBufferScreenHandler> CREATIVE_BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_buffer"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeBufferScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BufferScreenHandler> BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("buffer"), ((synchronizationID, inventory, buffer) -> {
		return new BufferScreenHandler(synchronizationID, inventory, buffer.readBlockPos(), buffer.readEnumConstant(BufferType.class));
	}));

	public static final ScreenHandlerType<TrituratorScreenHandler> TRITURATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("triturator"), ((synchronizationID, inventory, buffer) -> {
		return new TrituratorScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));


	public static final ScreenHandlerType<PresserScreenHandler> PRESSER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("presser"), ((synchronizationID, inventory, buffer) -> {
		return new PresserScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectricSmelterScreenHandler> ELECTRIC_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electric_smelter"), ((synchronizationID, inventory, buffer) -> {
		return new ElectricSmelterScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectrolyzerScreenHandler> ELECTROLYZER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electrolyzer"), ((synchronizationID, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidMixerScreenHandler> FLUID_MIXER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_mixer"), ((synchronizationID, inventory, buffer) -> {
		return new FluidMixerScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CraftingRecipeCreatorScreenHandler> CRAFTING_RECIPE_CREATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("crafting_recipe_creator"), ((synchronizationID, inventory, buffer) -> {
		return new CraftingRecipeCreatorScreenHandler(synchronizationID, inventory);
	}));

	public static final ScreenHandlerType<AlloySmelterScreenHandler> ALLOY_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("alloy_smelter"), ((synchronizationID, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static void initialize() {
		// Unused.
	}
}
