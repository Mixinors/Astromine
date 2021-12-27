/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.screenhandler;

import com.github.mixinors.astromine.common.screenhandler.base.block.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.widget.vanilla.ExtractionSlot;
import com.github.mixinors.astromine.common.block.entity.AlloySmelterBlockEntity;

public class AlloySmelterScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final AlloySmelterBlockEntity smelter;

	public AlloySmelterScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ALLOY_SMELTER, syncId, player, position);

		smelter = (AlloySmelterBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var firstInput = new SlotWidget(0, smelter.getItemStorage());
		firstInput.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		firstInput.setSize( Size.of(18, 18));
		
		var secondInput = new SlotWidget(1, smelter.getItemStorage());
		secondInput.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		secondInput.setSize(Size.of(18, 18));
		
		var output = new SlotWidget(2, smelter.getItemStorage(), ExtractionSlot::new);
		output.setPosition(Position.of(energyBar.getX(), energyBar.getY()));
		output.setSize(Size.of(18, 18));

		firstInput.setPosition(Position.of(width / 2F - firstInput.getWidth() / 2, firstInput.getY()));
		firstInput.setPosition(Position.of(firstInput.getX() - 22, firstInput.getY() + 15 - 9));

		secondInput.setPosition(Position.of(width / 2F - secondInput.getWidth() / 2, secondInput.getY()));
		secondInput.setPosition(Position.of(secondInput.getX() - 22, secondInput.getY() + 15 + 18 - 9));

		output.setPosition(Position.of(width / 2F - output.getWidth() / 2, output.getY()));
		output.setPosition(Position.of(secondInput.getX() + 57, secondInput.getY() - 9));
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(output.getX() - 31, output.getY()));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> smelter.limit);
		arrow.setProgressSupplier(() -> (int) smelter.progress);

		mainTab.add(firstInput);
		mainTab.add(secondInput);
		mainTab.add(output);
		mainTab.add(arrow);
	}
}
