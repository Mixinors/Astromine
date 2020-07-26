/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
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
package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineSoundEvents;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

public class AlternatorBlockEntity extends DoubleMachineBlockEntity {
	public boolean right = false;

	public AlternatorBlockEntity() {
		super(AstromineBlockEntityTypes.ALTERNATOR);
	}

	public AlternatorBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public void give(ItemStack stack) {
		ItemStack copyStack = stack.copy();

		if (right) {
			setRightStack(copyStack);
		} else {
			setLeftStack(copyStack);
		}
		right = !right;

		getWorld().playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), AstromineSoundEvents.MACHINE_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
