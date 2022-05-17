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

import com.github.mixinors.astromine.common.block.entity.machine.SolidifierBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SolidifierScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final SolidifierBlockEntity solidifier;
	
	public SolidifierScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.SOLIDIFIER, syncId, player, position);
		
		solidifier = (SolidifierBlockEntity) blockEntity;
	}
	
	@Override
	public int getDefaultFluidSlotForBar() {
		return SolidifierBlockEntity.FLUID_INPUT_SLOT;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var arrow = new ArrowWidget();
		arrow.setHorizontal(true);
		arrow.setPosition(new Position(fluidBar, BAR_WIDTH + PAD_7, BAR_HEIGHT / 2.0F - PAD_8));
		arrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		arrow.setMaximum(() -> (float) solidifier.limit);
		arrow.setCurrent(() -> (float) solidifier.progress);
		
		var output = new SlotWidget(SolidifierBlockEntity.ITEM_OUTPUT_SLOT, solidifier.getItemStorage(), ExtractionSlot::new);
		output.setPosition(new Position(arrow, ARROW_WIDTH + PAD_7, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F)); // 1.0F centers the slot against the arrow.
		output.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(output);
		tab.add(arrow);
	}
}
