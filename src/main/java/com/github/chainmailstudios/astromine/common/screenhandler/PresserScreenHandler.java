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

package com.github.chainmailstudios.astromine.common.screenhandler;

import com.github.chainmailstudios.astromine.common.block.entity.PresserBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PresserScreenHandler extends DefaultedEnergyItemScreenHandler {
	private PresserBlockEntity sorter;

	public PresserScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineScreenHandlers.PRESSER, syncId, player, position);

		sorter = (PresserBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget input = new SlotWidget(0, blockEntity);
		input.setPosition(new Position(energyBar.getX(), energyBar.getY()));
		input.setSize(new Size(18, 18));

		SlotWidget output = new SlotWidget(1, blockEntity);
		output.setPosition(new Position(energyBar.getX(), energyBar.getY()));
		output.setSize(new Size(18, 18));

		input.setPosition(new Position(width / 2F - input.getWidth() / 2F, input.getY()));
		input.setPosition(new Position(input.getX() + 27, input.getY() + 15));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position(input.getX() - 31, input.getY()));
		arrow.setSize(new Size(22, 16));
		arrow.setLimitSupplier(() -> sorter.limit);
		arrow.setProgressSupplier(() -> (int) sorter.progress);

		output.setPosition(new Position(arrow.getX() - 27, arrow.getY()));

		mainTab.addWidget(input);
		mainTab.addWidget(output);
		mainTab.addWidget(arrow);
	}
}
