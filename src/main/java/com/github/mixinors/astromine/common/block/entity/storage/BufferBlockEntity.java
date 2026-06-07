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

package com.github.mixinors.astromine.common.block.entity.storage;

import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemStorage;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BufferBlockEntity extends ExtendedBlockEntity {
	public BufferBlockEntity(Supplier<? extends BlockEntityType<?>> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}
	
	private static SimpleItemStorage createStorage(int slots) {
		return new SimpleItemStorage(slots)
				.insertPredicate(BufferBlockEntity::allowsTransfer)
				.extractPredicate(BufferBlockEntity::allowsTransfer);
	}
	
	private static boolean allowsTransfer(ItemStack stack, int slot) {
		return true;
	}
	
	public static class Primitive extends BufferBlockEntity {
		public Primitive(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.PRIMITIVE_BUFFER, blockPos, blockState);
			
			itemStorage = createStorage(6 * 9).listener(() -> {
				setChanged();
			});
		}
	}
	
	public static class Basic extends BufferBlockEntity {
		public Basic(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.BASIC_BUFFER, blockPos, blockState);
			
			itemStorage = createStorage(12 * 9).listener(() -> {
				setChanged();
			});
		}
	}
	
	public static class Advanced extends BufferBlockEntity {
		public Advanced(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ADVANCED_BUFFER, blockPos, blockState);
			
			itemStorage = createStorage(18 * 9).listener(() -> {
				setChanged();
			});
		}
	}
	
	public static class Elite extends BufferBlockEntity {
		public Elite(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.ELITE_BUFFER, blockPos, blockState);
			
			itemStorage = createStorage(24 * 9).listener(() -> {
				setChanged();
			});
		}
	}
	
	public static class Creative extends BufferBlockEntity {
		public Creative(BlockPos blockPos, BlockState blockState) {
			super(AMBlockEntityTypes.CREATIVE_BUFFER, blockPos, blockState);
			
			itemStorage = createStorage(6 * 9).listener(() -> {
				setChanged();
			});
		}
		
		@Override
		public void tick() {
			if (itemStorage != null) {
				for (var i = 0; i < itemStorage.getSize(); ++i) {
					var stack = itemStorage.getItem(i);
					
					stack.setCount(stack.getMaxStackSize());
				}
			}
		}
	}
}
