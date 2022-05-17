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

import com.github.mixinors.astromine.common.block.entity.storage.CapacitorBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import team.reborn.energy.api.EnergyStorage;

public class CapacitorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final CapacitorBlockEntity capacitor;
	
	public CapacitorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.CAPACITOR, syncId, player, position);
		
		capacitor = (CapacitorBlockEntity) blockEntity;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		if (!(capacitor instanceof CapacitorBlockEntity.Creative)) {
			energyBar.setPosition(new Position(tab, TABS_WIDTH / 2.0F - BAR_WIDTH / 2.0F, PAD_11));
		} else {
			energyBar.setPosition(new Position(tab, TABS_WIDTH / 2.0F - (BAR_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + SLOT_WIDTH) / 2.0F, PAD_11));
		}
		
		var leftArrow = new ArrowWidget();
		leftArrow.setHorizontal(true);
		leftArrow.setPosition(new Position(energyBar, -PAD_7 - ARROW_WIDTH, (BAR_HEIGHT / 2.0F) - (ARROW_HEIGHT / 2.0F)));
		leftArrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		
		var input = new SlotWidget(CapacitorBlockEntity.INPUT_SLOT, capacitor.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				var energyStorage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				return energyStorage != null;
			});
			
			return slot;
		});
		input.setPosition(new Position(leftArrow, -PAD_7 - SLOT_WIDTH, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F));
		input.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var rightArrow = new ArrowWidget();
		rightArrow.setHorizontal(true);
		rightArrow.setPosition(new Position(energyBar, BAR_WIDTH + PAD_7, (BAR_HEIGHT / 2.0F) - (ARROW_HEIGHT / 2.0F)));
		rightArrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		
		var output = new SlotWidget(CapacitorBlockEntity.OUTPUT_SLOT, capacitor.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				var energyStorage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
				
				return energyStorage != null;
			});
			
			return slot;
		});
		output.setPosition(new Position(rightArrow, PAD_7 + ARROW_WIDTH, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F));
		output.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		if (!(capacitor instanceof CapacitorBlockEntity.Creative)) {
			tab.add(input);
			tab.add(leftArrow);
		}
		
		tab.add(rightArrow);
		tab.add(output);
	}
}
