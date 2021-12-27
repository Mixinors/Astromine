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

package com.github.mixinors.astromine.common.screenhandler;

import com.github.mixinors.astromine.common.screenhandler.base.block.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.block.entity.ElectrolyzerBlockEntity;

public class ElectrolyzerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private ElectrolyzerBlockEntity electrolyzer;

	public ElectrolyzerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ELECTROLYZER, syncId, player, position);

		electrolyzer = (ElectrolyzerBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		FluidBarWidget firstOutputFluidBar = new FluidBarWidget();
		firstOutputFluidBar.setSize( Size.of(fluidBar));
		firstOutputFluidBar.setStorage(blockEntity.getFluidStorage().getStorage(1));

		FluidBarWidget secondOutputFluidBar = new FluidBarWidget();
		secondOutputFluidBar.setSize(Size.of(fluidBar));

		secondOutputFluidBar.setStorage(blockEntity.getFluidStorage().getStorage(2));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> electrolyzer.limit);
		arrow.setProgressSupplier(() -> (int) electrolyzer.progress);

		firstOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F)); // fluidBar.getX() + fluidBar.getWidth() + 4 + 18 + 18, fluidBar.getY()));
		secondOutputFluidBar.setPosition( Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() + 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));

		mainTab.add(firstOutputFluidBar);
		mainTab.add(secondOutputFluidBar);
		mainTab.add(arrow);
	}
}
