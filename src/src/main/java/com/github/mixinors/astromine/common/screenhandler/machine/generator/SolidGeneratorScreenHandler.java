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

import com.github.mixinors.astromine.common.block.entity.machine.generator.SolidGeneratorBlockEntity;
import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
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
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		var arrow = new HorizontalArrowWidget();
		arrow.setPosition(new Position( energyBar, energyBar.getWidth() + 3.0F, energyBar.getHeight() / 2.0F - 8.0F));
		arrow.setSize(new Size(0.0F, 16.0F));
		arrow.setLimitSupplier(() -> generator.limit);
		arrow.setProgressSupplier(() -> (int) generator.progress);
		
		var input = new SlotWidget(0, blockEntity.getItemStorage());
		input.setPosition(new Position(arrow, -26.0F, 0.0F));
		input.setSize(new Size(18.0F, 18.0F));

		energyBar.setPosition(new Position(mainTab, 68.0F, 11.0F));

		mainTab.add(input);
		mainTab.add(arrow);
	}
}
