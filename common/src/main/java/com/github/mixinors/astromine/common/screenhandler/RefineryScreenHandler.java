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

import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyFluidScreenHandler;
import com.github.mixinors.astromine.common.widget.blade.VerticalFluidBarWidget;
import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.block.entity.RefineryBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;

public class RefineryScreenHandler extends ComponentBlockEntityEnergyFluidScreenHandler {
	private RefineryBlockEntity refinery;

	public RefineryScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.REFINERY, syncId, player, position);

		refinery = (RefineryBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		VerticalFluidBarWidget outputFluidBar = new VerticalFluidBarWidget();
		outputFluidBar.setSize(Size.absolute(fluidBar));
		outputFluidBar.setVolumeSupplier(() -> blockEntity.getFluidComponent().getSecond());

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> refinery.limit);
		arrow.setProgressSupplier(() -> (int) refinery.progress);

		outputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		mainTab.addWidget(outputFluidBar);
		mainTab.addWidget(arrow);
	}
}
