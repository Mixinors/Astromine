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
package com.github.chainmailstudios.astromine.client.screen.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedBlockEntityScreenHandler;
import com.github.chainmailstudios.astromine.common.screenhandler.base.DefaultedEnergyFluidScreenHandler;
import com.github.chainmailstudios.astromine.common.widget.WEnergyVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.common.widget.WFluidVolumeFractionalVerticalBar;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public abstract class DefaultedEnergyFluidHandledScreen<T extends DefaultedBlockEntityScreenHandler> extends DefaultedBlockEntityHandledScreen<T> {
	public WEnergyVolumeFractionalVerticalBar energyBar;
	public WFluidVolumeFractionalVerticalBar fluidBar;

	public DefaultedEnergyFluidHandledScreen(Text name, DefaultedEnergyFluidScreenHandler linkedScreenHandler, PlayerEntity player) {
		super(name, (T) linkedScreenHandler, player);

		energyBar = mainPanel.createChild(WEnergyVolumeFractionalVerticalBar::new, Position.of(mainPanel, 7, 20, 8), Size.of(24, 48));

		fluidBar = mainPanel.createChild(WFluidVolumeFractionalVerticalBar::new, Position.of(energyBar, energyBar.getWidth() + 4, 0, 8), Size.of(energyBar));

		DefaultedEnergyBlockEntity blockEntity = linkedScreenHandler.blockEntity;

		energyBar.setEnergyVolume(blockEntity::getEnergyVolume);
		fluidBar.setFluidVolume(() -> blockEntity.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).getVolume(0));
	}
}
