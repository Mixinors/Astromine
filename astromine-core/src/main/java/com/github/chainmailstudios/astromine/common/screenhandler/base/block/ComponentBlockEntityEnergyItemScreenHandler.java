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

package com.github.chainmailstudios.astromine.common.screenhandler.base.block;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentEnergyItemBlockEntity;
import com.github.chainmailstudios.astromine.common.widget.blade.EnergyVerticalBarWidget;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

/**
 *A {@link ComponentBlockEntityScreenHandler}
 * with an attached {@link ComponentEnergyItemBlockEntity}.
 */
public class ComponentBlockEntityEnergyItemScreenHandler extends ComponentBlockEntityScreenHandler {
	public ComponentEnergyItemBlockEntity blockEntity;

	public EnergyVerticalBarWidget energyBar;

	/** Instantiates a {@link ComponentBlockEntityItemScreenHandler},
	 * obtaining the {@link ComponentEnergyItemBlockEntity}. */
	public ComponentBlockEntityEnergyItemScreenHandler(MenuType<?> type, int syncId, Player player, BlockPos position) {
		super(type, syncId, player, position);

		blockEntity = (ComponentEnergyItemBlockEntity) player.level.getBlockEntity(position);
	}

	/** Override behavior to add an energy bar. */
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		energyBar = new EnergyVerticalBarWidget();
		energyBar.setPosition(Position.of(mainTab, 7F, 11));
		energyBar.setSize(Size.of(24F, 48F));
		energyBar.setVolumeSupplier(() -> blockEntity.getEnergyComponent().getVolume());

		mainTab.addWidget(energyBar);
	}
}
