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

package com.github.mixinors.astromine.common.screenhandler.machine.generator;

import com.github.mixinors.astromine.common.block.entity.machine.generator.FluidGeneratorBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class FluidGeneratorScreenHandler extends ExtendedBlockEntityScreenHandler {
	private final FluidGeneratorBlockEntity generator;

	public FluidGeneratorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.LIQUID_GENERATOR, syncId, player, position);

		generator = (FluidGeneratorBlockEntity) blockEntity;
	}

	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);

		energyBar.setPosition( new Position(mainTab, 68.0F, 11.0F));
		fluidBar.setPosition(new Position(mainTab, 7.0F, 11.0F));
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position(fluidBar, fluidBar.getWidth() + 7.0F, fluidBar.getHeight() / 2.0F - 8.0F));
		arrow.setSize(new Size(22.0F, 16.0F));
		arrow.setLimitSupplier(() -> generator.limit);
		arrow.setProgressSupplier(() -> (int) generator.progress);

		mainTab.add(arrow);
	}
}
