package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.container.FluidTankContainer;
import com.github.chainmailstudios.astromine.common.container.FuelGeneratorContainer;
import com.github.chainmailstudios.astromine.common.container.NuclearWarheadContainer;
import com.github.chainmailstudios.astromine.common.container.VentContainer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class AstromineContainers {
	public static final ScreenHandlerType<FuelGeneratorContainer> FUEL_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fuel_generator"), ((synchronizationID, inventory, buffer) -> {
		return new FuelGeneratorContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<VentContainer> VENT = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("vent"), ((synchronizationID, inventory, buffer) -> {
		return new VentContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidTankContainer> FLUID_TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_tank"), ((synchronizationID, inventory, buffer) -> {
		return new FluidTankContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<NuclearWarheadContainer> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("nuclear_warhead"), ((synchronizationID, inventory, buffer) -> {
		return new NuclearWarheadContainer(synchronizationID, inventory, buffer.readBlockPos());
	}));

	public static void initialize() {
		// Unused.
	}
}
