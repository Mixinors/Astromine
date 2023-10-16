/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.client.screen.BodySelectorHandledScreen;
import com.github.mixinors.astromine.client.screen.RecipeCreatorHandledScreen;
import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

@Environment(EnvType.CLIENT)
public class AMScreens {
	public static void init() {
		register(AMScreenHandlers.RECIPE_CREATOR, RecipeCreatorHandledScreen::new);
		register(AMScreenHandlers.BODY_SELECTOR, BodySelectorHandledScreen::new);
		
		register(AMScreenHandlers.FLUID_EXTRACTOR);
		register(AMScreenHandlers.FLUID_INSERTER);
		register(AMScreenHandlers.BLOCK_BREAKER);
		register(AMScreenHandlers.BLOCK_PLACER);
		register(AMScreenHandlers.LIQUID_GENERATOR);
		register(AMScreenHandlers.SOLID_GENERATOR);
		register(AMScreenHandlers.TANK);
		register(AMScreenHandlers.NUCLEAR_WARHEAD);
		register(AMScreenHandlers.CAPACITOR);
		register(AMScreenHandlers.BUFFER);
		register(AMScreenHandlers.TRITURATOR);
		register(AMScreenHandlers.PRESSER);
		register(AMScreenHandlers.WIRE_MILL);
		register(AMScreenHandlers.ELECTRIC_FURNACE);
		register(AMScreenHandlers.ELECTROLYZER);
		register(AMScreenHandlers.REFINERY);
		register(AMScreenHandlers.FLUID_MIXER);
		register(AMScreenHandlers.ALLOY_SMELTER);
		register(AMScreenHandlers.SOLIDIFIER);
		register(AMScreenHandlers.MELTER);
		register(AMScreenHandlers.PUMP);
		
		register(AMScreenHandlers.ROCKET_CONTROLLER);
	}
	
	public static <H extends BaseScreenHandler> void register(RegistrySupplier<? extends ScreenHandlerType<? extends H>> type) {
		AMScreens.<H, CustomForegroundBaseHandledScreen<H>>register(type, CustomForegroundBaseHandledScreen::new);
	}
	
	public static <H extends ScreenHandler, S extends Screen & ScreenHandlerProvider<H>> void register(RegistrySupplier<? extends ScreenHandlerType<? extends H>> type, MenuRegistry.ScreenFactory<H, S> screenFactory) {
		MenuRegistry.registerScreenFactory(type.get(), screenFactory);
	}
}
