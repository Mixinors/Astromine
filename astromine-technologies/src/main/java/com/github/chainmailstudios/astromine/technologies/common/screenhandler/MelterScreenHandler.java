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

import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyFluidItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.common.widget.vanilla.ExtractionSlot;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.MelterBlockEntity;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class MelterScreenHandler extends ComponentBlockEntityEnergyFluidItemScreenHandler {
	private MelterBlockEntity melter;

	public MelterScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineTechnologiesScreenHandlers.MELTER, syncId, player, position);

		melter = (MelterBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget output = new SlotWidget(0, melter, ExtractionSlot::new);
		output.setSize(Size.of(18, 18));
		output.setPosition(Position.of(fluidBar, 102, 15));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(output, -31, 0));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> melter.limit);
		arrow.setProgressSupplier(() -> (int) melter.progress);

		fluidBar.setPosition(Position.of(arrow, -27 - 6, -fluidBar.getHeight() / 2 + output.getHeight() / 2));

		mainTab.addWidget(output);
		mainTab.addWidget(arrow);
	}
}
