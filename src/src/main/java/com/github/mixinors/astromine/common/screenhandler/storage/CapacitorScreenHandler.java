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

import com.github.mixinors.astromine.common.screenhandler.base.block.entity.ExtendedBlockEntityScreenHandler;
import com.github.mixinors.astromine.common.widget.HorizontalArrowWidget;
import com.github.mixinors.astromine.registry.common.AMScreenHandlers;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class CapacitorScreenHandler extends ExtendedBlockEntityScreenHandler {
	public CapacitorScreenHandler(int syncId, PlayerEntity player, BlockPos position) {
		super(AMScreenHandlers.CAPACITOR, syncId, player, position);
	}
	
	public CapacitorScreenHandler(Supplier<? extends ScreenHandlerType<?>> type, int syncId, PlayerEntity player, BlockPos position) {
		super(type, syncId, player, position);
	}
	
	@Override
	public void initialize(int width, int height) {
		super.initialize(width, height);
		
		energyBar.setPosition(new Position(width / 2.0F - energyBar.getWidth() / 2.0F, energyBar.getY()));
		
		var input = new SlotWidget(0, blockEntity.getItemStorage());
		input.setPosition(new Position(mainTab, 12.0F, 26.0F));
		input.setSize(new Size(18.0F, 18.0F));
		
		var output = new SlotWidget(1, blockEntity.getItemStorage());
		output.setPosition(new Position(mainTab, 146.0F, 26.0F));
		output.setSize(new Size(18.0F, 18.0F));
		
		var leftArrow = new HorizontalArrowWidget();
		leftArrow.setPosition(new Position(input, 28.0F, 0.0F));
		leftArrow.setSize(new Size(22.0F, 16.0F));
		
		var rightArrow = new HorizontalArrowWidget();
		rightArrow.setPosition(new Position(output, -34.0F, 0.0F));
		rightArrow.setSize(new Size(22.0F, 16.0F));
		
		mainTab.add(input);
		mainTab.add(output);
		mainTab.add(leftArrow);
		mainTab.add(rightArrow);
	}
}
