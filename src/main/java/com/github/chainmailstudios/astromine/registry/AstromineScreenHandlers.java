package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.screenhandler.BlockBreakerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.BlockPlacerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.CraftingRecipeCreatorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeBufferScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeCapacitorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.CreativeTankScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.ElectricSmelterScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.ElectrolyzerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidExtractorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidInserterScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidMixerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.FluidTankScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.LiquidGeneratorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.NuclearWarheadScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.PresserScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.SolidGeneratorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.TrituratorScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.VentScreenHandler;

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

	public static void initialize() {
		// Unused.
	}
}
