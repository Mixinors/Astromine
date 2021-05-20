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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMSoundEvents;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;

import com.github.mixinors.astromine.common.block.entity.base.AbstractConveyableBlockEntity;

import java.util.function.Supplier;

public class SplitterBlockEntity extends AbstractConveyableBlockEntity {
	public SplitterBlockEntity() {
		super(AMBlockEntityTypes.SPLITTER);
	}

	public SplitterBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public void give(ItemStack stack) {
		var size = stack.getCount();
		var smallHalf = size / 2;
		var largeHalf = size - smallHalf;

		if (isEmpty()) {
			var smallStack = stack.copy();
			var largeStack = stack.copy();

			smallStack.setCount(smallHalf);
			largeStack.setCount(largeHalf);

			if (smallStack.getCount() > 0)
				items.setFirst(smallStack);

			if (largeStack.getCount() > 0)
				items.setSecond(largeStack);
		} else if (!items.getFirst().isEmpty() && items.getSecond().isEmpty()) {
			items.setSecond(stack);
		} else if (!items.getSecond().isEmpty() && items.getFirst().isEmpty()) {
			items.setFirst(stack);
		}

		world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), AMSoundEvents.MACHINE_CLICK.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
}