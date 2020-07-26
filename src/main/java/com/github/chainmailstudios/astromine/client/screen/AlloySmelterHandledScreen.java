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

package com.github.chainmailstudios.astromine.client.screen;

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyItemHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.AlloySmelterBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.AlloySmelterScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyItemScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.WSlot;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class AlloySmelterHandledScreen extends DefaultedEnergyItemHandledScreen<AlloySmelterScreenHandler> {
	public AlloySmelterHandledScreen(Text name, DefaultedEnergyItemScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		AlloySmelterBlockEntity smelter = (AlloySmelterBlockEntity) linkedScreenHandler.blockEntity;

		WSlot firstInput = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(0);
		WSlot secondInput = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(1);
		WSlot output = mainPanel.createChild(WSlot::new, Position.of(energyBar), Size.of(18, 18)).setInventoryNumber(1).setSlotNumber(2);

		firstInput.centerX();
		firstInput.setPosition(Position.of(firstInput.getX() - 22, firstInput.getY() + 15 - 9, firstInput.getZ()));
		secondInput.centerX();
		secondInput.setPosition(Position.of(secondInput.getX() - 22, secondInput.getY() + 15 + 18 - 9, secondInput.getZ()));
		output.centerX();
		output.setPosition(Position.of(secondInput, 57, -9, 0));

		mainPanel.createChild(WHorizontalArrow::new, Position.of(output, -31, 0, 0), Size.of(22, 16)).setLimitSupplier(() -> smelter.limit).setProgressSupplier(() -> (int) smelter.progress);
	}
}
