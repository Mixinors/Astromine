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

import com.github.mixinors.astromine.common.block.entity.storage.CapacitorBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.common.transfer.storage.EnergyStorageItem;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;

public class CapacitorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final CapacitorBlockEntity capacitor;
	
	public CapacitorScreenHandler(int syncId, Player player, BlockPos position) {
		super(AMScreenHandlers.CAPACITOR, syncId, player, position);
		
		capacitor = (CapacitorBlockEntity) blockEntity;
		
		var creative = capacitor instanceof CapacitorBlockEntity.Creative;
		var energyX = creative ? (int) (TABS_WIDTH / 2.0F - (BAR_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + SLOT_WIDTH) / 2.0F) : (int) (TABS_WIDTH / 2.0F - BAR_WIDTH / 2.0F);
		var arrowY = ENERGY_BAR_Y + (int) (BAR_HEIGHT / 2.0F - ARROW_HEIGHT / 2.0F);
		var leftArrowX = energyX - (int) (PAD_7 + ARROW_WIDTH);
		var rightArrowX = energyX + (int) (BAR_WIDTH + PAD_7);
		var slotY = arrowY + (int) ((ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F);
		
		setEnergyBarPosition(energyX, ENERGY_BAR_Y);
		
		if (!creative) {
			addBlockEntitySlot(CapacitorBlockEntity.INPUT_SLOT, leftArrowX - (int) (PAD_7 + SLOT_WIDTH), slotY, CapacitorScreenHandler::hasEnergyStorage);
			addStaticArrow(leftArrowX, arrowY);
		}
		
		addStaticArrow(rightArrowX, arrowY);
		addBlockEntityWildSlot(CapacitorBlockEntity.OUTPUT_SLOT, rightArrowX + (int) (PAD_7 + ARROW_WIDTH), slotY, CapacitorScreenHandler::hasEnergyStorage);
	}
	
	private static boolean hasEnergyStorage(ItemStack stack) {
		return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null || stack.getItem() instanceof EnergyStorageItem;
	}
}
