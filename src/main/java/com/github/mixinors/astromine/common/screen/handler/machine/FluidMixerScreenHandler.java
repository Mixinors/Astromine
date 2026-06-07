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

import com.github.mixinors.astromine.common.block.entity.machine.FluidMixerBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class FluidMixerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final FluidMixerBlockEntity mixer;
	
	public FluidMixerScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.FLUID_MIXER, syncId, player, position);
		
		mixer = (FluidMixerBlockEntity) blockEntity;
		
		var firstX = defaultFluidBarX();
		var firstY = defaultFluidBarY();
		var secondX = firstX + (int) (BAR_WIDTH + PAD_7);
		var arrowX = fluidArrowX(secondX);
		var arrowY = fluidArrowY(firstY);
		
		addFluidBar(FluidMixerBlockEntity.INPUT_SLOT_1, firstX, firstY);
		addFluidBar(FluidMixerBlockEntity.INPUT_SLOT_2, secondX, firstY);
		addFluidBar(FluidMixerBlockEntity.OUTPUT_SLOT, processOutputX(arrowX), firstY);
		addProgressArrow(arrowX, arrowY);
	}
	
	@Override
	public int getDefaultFluidSlotForBar() {
		return FluidMixerBlockEntity.INPUT_SLOT_1;
	}
}
