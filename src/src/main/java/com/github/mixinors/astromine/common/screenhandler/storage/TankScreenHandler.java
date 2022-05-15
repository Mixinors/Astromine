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

import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.slot.ExtractionSlot;
import com.github.mixinors.astromine.common.widget.FluidFilterWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import kotlin.Unit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TankScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final TankBlockEntity tank;
	
	public TankScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.TANK, syncId, player, position);
		
		tank = (TankBlockEntity) blockEntity;
	}
	
	@Override
	public int getDefaultFluidSlotForBar() {
		return TankBlockEntity.FLUID_INPUT_SLOT;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		fluidBar.setPosition(new Position(width / 2.0F - BAR_WIDTH / 2.0F, fluidBar.getY()));
		
		var unload = new SlotWidget(TankBlockEntity.ITEM_INPUT_SLOT, tank.getItemStorage());
		unload.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		if (!(tank instanceof TankBlockEntity.Creative)) {
			unload.setPosition(new Position(fluidBar, -SLOT_WIDTH - PAD_3, 0.0F));
		} else {
			unload.setPosition(new Position(fluidBar, -SLOT_WIDTH - PAD_3, -(((BAR_HEIGHT - (SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT)) / 2.0F))));
		}
		
		var buffer = new SlotWidget(TankBlockEntity.ITEM_BUFFER_STORAGE, tank.getItemStorage(), ExtractionSlot::new);
		buffer.setPosition(new Position(unload, -SLOT_WIDTH - PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
		buffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var load = new SlotWidget(TankBlockEntity.ITEM_OUTPUT_SLOT_2, tank.getItemStorage());
		load.setPosition(new Position(unload, 0.0F, SLOT_HEIGHT + PAD_3 + FILTER_HEIGHT + PAD_3));
		load.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var filter = new FluidFilterWidget();
		filter.setPosition(new Position(unload, (SLOT_WIDTH / 2.0F - FILTER_WIDTH / 2.0F), SLOT_WIDTH + 2.0F)); // 2.0F centers the filter against the upper and lower slots.
		filter.setSize(new Size(FILTER_WIDTH, FILTER_HEIGHT));
		filter.setFluidVariantConsumer((variant) -> {
			if (!(tank instanceof TankBlockEntity.Creative)) {
				tank.setFilter(variant);
			} else {
				var storage = tank.getFluidStorage().getStorage(TankBlockEntity.FLUID_INPUT_SLOT);
				
				storage.setAmount(Long.MAX_VALUE);
				storage.setVariant(variant);
				
				tank.setFilter(variant);
			}
			
			return Unit.INSTANCE;
		});
		filter.setFluidVariantSupplier(tank::getFilter);
		
		if (!(tank instanceof TankBlockEntity.Creative)) {
			tab.add(unload);
			tab.add(buffer);
		}
		
		tab.add(load);
		
		tab.add(filter);
	}
}
