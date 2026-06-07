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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import java.util.function.Supplier;

public class AMScreens {
	public static void init(IEventBus modBus) {
		modBus.addListener((RegisterMenuScreensEvent event) -> register(event));
	}
	
	private static void register(RegisterMenuScreensEvent event) {
		register(event, AMScreenHandlers.RECIPE_CREATOR, RecipeCreatorHandledScreen::new);
		register(event, AMScreenHandlers.BODY_SELECTOR, BodySelectorHandledScreen::new);
		
		registerSimple(event, AMScreenHandlers.FLUID_EXTRACTOR);
		registerSimple(event, AMScreenHandlers.FLUID_INSERTER);
		registerSimple(event, AMScreenHandlers.BLOCK_BREAKER);
		registerSimple(event, AMScreenHandlers.BLOCK_PLACER);
		registerSimple(event, AMScreenHandlers.LIQUID_GENERATOR);
		registerSimple(event, AMScreenHandlers.SOLID_GENERATOR);
		registerSimple(event, AMScreenHandlers.TANK);
		registerSimple(event, AMScreenHandlers.NUCLEAR_WARHEAD);
		registerSimple(event, AMScreenHandlers.CAPACITOR);
		registerSimple(event, AMScreenHandlers.BUFFER);
		registerSimple(event, AMScreenHandlers.TRITURATOR);
		registerSimple(event, AMScreenHandlers.PRESSER);
		registerSimple(event, AMScreenHandlers.WIRE_MILL);
		registerSimple(event, AMScreenHandlers.ELECTRIC_FURNACE);
		registerSimple(event, AMScreenHandlers.ELECTROLYZER);
		registerSimple(event, AMScreenHandlers.REFINERY);
		registerSimple(event, AMScreenHandlers.FLUID_MIXER);
		registerSimple(event, AMScreenHandlers.ALLOY_SMELTER);
		registerSimple(event, AMScreenHandlers.SOLIDIFIER);
		registerSimple(event, AMScreenHandlers.MELTER);
		registerSimple(event, AMScreenHandlers.PUMP);
		
		registerSimple(event, AMScreenHandlers.ROCKET_CONTROLLER);
		registerSimple(event, AMScreenHandlers.STATION_CONTROLLER);
	}
	
	public static <H extends AbstractContainerMenu> void registerSimple(RegisterMenuScreensEvent event, Supplier<? extends MenuType<? extends H>> type) {
		AMScreens.<H, CustomForegroundBaseHandledScreen<H>>register(event, type, CustomForegroundBaseHandledScreen::new);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void register(RegisterMenuScreensEvent event, Supplier<? extends MenuType<? extends H>> type, MenuScreens.ScreenConstructor<H, S> screenFactory) {
		event.register((MenuType) type.get(), (MenuScreens.ScreenConstructor) screenFactory);
	}
}
