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
import com.github.mixinors.astromine.common.widget.FluidFilterWidget;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import kotlin.Unit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class TankScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final TankBlockEntity tank;
	
	public TankScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.TANK, syncId, player, position);
		tank = (TankBlockEntity) blockEntity;
	}
	
	public TankScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);
		tank = (TankBlockEntity) blockEntity;
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		fluidBar.setPosition(new Position(width / 2.0F - BAR_WIDTH / 2.0F, fluidBar.getY()));
		
		var unload = new SlotWidget(TankBlockEntity.ITEM_INPUT_SLOT, tank.getItemStorage());
		unload.setPosition(new Position(fluidBar, -SLOT_WIDTH - PAD_3, 0));
		unload.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var buffer = new SlotWidget(TankBlockEntity.ITEM_OUTPUT_SLOT_1, tank.getItemStorage());
		buffer.setPosition(new Position(unload, -SLOT_WIDTH - PAD_3, SLOT_HEIGHT - 4.0F)); // 4.0F centers the buffer slot against the two other slots.
		buffer.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var load = new SlotWidget(TankBlockEntity.ITEM_OUTPUT_SLOT_2, tank.getItemStorage());
		load.setPosition(new Position(fluidBar, -SLOT_WIDTH - PAD_3, BAR_HEIGHT - SLOT_HEIGHT));
		load.setSize(new Size(SLOT_WIDTH, SLOT_HEIGHT));
		
		var filter = new FluidFilterWidget();
		filter.setPosition(new Position(unload, PAD_3, SLOT_WIDTH + 2.0F)); // 2.0F centers the filter against the upper and lower slots.
		filter.setSize(new Size(FILTER_WIDTH, FILTER_HEIGHT));
		filter.setFluidVariantConsumer((variant) -> {
			tank.setFilter(variant);
			
			return Unit.INSTANCE;
		});
		filter.setFluidVariantSupplier(tank::getFilter);
		
		tab.add(unload);
		tab.add(buffer);
		tab.add(load);
		
		tab.add(filter);
	}
}
