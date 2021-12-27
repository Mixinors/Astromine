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
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.block.entity.MelterBlockEntity;

public class MelterScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final MelterBlockEntity melter;

	public MelterScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.MELTER, syncId, player, position);

		melter = (MelterBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		var input = new SlotWidget(0, melter.getItemStorage(), Slot::new);
		input.setSize( Size.of(18, 18));

		fluidBar.setPosition( Position.of(energyBar, 102, 0));
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(fluidBar, -31, fluidBar.getHeight() / 2 - 16 / 2));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> melter.limit);
		arrow.setProgressSupplier(() -> (int) melter.progress);
		
		input.setPosition(Position.of(arrow, -27, 0));
		
		mainTab.add(input);
		mainTab.add(arrow);
	}
}
