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

package com.github.mixinors.astromine.common.screen.handler.machine.generator;

import com.github.mixinors.astromine.common.block.entity.machine.generator.SolidGeneratorBlockEntity;
import com.github.mixinors.astromine.common.screen.handler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SolidGeneratorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final SolidGeneratorBlockEntity generator;
	
	public SolidGeneratorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.SOLID_GENERATOR, syncId, player, position);
		
		generator = (SolidGeneratorBlockEntity) blockEntity;
	}
	
	@Override
	public void init(int width, int height) {
		super.init(width, height);
		
		var input = new SlotWidget(SolidGeneratorBlockEntity.INPUT_SLOT, generator.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				return FuelRegistry.get(stack) > 0;
			});
			
			return slot;
		});
		input.setPosition(new Position(tab, TABS_WIDTH / 2.0F - (SLOT_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + BAR_WIDTH) / 2.0F, PAD_11 + (BAR_HEIGHT / 2.0F) - (SLOT_HEIGHT / 2.0F)));
		input.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var arrow = new ArrowWidget();
		arrow.setHorizontal(true);
		arrow.setPosition(new Position(input, SLOT_WIDTH + PAD_7, (SLOT_HEIGHT - ARROW_HEIGHT) / 2.0F));
		arrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		arrow.setMaximum(() -> (float) generator.limit);
		arrow.setCurrent(() -> (float) generator.progress);
		
		energyBar.setPosition(new Position(arrow, ARROW_WIDTH + PAD_7, -(BAR_HEIGHT / 2.0F - ARROW_HEIGHT / 2.0F)));
		
		tab.add(input);
		tab.add(arrow);
	}
}
