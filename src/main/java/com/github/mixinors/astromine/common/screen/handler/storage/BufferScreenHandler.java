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

package com.github.mixinors.astromine.common.screen.handler.storage;

import static com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityMenuLayout.*;

import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class BufferScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final BufferBlockEntity buffer;
	
	public BufferScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.BUFFER, syncId, player, position);
		
		buffer = (BufferBlockEntity) blockEntity;
		
		if (buffer instanceof BufferBlockEntity.Creative) {
			addBlockEntityWildSlot(0, (int) (TABS_WIDTH / 2.0F - SLOT_WIDTH / 2.0F), (int) (PAD_25 + PAD_7 + SLOT_WIDTH), ($) -> true);
		} else if (buffer != null && buffer.hasItemStorage()) {
			var visibleSlots = Math.min(buffer.getItemStorage().getSlots(), 9 * 6);
			
			for (var slot = 0; slot < visibleSlots; ++slot) {
				addBlockEntitySlot(slot, (int) (PAD_7 + slot % 9 * SLOT_WIDTH), (int) (PAD_25 + PAD_7 + PAD_10 + slot / 9 * SLOT_HEIGHT));
			}
		}
	}
	
}
