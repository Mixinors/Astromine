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
