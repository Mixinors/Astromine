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

import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyFluidItemScreenHandler;
import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.widget.vanilla.ExtractionSlot;
import com.github.mixinors.astromine.common.block.entity.SolidifierBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;

public class SolidifierScreenHandler extends ComponentBlockEntityEnergyFluidItemScreenHandler {
	private final SolidifierBlockEntity solidifier;

	public SolidifierScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.SOLIDIFIER, syncId, player, position);

		solidifier = (SolidifierBlockEntity) energyFluidItemBlockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		var output = new SlotWidget(0, solidifier, ExtractionSlot::new);
		output.setSize(Size.of(18, 18));
		output.setPosition(Position.of(fluidBar, 102, 15));

		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(output, -31, 0));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> solidifier.limit);
		arrow.setProgressSupplier(() -> (int) solidifier.progress);

		fluidBar.setPosition(Position.of(arrow, -27 - 6, -fluidBar.getHeight() / 2 + output.getHeight() / 2));

		mainTab.addWidget(output);
		mainTab.addWidget(arrow);
	}
}
