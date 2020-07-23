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

import com.github.chainmailstudios.astromine.client.screen.base.DefaultedEnergyFluidHandledScreen;
import com.github.chainmailstudios.astromine.common.block.entity.ElectrolyzerBlockEntity;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.screenhandler.ElectrolyzerScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.common.widget.WHorizontalArrow;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class ElectrolyzerHandledScreen extends DefaultedEnergyFluidHandledScreen<ElectrolyzerScreenHandler> {
	public ElectrolyzerHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, linkedScreenHandler, player);

		ElectrolyzerBlockEntity electrolyzer = (ElectrolyzerBlockEntity) linkedScreenHandler.blockEntity;

		ComponentProvider componentProvider = linkedScreenHandler.blockEntity;

		WFluidVolumeFractionalVerticalBar firstOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(fluidBar, fluidBar.getWidth() + 4 + 18 + 18, 0, 0), Size.of(fluidBar));

		firstOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(1));

		WFluidVolumeFractionalVerticalBar secondOutputFluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(firstOutputFluidBar, firstOutputFluidBar.getWidth() + 7, 0, 0), Size.of(fluidBar));

		secondOutputFluidBar.setFluidVolume(() -> componentProvider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(2));

		mainPanel.createChild(WHorizontalArrow::new, Position.of(fluidBar, fluidBar.getWidth() + 9, fluidBar.getHeight() / 2 - 8, 0), Size.of(22, 16))
				.setLimitSupplier(() -> electrolyzer.limit)
				.setProgressSupplier(() -> (int) electrolyzer.current);
	}
}
