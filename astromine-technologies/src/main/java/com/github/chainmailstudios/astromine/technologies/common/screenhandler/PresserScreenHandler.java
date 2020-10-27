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

import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.common.widget.vanilla.ExtractionSlot;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.PresserBlockEntity;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;

public class PresserScreenHandler extends ComponentBlockEntityEnergyItemScreenHandler {
	private PresserBlockEntity presser;

	public PresserScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineTechnologiesScreenHandlers.PRESSER, syncId, player, position);

		presser = (PresserBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget output = new SlotWidget(0, blockEntity, ExtractionSlot::new);
		output.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		output.setSize(Size.of(18, 18));

		SlotWidget input = new SlotWidget(1, blockEntity);
		input.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		input.setSize(Size.of(18, 18));

		output.setPosition(Position.of(width / 2F - output.getWidth() / 2F, output.getY()));
		output.setPosition(Position.of(output.getX() + 27, output.getY() + 15));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(output.getX() - 31, output.getY()));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> presser.limit);
		arrow.setProgressSupplier(() -> (int) presser.progress);

		input.setPosition(Position.of(arrow.getX() - 27, arrow.getY()));

		mainTab.addWidget(input);
		mainTab.addWidget(output);
		mainTab.addWidget(arrow);
	}
}
