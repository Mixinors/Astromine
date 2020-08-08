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

import com.github.chainmailstudios.astromine.common.block.entity.TrituratorBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TrituratorScreenHandler extends DefaultedEnergyItemScreenHandler {
	private TrituratorBlockEntity triturator;

	public TrituratorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineScreenHandlers.TRITURATOR, syncId, player, position);

		triturator = (TrituratorBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget input = new SlotWidget(0, blockEntity);
		input.setPosition(new Position(energyBar.getPosition().getX(), energyBar.getPosition().getY()));
		input.setSize(new Size(18, 18));

		SlotWidget output = new SlotWidget(1, blockEntity);
		input.setPosition(new Position(energyBar.getPosition().getX(), energyBar.getPosition().getY()));
		input.setSize(new Size(18, 18));

		input.setPosition(new Position(width / 2F - input.getSize().getWidth() / 2F, input.getPosition().getY()));
		input.setPosition(new Position(input.getPosition().getX() + 29, input.getPosition().getY() + 15));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position(input.getPosition().getX() - 31, input.getPosition().getY()));
		arrow.setSize(new Size(22, 16));
		arrow.setLimitSupplier(() -> triturator.limit);
		arrow.setProgressSupplier(() -> (int) triturator.progress);

		output.setPosition(new Position(width / 2F - output.getSize().getWidth() / 2F, output.getPosition().getY()));
		output.setPosition(new Position(arrow.getPosition().getX() - 27, arrow.getPosition().getY()));
	}
}
