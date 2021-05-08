/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.client.screen.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

import com.github.mixinors.astromine.registry.common.AMScreenHandlers;

@Environment(EnvType.CLIENT)
public class AMScreens {
	public static void init() {
		ScreenRegistry.register(AMScreenHandlers.RECIPE_CREATOR, RecipeCreatorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.ROCKET, RocketHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.FLUID_EXTRACTOR, FluidCollectorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.FLUID_INSERTER, FluidPlacerHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.BLOCK_BREAKER, BlockBreakerHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.BLOCK_PLACER, BlockPlacerHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.LIQUID_GENERATOR, FluidGeneratorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.SOLID_GENERATOR, SolidGeneratorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.VENT, VentHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.TANK, TankHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.NUCLEAR_WARHEAD, NuclearWarheadHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.CAPACITOR, CapacitorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.CREATIVE_CAPACITOR, CreativeCapacitorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.CREATIVE_TANK, CreativeTankHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.CREATIVE_BUFFER, CreativeBufferHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.BUFFER, BufferHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.TRITURATOR, TrituratorHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.PRESSER, PressHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.WIREMILL, WireMillHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.ELECTRIC_FURNACE, ElectricFurnaceHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.ELECTROLYZER, ElectrolyzerHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.REFINERY, RefineryHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.FLUID_MIXER, FluidMixerHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.ALLOY_SMELTER, AlloySmelterHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.SOLIDIFIER, SolidifierHandledScreen::new);
		
		ScreenRegistry.register(AMScreenHandlers.MELTER, MelterHandledScreen::new);
	}
}
