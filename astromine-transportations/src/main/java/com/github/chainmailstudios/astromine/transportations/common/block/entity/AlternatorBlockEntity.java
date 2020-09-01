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

package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

import com.github.chainmailstudios.astromine.transportations.common.block.entity.base.AbstractConveyableBlockEntity;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsSoundEvents;

public class AlternatorBlockEntity extends AbstractConveyableBlockEntity {
	public boolean right = false;

	public AlternatorBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.ALTERNATOR);
	}

	public AlternatorBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public void give(ItemStack stack) {
		ItemStack copyStack = stack.copy();

		if (isEmpty()) {
			if (right) {
				setRightStack(copyStack);
			} else {
				setLeftStack(copyStack);
			}
			right = !right;
		} else if (!getLeftStack().isEmpty() && getRightStack().isEmpty()) {
			setRightStack(stack);
		} else if (!getRightStack().isEmpty() && getLeftStack().isEmpty()) {
			setLeftStack(stack);
		}

		getWorld().playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), AstromineTransportationsSoundEvents.MACHINE_CLICK, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}
