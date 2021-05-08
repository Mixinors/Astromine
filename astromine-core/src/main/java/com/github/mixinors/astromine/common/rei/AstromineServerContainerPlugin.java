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

package com.github.mixinors.astromine.common.rei;

import com.github.mixinors.astromine.AMCommon;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.AlloySmelterScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.ElectricFurnaceScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.PressScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.SolidGeneratorScreenHandler;
import com.github.mixinors.astromine.common.screenhandler.TrituratorScreenHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.server.ContainerContext;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.server.ContainerInfoHandler;
import me.shedaniel.rei.server.InventoryStackAccessor;
import me.shedaniel.rei.server.StackAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class AstromineServerContainerPlugin implements Runnable {
	public static final Identifier TRITURATING = AMCommon.id("triturating");
	public static final Identifier ELECTRIC_SMELTING = AMCommon.id("electric_smelting");
	public static final Identifier SOLID_GENERATING = AMCommon.id("solid_generating");
	public static final Identifier PRESSING = AMCommon.id("pressing");
	public static final Identifier ALLOY_SMELTING = AMCommon.id("alloy_smelting");

	@Override
	public void run() {
		ContainerInfoHandler.registerContainerInfo(TRITURATING, new SimpleContainerInfo<>(TrituratorScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(ELECTRIC_SMELTING, new SimpleContainerInfo<>(ElectricFurnaceScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(SOLID_GENERATING, new SimpleContainerInfo<>(SolidGeneratorScreenHandler.class, 1, 1, 0));
		ContainerInfoHandler.registerContainerInfo(PRESSING, new SimpleContainerInfo<>(PressScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(ALLOY_SMELTING, new SimpleContainerInfo<>(AlloySmelterScreenHandler.class, 1, 2, 0, 1));
	}

	private static class SimpleContainerInfo<T extends ComponentBlockEntityScreenHandler> implements ContainerInfo<T> {
		private final Class<T> clazz;
		private final int width;
		private final int height;
		private final IntList gridStacks;

		public SimpleContainerInfo(Class<T> clazz, int width, int height, int... gridStacks) {
			this.clazz = clazz;
			this.width = width;
			this.height = height;
			this.gridStacks = new IntArrayList(gridStacks);
		}

		@Override
		public Class<? extends ScreenHandler> getContainerClass() {
			return clazz;
		}

		@Override
		public List<StackAccessor> getGridStacks(ContainerContext<T> context) {
			return gridStacks.stream().map(slotIndex -> new InventoryStackAccessor((Inventory) context.getContainer().getBlockEntity(), slotIndex)).collect(Collectors.toList());
		}

		@Override
		public int getCraftingResultSlotIndex(T t) {
			return Integer.MAX_VALUE;
		}

		@Override
		public int getCraftingWidth(T t) {
			return width;
		}

		@Override
		public int getCraftingHeight(T t) {
			return height;
		}
	}
}
