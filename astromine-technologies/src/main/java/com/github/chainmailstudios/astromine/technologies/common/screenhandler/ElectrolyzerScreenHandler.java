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

import com.github.chainmailstudios.astromine.technologies.common.block.entity.ElectrolyzerBlockEntity;
import com.github.chainmailstudios.astromine.common.component.SidedComponentProvider;
import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.FluidVerticalBarWidget;
import com.github.chainmailstudios.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ElectrolyzerScreenHandler extends ComponentBlockEntityEnergyFluidScreenHandler {
	private ElectrolyzerBlockEntity electrolyzer;

	public ElectrolyzerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineTechnologiesScreenHandlers.ELECTROLYZER, syncId, player, position);

		electrolyzer = (ElectrolyzerBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SidedComponentProvider sidedComponentProvider = blockEntity;

		FluidVerticalBarWidget firstOutputFluidBar = new FluidVerticalBarWidget();
		firstOutputFluidBar.setSize(Size.absolute(fluidBar));
		firstOutputFluidBar.setVolume(() -> sidedComponentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));

		FluidVerticalBarWidget secondOutputFluidBar = new FluidVerticalBarWidget();
		secondOutputFluidBar.setSize(Size.absolute(fluidBar));

		secondOutputFluidBar.setVolume(() -> sidedComponentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(2));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, fluidBar.getWidth() + 7, fluidBar.getHeight() / 2 - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> electrolyzer.limit);
		arrow.setProgressSupplier(() -> (int) electrolyzer.progress);

		firstOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F)); // fluidBar.getX() + fluidBar.getWidth() + 4 + 18 + 18, fluidBar.getY()));
		secondOutputFluidBar.setPosition(Position.of(arrow.getPosition(), 7 + fluidBar.getWidth() + 7 + fluidBar.getWidth(), -fluidBar.getHeight() / 2F + arrow.getHeight() / 2F));

		mainTab.addWidget(firstOutputFluidBar);
		mainTab.addWidget(secondOutputFluidBar);
		mainTab.addWidget(arrow);
	}
}
