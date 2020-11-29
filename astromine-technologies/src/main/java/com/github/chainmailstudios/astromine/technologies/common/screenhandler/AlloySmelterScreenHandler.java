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

import com.github.chainmailstudios.astromine.common.screenhandler.base.block.ComponentBlockEntityEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.chainmailstudios.astromine.common.widget.vanilla.ExtractionSlot;
import com.github.chainmailstudios.astromine.technologies.common.block.entity.AlloySmelterBlockEntity;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesScreenHandlers;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class AlloySmelterScreenHandler extends ComponentBlockEntityEnergyItemScreenHandler {
	private AlloySmelterBlockEntity smelter;

	public AlloySmelterScreenHandler(int syncId, Player player, BlockPos position) {
		super(AstromineTechnologiesScreenHandlers.ALLOY_SMELTER, syncId, player, position);

		smelter = (AlloySmelterBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		SlotWidget firstInput = new SlotWidget(0, smelter);
		firstInput.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		firstInput.setSize(Size.of(18, 18));

		SlotWidget secondInput = new SlotWidget(1, smelter);
		secondInput.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		secondInput.setSize(Size.of(18, 18));

		SlotWidget output = new SlotWidget(2, smelter, ExtractionSlot::new);
		output.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		output.setSize(Size.of(18, 18));

		firstInput.setPosition(Position.of(width / 2F - firstInput.getWidth() / 2, firstInput.getY()));
		firstInput.setPosition(Position.of(firstInput.getX() - 22, firstInput.getY() + 15 - 9));

		secondInput.setPosition(Position.of(width / 2F - secondInput.getWidth() / 2, secondInput.getY()));
		secondInput.setPosition(Position.of(secondInput.getX() - 22, secondInput.getY() + 15 + 18 - 9));

		output.setPosition(Position.of(width / 2F - output.getWidth() / 2, output.getY()));
		output.setPosition(Position.of(secondInput.getX() + 57, secondInput.getY() - 9));

		HorizontalArrowWidget arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(output.getX() - 31, output.getY()));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> smelter.limit);
		arrow.setProgressSupplier(() -> (int) smelter.progress);

		mainTab.addWidget(firstInput);
		mainTab.addWidget(secondInput);
		mainTab.addWidget(output);
		mainTab.addWidget(arrow);
	}
}
