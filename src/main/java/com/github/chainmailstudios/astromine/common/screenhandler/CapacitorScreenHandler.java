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

import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.registry.AstromineScreenHandlers;
import com.github.vini2003.blade.common.data.Position;
import com.github.vini2003.blade.common.data.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CapacitorScreenHandler extends DefaultedEnergyItemScreenHandler {
	public CapacitorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AstromineScreenHandlers.CAPACITOR, syncId, player, position);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		energyBar.setPosition(new Position(width / 2F - energyBar.getSize().getWidth() / 2F, energyBar.getPosition().getY()));

		SlotWidget input = new SlotWidget(0, blockEntity);
		input.setPosition(new Position(mainTab.getPosition().getX() + 12, mainTab.getPosition().getY() + 36));
		input.setSize(new Size(18, 18));

		SlotWidget output = new SlotWidget(1, blockEntity);
		output.setPosition(new Position(mainTab.getPosition().getX() + 146, mainTab.getPosition().getY() + 36));
		output.setSize(new Size(18, 18));

		mainTab.addWidget(input);
		mainTab.addWidget(output);
	}
}
