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
import com.github.mixinors.astromine.common.widget.FluidFilterWidget;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;

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

		fluidBar.setPosition( new Position(width / 2.0F - fluidBar.getWidth() / 2F, fluidBar.getY()));
		
		var unload = new SlotWidget(0, blockEntity.getItemStorage());
		unload.setPosition(new Position(fluidBar, -18 - 3, 0));
		unload.setSize( new Size(18, 18));
		
		var buffer = new SlotWidget(1, blockEntity.getItemStorage());
		buffer.setPosition(new Position(unload, -18 - 3, 18 - 4F));
		buffer.setSize(new Size(18.0F, 18.0F));
		
		var load = new SlotWidget(2, blockEntity.getItemStorage());
		load.setPosition(new Position(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		load.setSize(new Size(18.0F, 18.0F));
		
		var leftArrow = new HorizontalArrowWidget();
		leftArrow.setPosition(new Position(unload, 28, 0));
		leftArrow.setSize(new Size(22, 16));
		
		var rightArrow = new HorizontalArrowWidget();
		rightArrow.setPosition(new Position(load, -34, 0));
		rightArrow.setSize(new Size(22, 16));
		
		var filter = new FluidFilterWidget();
		filter.setPosition(new Position(unload, 5F, 18F + 2F));
		filter.setSize(new Size(8, 8));
		filter.setFluidConsumer(tank::setFilter);
		filter.setFluidSupplier(() -> tank.getFilter());
		
		mainTab.add(unload);
		mainTab.add(buffer);
		mainTab.add(load);

		mainTab.add(filter);
	}
}
