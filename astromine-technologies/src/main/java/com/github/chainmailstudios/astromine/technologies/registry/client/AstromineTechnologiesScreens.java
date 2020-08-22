package com.github.chainmailstudios.astromine.technologies.registry.client;

import com.github.chainmailstudios.astromine.registry.client.AstromineScreens;
import com.github.chainmailstudios.astromine.technologies.client.screen.*;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class AstromineTechnologiesScreens extends AstromineScreens {
	public static void initialize() {
		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.FLUID_EXTRACTOR, FluidExtractorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.FLUID_INSERTER, FluidInserterHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.BLOCK_BREAKER, BlockBreakerHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.BLOCK_PLACER, BlockPlacerHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.LIQUID_GENERATOR, LiquidGeneratorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.SOLID_GENERATOR, SolidGeneratorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.VENT, VentHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.TANK, TankHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.NUCLEAR_WARHEAD, NuclearWarheadHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.CAPACITOR, CapacitorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.CREATIVE_CAPACITOR, CreativeCapacitorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.CREATIVE_TANK, CreativeTankHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.CREATIVE_BUFFER, CreativeBufferHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.BUFFER, BufferHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.TRITURATOR, TrituratorHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.PRESSER, PresserHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.ELECTRIC_SMELTER, ElectricSmelterHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.ELECTROLYZER, ElectrolyzerHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.FLUID_MIXER, FluidMixerHandledScreen::new);

		ScreenRegistry.register(AstromineTechnologiesScreenHandlers.ALLOY_SMELTER, AlloySmelterHandledScreen::new);
	}
}
