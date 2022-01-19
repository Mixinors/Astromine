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

package com.github.mixinors.astromine.registry.common;

import java.util.stream.Collectors;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.rei.alloysmelting.AlloySmeltingDisplay;
import com.github.mixinors.astromine.client.rei.electricsmelting.ElectricSmeltingDisplay;
import com.github.mixinors.astromine.client.rei.pressing.PressingDisplay;
import com.github.mixinors.astromine.client.rei.solidgenerating.SolidGeneratingDisplay;
import com.github.mixinors.astromine.client.rei.triturating.TrituratingDisplay;
import com.github.mixinors.astromine.common.screenhandler.machine.AlloySmelterScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.machine.ElectricFurnaceScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.machine.PresserScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.machine.generator.SolidGeneratorScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.machine.TrituratorScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;
import me.shedaniel.rei.api.common.transfer.info.simple.SimplePlayerInventoryMenuInfo;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;

public class AMContainersInfoHandlers implements REIServerPlugin {
	public static final CategoryIdentifier<TrituratingDisplay> TRITURATING = CategoryIdentifier.of(AMCommon.id("triturating"));
	public static final CategoryIdentifier<ElectricSmeltingDisplay> ELECTRIC_SMELTING = CategoryIdentifier.of(AMCommon.id("electric_smelting"));
	public static final CategoryIdentifier<SolidGeneratingDisplay> SOLID_GENERATING = CategoryIdentifier.of(AMCommon.id("solid_generating"));
	public static final CategoryIdentifier<PressingDisplay> PRESSING = CategoryIdentifier.of(AMCommon.id("pressing"));
	public static final CategoryIdentifier<AlloySmeltingDisplay> ALLOY_SMELTING = CategoryIdentifier.of(AMCommon.id("alloy_smelting"));

	@Override
	public void registerMenuInfo(MenuInfoRegistry registry) {
		registry.register(TRITURATING, TrituratorScreenHandler.class,
			SimpleMenuInfoProvider.of(display -> new SimpleContainerInfo<>(display, 1)));
		registry.register(ELECTRIC_SMELTING, ElectricFurnaceScreenHandler.class,
			SimpleMenuInfoProvider.of(display -> new SimpleContainerInfo<>(display, 1)));
		registry.register(SOLID_GENERATING, SolidGeneratorScreenHandler.class,
			SimpleMenuInfoProvider.of(display -> new SimpleContainerInfo<>(display, 0)));
		registry.register(PRESSING, PresserScreenHandler.class,
			SimpleMenuInfoProvider.of(display -> new SimpleContainerInfo<>(display, 1)));
		registry.register(ALLOY_SMELTING, AlloySmelterScreenHandler.class,
			SimpleMenuInfoProvider.of(display -> new SimpleContainerInfo<>(display, 0, 1)));
	}

	private static class SimpleContainerInfo<T extends ExtendedBlockEntityScreenHandler, D extends Display> implements SimplePlayerInventoryMenuInfo<T, D> {
		private final D display;
		private final IntList gridStacks;

		public SimpleContainerInfo(D display, int... gridStacks) {
			this.display = display;
			this.gridStacks = new IntArrayList(gridStacks);
		}

		@Override
		public D getDisplay() {
			return display;
		}

		@Override
		public Iterable<SlotAccessor> getInputSlots(MenuInfoContext<T, ?, D> context) {
			return gridStacks.intStream()
				.mapToObj(value -> SlotAccessor.fromSlot(context.getMenu().getSlot(value)))
				.collect(Collectors.toList());
		}
	}
}
