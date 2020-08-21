package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.type.BufferType;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.*;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class AstromineTechnologiesScreenHandlers extends AstromineScreenHandlers {
	public static final ScreenHandlerType<FluidExtractorScreenHandler> FLUID_EXTRACTOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_extractor"), ((syncId, inventory, buffer) -> {
		return new FluidExtractorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidInserterScreenHandler> FLUID_INSERTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_inserter"), ((syncId, inventory, buffer) -> {
		return new FluidInserterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BlockBreakerScreenHandler> BLOCK_BREAKER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_breaker"), ((syncId, inventory, buffer) -> {
		return new BlockBreakerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BlockPlacerScreenHandler> BLOCK_PLACER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("block_placer"), ((syncId, inventory, buffer) -> {
		return new BlockPlacerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<LiquidGeneratorScreenHandler> LIQUID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("liquid_generator"), ((syncId, inventory, buffer) -> {
		return new LiquidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<SolidGeneratorScreenHandler> SOLID_GENERATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("solid_generator"), ((syncId, inventory, buffer) -> {
		return new SolidGeneratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<VentScreenHandler> VENT = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("vent"), ((syncId, inventory, buffer) -> {
		return new VentScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<TankScreenHandler> TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("tank"), ((syncId, inventory, buffer) -> {
		return new TankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<NuclearWarheadScreenHandler> NUCLEAR_WARHEAD = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("nuclear_warhead"), ((syncId, inventory, buffer) -> {
		return new NuclearWarheadScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CapacitorScreenHandler> CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("capacitor"), ((syncId, inventory, buffer) -> {
		return new CapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeCapacitorScreenHandler> CREATIVE_CAPACITOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_capacitor"), ((syncId, inventory, buffer) -> {
		return new CreativeCapacitorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeTankScreenHandler> CREATIVE_TANK = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_tank"), ((syncId, inventory, buffer) -> {
		return new CreativeTankScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<CreativeBufferScreenHandler> CREATIVE_BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("creative_buffer"), ((syncId, inventory, buffer) -> {
		return new CreativeBufferScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<BufferScreenHandler> BUFFER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("buffer"), ((syncId, inventory, buffer) -> {
		return new BufferScreenHandler(syncId, inventory.player, buffer.readBlockPos(), buffer.readEnumConstant(BufferType.class));
	}));

	public static final ScreenHandlerType<TrituratorScreenHandler> TRITURATOR = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("triturator"), ((syncId, inventory, buffer) -> {
		return new TrituratorScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<PresserScreenHandler> PRESSER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("presser"), ((syncId, inventory, buffer) -> {
		return new PresserScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectricSmelterScreenHandler> ELECTRIC_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electric_smelter"), ((syncId, inventory, buffer) -> {
		return new ElectricSmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<ElectrolyzerScreenHandler> ELECTROLYZER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("electrolyzer"), ((syncId, inventory, buffer) -> {
		return new ElectrolyzerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<FluidMixerScreenHandler> FLUID_MIXER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("fluid_mixer"), ((syncId, inventory, buffer) -> {
		return new FluidMixerScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static final ScreenHandlerType<AlloySmelterScreenHandler> ALLOY_SMELTER = ScreenHandlerRegistry.registerExtended(AstromineCommon.identifier("alloy_smelter"), ((syncId, inventory, buffer) -> {
		return new AlloySmelterScreenHandler(syncId, inventory.player, buffer.readBlockPos());
	}));

	public static void initialize() {
		// Unused.
	}
}
