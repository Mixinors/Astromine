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

package com.github.chainmailstudios.astromine.technologies.common.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.FluidVerticalBarWidget;
import com.github.chainmailstudios.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.RefineryBlockEntity;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;

public class RefineryScreenHandler extends ComponentBlockEntityEnergyFluidScreenHandler {
	private RefineryBlockEntity refinery;

	public RefineryScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineTechnologiesScreenHandlers.REFINERY, syncId, player, position);

		refinery = (RefineryBlockEntity) blockEntity;
	}

	@Override
	public Size getTabsSize(int width, int height) {
		return Size.of(317, super.getTabsSize(width, height).getHeight());
	}

	@Override
	public Position getTabsPosition(int width, int height) {
		return super.getTabsPosition(width, height).offset(-(317 / 2 - tabs.getWidth() / 2), 0);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		FluidVerticalBarWidget firstOutputFluidBar = new FluidVerticalBarWidget();
		firstOutputFluidBar.setSize(Size.absolute(fluidBar));
		firstOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getSecond());

		FluidVerticalBarWidget secondOutputFluidBar = new FluidVerticalBarWidget();
		secondOutputFluidBar.setSize(Size.absolute(fluidBar));
		secondOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getThird());

		FluidVerticalBarWidget thirdOutputFluidBar = new FluidVerticalBarWidget();
		thirdOutputFluidBar.setSize(Size.absolute(fluidBar));
		thirdOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getFourth());

		FluidVerticalBarWidget fourthOutputFluidBar = new FluidVerticalBarWidget();
		fourthOutputFluidBar.setSize(Size.absolute(fluidBar));
		fourthOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getFifth());

		FluidVerticalBarWidget fifthOutputFluidBar = new FluidVerticalBarWidget();
		fifthOutputFluidBar.setSize(Size.absolute(fluidBar));
		fifthOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getSixth());

		FluidVerticalBarWidget sixthOutputFluidBar = new FluidVerticalBarWidget();
		sixthOutputFluidBar.setSize(Size.absolute(fluidBar));
		sixthOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getSeventh());

		FluidVerticalBarWidget seventhOutputFluidBar = new FluidVerticalBarWidget();
		seventhOutputFluidBar.setSize(Size.absolute(fluidBar));
		seventhOutputFluidBar.setVolume(() -> blockEntity.getFluidComponent().getEighth());

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> refinery.limit);
		arrow.setProgressSupplier(() -> (int) refinery.progress);

		firstOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		secondOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() + 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		thirdOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() * 2 + 7 * 2 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		fourthOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() * 3 + 7 * 3 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		fifthOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() * 4 + 7 * 4 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		sixthOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() * 5 + 7 * 5 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));
		seventhOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() * 6 + 7 * 6 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));

		mainTab.addWidget(firstOutputFluidBar);
		mainTab.addWidget(secondOutputFluidBar);
		mainTab.addWidget(thirdOutputFluidBar);
		mainTab.addWidget(fourthOutputFluidBar);
		mainTab.addWidget(fifthOutputFluidBar);
		mainTab.addWidget(sixthOutputFluidBar);
		mainTab.addWidget(seventhOutputFluidBar);
		mainTab.addWidget(arrow);
	}
}
