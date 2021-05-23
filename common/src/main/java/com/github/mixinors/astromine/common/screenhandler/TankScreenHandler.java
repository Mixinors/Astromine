/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.screenhandler;

import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.common.item.base.FluidVolumeItem;
import com.github.mixinors.astromine.common.screenhandler.base.block.ComponentBlockEntityFluidItemScreenHandler;
import com.github.mixinors.astromine.common.widget.blade.FluidFilterWidget;
import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.common.block.entity.TankBlockEntity;
import com.github.vini2003.blade.common.miscellaneous.Position;
import com.github.vini2003.blade.common.miscellaneous.Size;
import com.github.vini2003.blade.common.widget.base.SlotWidget;

import java.util.function.Supplier;

public class TankScreenHandler extends ComponentBlockEntityFluidItemScreenHandler {
	private final TankBlockEntity tank;

	public TankScreenHandler(int syncId, PlayerEntity player, BlockPos pos) {
		super(AMScreenHandlers.TANK, syncId, player, pos);
		tank = (TankBlockEntity) fluidItemBlockEntity;
	}

	public TankScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos pos) {
		super(type, syncId, player, pos);
		tank = (TankBlockEntity) fluidItemBlockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		fluidBar.setPosition(Position.of(width/ 2.0F - fluidBar.getWidth()/ 2.0F, fluidBar.getY()));

		var input = new SlotWidget(0, fluidItemBlockEntity);
		input.setPosition(Position.of(fluidBar, -18 - 3, 0));
		input.setSize(Size.of(18, 18));

		var output = new SlotWidget(1, fluidItemBlockEntity);
		output.setPosition(Position.of(fluidBar, -18 - 3, fluidBar.getHeight() - 18));
		output.setSize(Size.of(18, 18));

		var leftArrow = new HorizontalArrowWidget();
		leftArrow.setPosition(Position.of(input, 28, 0));
		leftArrow.setSize(Size.of(22, 16));
		leftArrow.setLimitSupplier(() -> {
			var stack = ItemComponent.from(tank).getFirst();
			
			var fluidComponent = FluidComponent.from(stack);
			
			return fluidComponent == null ? 1 : fluidComponent.getFirst().getSize().intValue();
 		});
		leftArrow.setProgressSupplier(() -> {
			var stack = ItemComponent.from(tank).getFirst();
			
			var fluidComponent = FluidComponent.from(stack);
			
			return fluidComponent == null ? 0 : fluidComponent.getFirst().getAmount().intValue();
		});

		var rightArrow = new HorizontalArrowWidget();
		rightArrow.setPosition(Position.of(output, -34, 0));
		rightArrow.setSize(Size.of(22, 16));
		rightArrow.setLimitSupplier(() -> {
			var stack = ItemComponent.from(tank).getSecond();
			
			var fluidComponent = FluidComponent.from(stack);
			
			return fluidComponent == null ? 1 : fluidComponent.getFirst().getSize().intValue();
		});
		rightArrow.setProgressSupplier(() -> {
			var stack = ItemComponent.from(tank).getSecond();
			
			var fluidComponent = FluidComponent.from(stack);
			
			return fluidComponent == null ? 0 : fluidComponent.getFirst().getAmount().intValue();
		});

		var filter = new FluidFilterWidget();
		filter.setPosition(Position.of(input, 5F, 18F + 2F));
		filter.setSize(Size.of(8, 8));
		filter.setFluidConsumer(tank::setFilter);
		filter.setFluidSupplier(tank::getFilter);

		mainTab.addWidget(input);
		mainTab.addWidget(output);

		mainTab.addWidget(filter);
	}
}
