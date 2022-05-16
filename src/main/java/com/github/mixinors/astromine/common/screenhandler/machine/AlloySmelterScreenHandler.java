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

package com.github.mixinors.astromine.common.screenhandler.machine;

import com.github.mixinors.astromine.common.block.entity.machine.AlloySmelterBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.slot.FilterSlot;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class AlloySmelterScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final AlloySmelterBlockEntity smelter;
	
	public AlloySmelterScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.ALLOY_SMELTER, syncId, player, position);
		
		smelter = (AlloySmelterBlockEntity) blockEntity;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var firstInput = new SlotWidget(AlloySmelterBlockEntity.INPUT_SLOT_1, smelter.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				return smelter.getItemStorage().canInsert(ItemVariant.of(stack), AlloySmelterBlockEntity.INPUT_SLOT_1);
			});
			
			return slot;
		});
		firstInput.setPosition(new Position(energyBar, (TABS_WIDTH / 2.0F - (SLOT_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + SLOT_WIDTH) / 2.0F - SLOT_WIDTH / 2.0F), BAR_HEIGHT / 2.0F - SLOT_HEIGHT / 2.0F - (SLOT_HEIGHT / 2.0F) - PAD_3));
		firstInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var secondInput = new SlotWidget(AlloySmelterBlockEntity.INPUT_SLOT_2, smelter.getItemStorage(), (inventory, id, x, y) -> {
			var slot = new FilterSlot(inventory, id, x, y);
			
			slot.setInsertPredicate((stack) -> {
				return smelter.getItemStorage().canInsert(ItemVariant.of(stack), AlloySmelterBlockEntity.INPUT_SLOT_2);
			});
			
			return slot;
		});
		secondInput.setPosition(new Position(energyBar, (TABS_WIDTH / 2.0F - (SLOT_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7 + SLOT_WIDTH) / 2.0F - SLOT_WIDTH / 2.0F), BAR_HEIGHT / 2.0F - SLOT_HEIGHT / 2.0F + (SLOT_HEIGHT / 2.0F) + PAD_3));
		secondInput.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var arrow = new ArrowWidget();
		arrow.setHorizontal(true);
		arrow.setPosition(new Position(secondInput, SLOT_WIDTH + PAD_7, -(SLOT_HEIGHT / 2.0F + PAD_3) + 0.5F)); // 0.5F centers the arrow against the input slots.
		arrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		arrow.setMaximum(() -> (float) smelter.limit);
		arrow.setCurrent(() -> (float) smelter.progress);
		
		var output = new SlotWidget(AlloySmelterBlockEntity.OUTPUT_SLOT, smelter.getItemStorage(), ExtractionSlot::new);
		output.setPosition(new Position(arrow, ARROW_WIDTH + PAD_7, (ARROW_HEIGHT - SLOT_HEIGHT) / 2.0F + 1.0F)); // 1.0F centers the slot against the arrow.
		output.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		tab.add(firstInput);
		tab.add(secondInput);
		tab.add(output);
		tab.add(arrow);
	}
}
