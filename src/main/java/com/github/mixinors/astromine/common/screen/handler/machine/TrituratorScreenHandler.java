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

package com.github.mixinors.astromine.common.screen.handler.machine;

import com.github.mixinors.astromine.common.block.entity.machine.TrituratorBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class TrituratorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final TrituratorBlockEntity triturator;
	
	public TrituratorScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.TRITURATOR, syncId, player, position);
		
		triturator = (TrituratorBlockEntity) blockEntity;
		
		var inputX = centeredProcessInputX();
		var inputY = centeredProcessInputY();
		var arrowX = processArrowX(inputX);
		var arrowY = processArrowY(inputY);
		
		addBlockEntitySlot(TrituratorBlockEntity.INPUT_SLOT, inputX, inputY);
		addBlockEntityOutputSlot(TrituratorBlockEntity.OUTPUT_SLOT, processOutputX(arrowX), processOutputY(arrowY));
		addProgressArrow(arrowX, arrowY);
	}
}
