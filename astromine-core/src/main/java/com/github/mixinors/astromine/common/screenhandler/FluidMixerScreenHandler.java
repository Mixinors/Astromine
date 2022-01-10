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

import com.github.mixinors.astromine.common.block.entity.machine.FluidMixerBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.blade.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.common.geometry.position.Position;
import dev.vini2003.hammer.common.geometry.size.Size;
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class FluidMixerScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final FluidMixerBlockEntity mixer;

	public FluidMixerScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.FLUID_MIXER, syncId, player, position);

		mixer = (FluidMixerBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var secondInputFluidBar = new FluidBarWidget();
		secondInputFluidBar.setPosition( Position.of(fluidBar, fluidBar.getWidth() + 7, 0));
		secondInputFluidBar.setSize( Size.of(fluidBar));
		secondInputFluidBar.setStorage(blockEntity.getFluidStorage().getStorage(1));
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(Position.of(secondInputFluidBar, secondInputFluidBar.getWidth() + 9, secondInputFluidBar.getHeight() / 2F - 8));
		arrow.setSize(Size.of(22, 16));
		arrow.setLimitSupplier(() -> mixer.limit);
		arrow.setProgressSupplier(() -> (int) mixer.progress);
		
		var outputFluidBar = new FluidBarWidget();
		outputFluidBar.setPosition(Position.of(secondInputFluidBar, secondInputFluidBar.getWidth() + 9 + arrow.getWidth() + 7, 0));
		outputFluidBar.setSize(Size.of(fluidBar));
		outputFluidBar.setStorage(blockEntity.getFluidStorage().getStorage(2));

		mainTab.add(secondInputFluidBar);
		mainTab.add(arrow);
		mainTab.add(outputFluidBar);
	}
}
