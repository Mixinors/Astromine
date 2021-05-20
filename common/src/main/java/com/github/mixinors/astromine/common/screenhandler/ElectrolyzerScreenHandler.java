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
import com.github.mixinors.astromine.common.block.entity.ElectrolyzerBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;

public class ElectrolyzerScreenHandler extends ComponentBlockEntityEnergyFluidScreenHandler {
	private final ElectrolyzerBlockEntity electrolyzer;

	public ElectrolyzerScreenHandler(int syncId, PlayerEntity player, BlockPos pos) {
		super(AMScreenHandlers.ELECTROLYZER, syncId, player, pos);

		electrolyzer = (ElectrolyzerBlockEntity) energyFluidBlockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		var firstOutputFluidBar = new VerticalFluidBarWidget();
		firstOutputFluidBar.setSize(Size.absolute(fluidBar));
		firstOutputFluidBar.setVolumeSupplier(() -> energyFluidBlockEntity.getFluidComponent().getSecond());

		var secondOutputFluidBar = new VerticalFluidBarWidget();
		secondOutputFluidBar.setSize(Size.absolute(fluidBar));

		secondOutputFluidBar.setVolumeSupplier(() -> energyFluidBlockEntity.getFluidComponent().getThird());

		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(electrolyzer::getLimit);
		arrow.setProgressSupplier(electrolyzer::getProgress);

		firstOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth(), -fluidBar.getHeight()/ 2.0F + arrow.getHeight()/ 2.0F)); // fluidBar.getX() + fluidBar.getWidth() + 4 + 18 + 18, fluidBar.getY()));
		secondOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() + 7 + fluidBar.getWidth(), -fluidBar.getHeight()/ 2.0F + arrow.getHeight()/ 2.0F));

		mainTab.addWidget(firstOutputFluidBar);
		mainTab.addWidget(secondOutputFluidBar);
		mainTab.addWidget(arrow);
	}
}
