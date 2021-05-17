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

import com.github.mixinors.astromine.client.screen.RecipeCreatorHandledScreen;
import com.github.mixinors.astromine.client.screen.base.CustomForegroundBaseHandledScreen;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import com.github.vini2003.blade.common.handler.BaseScreenHandler;
import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

@Environment(EnvType.CLIENT)
public class AMScreens {
	public static void init() {
		ClientLifecycleEvent.CLIENT_SETUP.register(client -> {
			register(AMScreenHandlers.RECIPE_CREATOR, RecipeCreatorHandledScreen::new);
			registerSimple(AMScreenHandlers.ROCKET);
			registerSimple(AMScreenHandlers.FLUID_EXTRACTOR);
			registerSimple(AMScreenHandlers.FLUID_INSERTER);
			registerSimple(AMScreenHandlers.BLOCK_BREAKER);
			registerSimple(AMScreenHandlers.BLOCK_PLACER);
			registerSimple(AMScreenHandlers.LIQUID_GENERATOR);
			registerSimple(AMScreenHandlers.SOLID_GENERATOR);
			registerSimple(AMScreenHandlers.VENT);
			registerSimple(AMScreenHandlers.TANK);
			registerSimple(AMScreenHandlers.NUCLEAR_WARHEAD);
			registerSimple(AMScreenHandlers.CAPACITOR);
			registerSimple(AMScreenHandlers.CREATIVE_CAPACITOR);
			registerSimple(AMScreenHandlers.CREATIVE_TANK);
			registerSimple(AMScreenHandlers.CREATIVE_BUFFER);
			registerSimple(AMScreenHandlers.BUFFER);
			registerSimple(AMScreenHandlers.TRITURATOR);
			registerSimple(AMScreenHandlers.PRESSER);
			registerSimple(AMScreenHandlers.WIREMILL);
			registerSimple(AMScreenHandlers.ELECTRIC_FURNACE);
			registerSimple(AMScreenHandlers.ELECTROLYZER);
			registerSimple(AMScreenHandlers.REFINERY);
			registerSimple(AMScreenHandlers.FLUID_MIXER);
			registerSimple(AMScreenHandlers.ALLOY_SMELTER);
			registerSimple(AMScreenHandlers.SOLIDIFIER);
			registerSimple(AMScreenHandlers.MELTER);
		});
	}

	public static <H extends BaseScreenHandler> void registerSimple(RegistrySupplier<? extends ScreenHandlerType<? extends H>> type) {
		AMScreens.<H, CustomForegroundBaseHandledScreen<H>>register(type, CustomForegroundBaseHandledScreen::new);
	}

	public static <H extends ScreenHandler, S extends Screen & ScreenHandlerProvider<H>> void register(RegistrySupplier<? extends ScreenHandlerType<? extends H>> type, MenuRegistry.ScreenFactory<H, S> screenFactory) {
		MenuRegistry.registerScreenFactory(type.get(), screenFactory);
	}
}
