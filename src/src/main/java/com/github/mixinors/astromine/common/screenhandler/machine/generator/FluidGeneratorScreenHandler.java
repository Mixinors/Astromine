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
		
		fluidBar.setPosition(new Position(tab, PAD_7, PAD_11));
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position(fluidBar, BAR_WIDTH + PAD_7, BAR_HEIGHT / 2.0F - PAD_8));
		arrow.setSize(new Size(ARROW_WIDTH, ARROW_HEIGHT));
		arrow.setMaximum(() -> (float) generator.limit);
		arrow.setCurrent(() -> (float) generator.progress);
		
		energyBar.setPosition(new Position(fluidBar, BAR_WIDTH + PAD_7 + ARROW_WIDTH + PAD_7, 0.0F));
		
		tab.add(arrow);
	}
}
