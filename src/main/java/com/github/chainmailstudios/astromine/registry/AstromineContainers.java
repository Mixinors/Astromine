package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.client.screen.ElectrolyzerContainerScreen;
import com.github.chainmailstudios.astromine.common.container.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

import com.github.chainmailstudios.astromine.AstromineCommon;

public class AstromineContainers {
	public static final ScreenHandlerType<FluidExtractorContainer> FLUID_EXTRACTOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_extractor"), ((synchronizationID, inventory, buffer) -> {
		return new FluidExtractorContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<LiquidGeneratorContainer> LIQUID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("liquid_generator"), ((synchronizationID, inventory, buffer) -> {
		return new LiquidGeneratorContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<VentContainer> VENT = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("vent"), ((synchronizationID, inventory, buffer) -> {
		return new VentContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidTankContainer> TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("tank"), ((synchronizationID, inventory, buffer) -> {
		return new FluidTankContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<NuclearWarheadContainer> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("nuclear_warhead"), ((synchronizationID, inventory, buffer) -> {
		return new NuclearWarheadContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeCapacitorContainer> CREATIVE_CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_capacitor"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeCapacitorContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeTankContainer> CREATIVE_TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_tank"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeTankContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeBufferContainer> CREATIVE_BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_buffer"), ((synchronizationID, inventory, buffer) -> {
		return new CreativeBufferContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<SorterContainer> SORTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("sorter"), ((synchronizationID, inventory, buffer) -> {
		return new SorterContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectricSmelterContainer> ELECTRIC_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electric_smelter"), ((synchronizationID, inventory, buffer) -> {
		return new ElectricSmelterContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectrolyzerContainer> ELECTROLYZER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electrolyzer"), ((synchronizationID, inventory, buffer) -> {
		return new ElectrolyzerContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FuelMixerContainer> FUEL_MIXER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fuel_mixer"), ((synchronizationID, inventory, buffer) -> {
		return new FuelMixerContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static void initialize() {
		// Unused.
	}
}
