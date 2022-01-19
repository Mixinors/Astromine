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

import java.util.function.Supplier;

import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.blade.FluidFilterWidget;
import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.widget.slot.SlotWidget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

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

		fluidBar.setPosition( Position.of(width / 2F - fluidBar.getWidth() / 2F, fluidBar.getY()));
		
		var unload = new SlotWidget(0, blockEntity.getItemStorage());
		unload.setPosition(Position.of(fluidBar, -18 - 3, 0));
		unload.setSize( Size.of(18, 18));
		
		var buffer = new SlotWidget(1, blockEntity.getItemStorage());
		buffer.setPosition(Position.of(unload, -18 - 3, 18 - 3.5F));
		buffer.setSize(Size.of(18, 18));
		
		var load = new SlotWidget(2, blockEntity.getItemStorage());
		load.setPosition(Position.of(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		load.setSize(Size.of(18, 18));
		
		var leftArrow = new HorizontalArrowWidget();
		leftArrow.setPosition(Position.of(unload, 28, 0));
		leftArrow.setSize(Size.of(22, 16));
		
		var rightArrow = new HorizontalArrowWidget();
		rightArrow.setPosition(Position.of(load, -34, 0));
		rightArrow.setSize(Size.of(22, 16));
		
		var filter = new FluidFilterWidget();
		filter.setPosition(Position.of(unload, 5F, 18F + 2F));
		filter.setSize(Size.of(8, 8));
		filter.setFluidConsumer(tank::setFilter);
		filter.setFluidSupplier(() -> tank.getFilter());
		
		mainTab.add(unload);
		mainTab.add(buffer);
		mainTab.add(load);

		mainTab.add(filter);
	}
}
