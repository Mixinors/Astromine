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

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public abstract class BufferBlockEntity extends ExtendedBlockEntity {
	public BufferBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	public static class Primitive extends BufferBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_BUFFER, blockPos, blockState);
			
			itemStorage = new SimpleItemStorage(6 * 9);
		}
	}

	public static class Basic extends BufferBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_BUFFER, blockPos, blockState);
			
			itemStorage = new SimpleItemStorage(12 * 9);
		}
	}

	public static class Advanced extends BufferBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_BUFFER, blockPos, blockState);
			
			itemStorage = new SimpleItemStorage(18 * 9);
		}
	}

	public static class Elite extends BufferBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_BUFFER, blockPos, blockState);
			
			itemStorage = new SimpleItemStorage(24 * 9);
		}
	}

	public static class Creative extends BufferBlockEntity {
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_BUFFER, blockPos, blockState);
			
			itemStorage = new SimpleItemStorage(6 * 9);
		}

		@Override
		public void tick() {
			if (itemStorage != null) {
				for (var i = 0; i < itemStorage.getSize(); ++i) {
					var stack = itemStorage.getStack(i);
					
					stack.setCount(stack.getMaxCount());
				}
			}
		}
	}
}
