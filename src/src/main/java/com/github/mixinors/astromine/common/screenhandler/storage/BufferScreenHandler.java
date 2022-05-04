/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.screenhandler.storage;

import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.gui.common.widget.list.slot.SlotListWidget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BufferScreenHandler extends ExtendedBlockEntityScreenHandler {
	public BufferScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.BUFFER, syncId, player, position);
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var slotWidth = 9 * 18;
		var slotHeight = 6 * 18;
		
		var leftPadding = 7;
		var topPadding = 10;
		
		var slotList = new SlotListWidget(blockEntity.getItemStorage(), slotWidth, slotHeight, 0);
		slotList.setPosition( Position.of(mainTab.getX() + leftPadding, mainTab.getY() + topPadding));
		slotList.setSize( Size.of(slotWidth, slotHeight));

		mainTab.add(slotList);
	}

	@Override
	public int getTabWidgetExtendedHeight() {
		return 58;
	}
}
