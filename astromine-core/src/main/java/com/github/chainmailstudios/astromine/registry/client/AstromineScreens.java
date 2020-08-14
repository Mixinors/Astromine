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

package com.github.chainmailstudios.astromine.registry.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import com.github.chainmailstudios.astromine.client.screen.*;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;

@Environment(EnvType.CLIENT)
public class AstromineScreens {
	public static void initialize() {
		ScreenRegistry.register(AstromineScreenHandlers.FLUID_EXTRACTOR, FluidExtractorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.FLUID_INSERTER, FluidInserterHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.BLOCK_BREAKER, BlockBreakerHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.BLOCK_PLACER, BlockPlacerHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.LIQUID_GENERATOR, LiquidGeneratorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.SOLID_GENERATOR, SolidGeneratorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.VENT, VentHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.TANK, TankHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.NUCLEAR_WARHEAD, NuclearWarheadHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.CAPACITOR, CapacitorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_CAPACITOR, CreativeCapacitorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_TANK, CreativeTankHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.CREATIVE_BUFFER, CreativeBufferHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.BUFFER, BufferHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.TRITURATOR, TrituratorHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.PRESSER, PresserHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.ELECTRIC_SMELTER, ElectricSmelterHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.ELECTROLYZER, ElectrolyzerHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.FLUID_MIXER, FluidMixerHandledScreen::new);

		ScreenRegistry.register(AstromineScreenHandlers.ALLOY_SMELTER, AlloySmelterHandledScreen::new);
	}
}
