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

package com.github.chainmailstudios.astromine.technologies.common.rei;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.AlloySmelterScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.ElectricSmelterScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.PresserScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.SolidGeneratorScreenHandler;
import com.github.chainmailstudios.astromine.technologies.common.screenhandler.TrituratorScreenHandler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.shedaniel.rei.server.ContainerContext;
import me.shedaniel.rei.server.ContainerInfo;
import me.shedaniel.rei.server.ContainerInfoHandler;
import me.shedaniel.rei.server.InventoryStackAccessor;
import me.shedaniel.rei.server.StackAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class AstromineTechnologiesServerContainerPlugin implements Runnable {
	public static final Identifier TRITURATING = AstromineCommon.identifier("triturating");
	public static final Identifier ELECTRIC_SMELTING = AstromineCommon.identifier("electric_smelting");
	public static final Identifier SOLID_GENERATING = AstromineCommon.identifier("solid_generating");
	public static final Identifier PRESSING = AstromineCommon.identifier("pressing");
	public static final Identifier ALLOY_SMELTING = AstromineCommon.identifier("alloy_smelting");

	@Override
	public void run() {
		ContainerInfoHandler.registerContainerInfo(TRITURATING, new SimpleContainerInfo<>(TrituratorScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(ELECTRIC_SMELTING, new SimpleContainerInfo<>(ElectricSmelterScreenHandler.class, 1, 1, 1));
		ContainerInfoHandler.registerContainerInfo(SOLID_GENERATING, new SimpleContainerInfo<>(SolidGeneratorScreenHandler.class, 1, 1, 0));
		ContainerInfoHandler.registerContainerInfo(PRESSING, new SimpleContainerInfo<>(PresserScreenHandler.class, 1, 1, 1));
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
			return gridStacks.stream().map(slotIndex -> new InventoryStackAccessor((Inventory) context.getContainer().syncBlockEntity, slotIndex)).collect(Collectors.toList());
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
