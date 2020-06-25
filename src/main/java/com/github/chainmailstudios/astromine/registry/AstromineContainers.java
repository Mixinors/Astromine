package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.container.CreativeBufferContainer;
import com.github.chainmailstudios.astromine.common.container.CreativeCapacitorContainer;
import com.github.chainmailstudios.astromine.common.container.CreativeTankContainer;
import com.github.chainmailstudios.astromine.common.container.ElectricalSmelterContainer;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import com.github.chainmailstudios.astromine.common.container.LiquidGeneratorContainer;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;
import com.github.chainmailstudios.astromine.common.container.SorterContainer;
import com.github.chainmailstudios.astromine.common.container.VentContainer;

public class AstromineContainers {
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

	public static final ScreenHandlerType<ElectricalSmelterContainer> ELECTRICAL_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electrical_smelter"), ((synchronizationID, inventory, buffer) -> {
		return new ElectricalSmelterContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static void initialize() {
		// Unused.
	}
}
