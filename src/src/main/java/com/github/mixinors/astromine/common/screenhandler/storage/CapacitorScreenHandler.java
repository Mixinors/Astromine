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

import com.github.mixinors.astromine.common.block.entity.storage.CapacitorBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class CapacitorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final CapacitorBlockEntity capacitor;
	
	public CapacitorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.CAPACITOR, syncId, player, position);
		
		capacitor = (CapacitorBlockEntity) blockEntity;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		energyBar.setPosition(new Position(width / 2.0F - BAR_WIDTH / 2.0F, energyBar.getY()));
		
		var leftArrow = new HorizontalArrowWidget();
		leftArrow.setPosition(new Position(energyBar, -PAD_7 - ARROW_WIDTH, (BAR_HEIGHT / 2.0F) - (ARROW_HEIGHT / 2.0F)));
		leftArrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		
		var input = new SlotWidget(CapacitorBlockEntity.INPUT_SLOT, capacitor.getItemStorage());
		input.setPosition(new Position(leftArrow, -PAD_7 - SLOT_WIDTH, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F));
		input.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var rightArrow = new HorizontalArrowWidget();
		rightArrow.setPosition(new Position(energyBar, BAR_WIDTH + PAD_7, (BAR_HEIGHT / 2.0F) - (ARROW_HEIGHT / 2.0F)));
		rightArrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		
		var output = new SlotWidget(CapacitorBlockEntity.OUTPUT_SLOT, capacitor.getItemStorage());
		output.setPosition(new Position(rightArrow, PAD_7 + ARROW_WIDTH, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F));
		output.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(input);
		tab.add(output);
		tab.add(leftArrow);
		tab.add(rightArrow);
	}
}
