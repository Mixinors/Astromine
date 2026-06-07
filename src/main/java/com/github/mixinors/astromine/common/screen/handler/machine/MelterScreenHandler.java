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

import static com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityMenuLayout.*;

import com.github.mixinors.astromine.common.block.entity.machine.MelterBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class MelterScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final MelterBlockEntity melter;
	
	public MelterScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.MELTER, syncId, player, position);
		
		melter = (MelterBlockEntity) blockEntity;
		
		var inputX = centeredProcessInputX();
		var inputY = centeredProcessInputY();
		var arrowX = processArrowX(inputX);
		var arrowY = processArrowY(inputY);
		
		addBlockEntitySlot(MelterBlockEntity.ITEM_INPUT_SLOT, inputX, inputY);
		addFluidBar(MelterBlockEntity.FLUID_OUTPUT_SLOT, processOutputX(arrowX), ENERGY_BAR_Y);
		addProgressArrow(arrowX, arrowY);
	}
	
	@Override
	public int getDefaultFluidSlotForBar() {
		return MelterBlockEntity.FLUID_OUTPUT_SLOT;
	}
}
