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

import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidUtil;

public class TankScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final TankBlockEntity tank;
	
	public TankScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.TANK, syncId, player, position);
		
		tank = (TankBlockEntity) blockEntity;
		
		var creative = tank instanceof TankBlockEntity.Creative;
		var fluidX = (int) (TABS_WIDTH / 2.0F - BAR_WIDTH / 2.0F);
		var fluidY = defaultFluidBarY();
		var inputX = fluidX - (int) (SLOT_WIDTH + PAD_3);
		var inputY = creative ? fluidY - (int) ((BAR_HEIGHT - (SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT)) / 2.0F) : fluidY;
		var bufferX = inputX - (int) (SLOT_WIDTH + PAD_3);
		var bufferY = inputY + (int) (SLOT_HEIGHT - 4.0F);
		var outputY = inputY + (int) (SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + PAD_3);
		var filterX = inputX + (int) (SLOT_WIDTH / 2.0F - FILTER_WIDTH / 2.0F);
		var filterY = inputY + (int) (SLOT_WIDTH + 2.0F);
		
		addFluidBar(TankBlockEntity.FLUID_INPUT_SLOT, fluidX, fluidY);
		
		if (!creative) {
			addBlockEntitySlot(TankBlockEntity.ITEM_INPUT_SLOT, inputX, inputY, TankScreenHandler::hasFluidHandler);
			addBlockEntityWildOutputSlot(TankBlockEntity.ITEM_BUFFER_SLOT, bufferX, bufferY);
		}
		
		addBlockEntityWildSlot(TankBlockEntity.ITEM_OUTPUT_SLOT, inputX, outputY, TankScreenHandler::hasFluidHandler);
		addFluidFilter(filterX, filterY);
	}
	
	@Override
	public int getDefaultFluidSlotForBar() {
		return TankBlockEntity.FLUID_INPUT_SLOT;
	}
	
	private static boolean hasFluidHandler(ItemStack stack) {
		return FluidUtil.getFluidHandler(stack).isPresent();
	}
}
