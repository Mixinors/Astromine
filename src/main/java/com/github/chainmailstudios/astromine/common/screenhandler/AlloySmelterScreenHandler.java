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

import com.github.chainmailstudios.astromine.common.block.entity.AlloySmelterBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.HorizontalBarWidget;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class AlloySmelterScreenHandler extends DefaultedEnergyItemScreenHandler {
	private AlloySmelterBlockEntity smelter;

	public AlloySmelterScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);

		smelter = (AlloySmelterBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget firstInput = new SlotWidget(0, smelter);
		firstInput.setPosition(new Position(energyBar.getPosition().getX(), energyBar.getPosition().getY()));
		firstInput.setSize(new Size(18, 18));

		SlotWidget secondInput = new SlotWidget(1, smelter);
		secondInput.setPosition(new Position(energyBar.getPosition().getX(), energyBar.getPosition().getY()));
		secondInput.setSize(new Size(18, 18));

		SlotWidget output = new SlotWidget(2, smelter);
		output.setPosition(new Position(energyBar.getPosition().getX(), energyBar.getPosition().getY()));
		output.setSize(new Size(18, 18));

		firstInput.setPosition(new Position(width / 2F - firstInput.getSize().getWidth() / 2, firstInput.getPosition().getY()));
		firstInput.setPosition(new Position(firstInput.getPosition().getX() - 22, firstInput.getPosition().getY() + 15 - 9));


		secondInput.setPosition(new Position(width / 2F - secondInput.getSize().getWidth() / 2, secondInput.getPosition().getY()));
		secondInput.setPosition(new Position(secondInput.getPosition().getX() - 22, secondInput.getPosition().getY() + 15 + 18 - 9));

		output.setPosition(new Position(width / 2F - output.getSize().getWidth() / 2, output.getPosition().getY()));
		output.setPosition(new Position(secondInput.getPosition().getX() + 57, secondInput.getPosition().getY() -9));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position(output.getPosition().getX() - 31, output.getPosition().getY()));
		arrow.setSize(new Size(22, 16));
		arrow.setLimitSupplier(() -> smelter.limit);
		arrow.setProgressSupplier(() -> (int) smelter.progress);

		addWidget(firstInput);
		addWidget(secondInput);
		addWidget(output);
		addWidget(arrow);
	}
}
