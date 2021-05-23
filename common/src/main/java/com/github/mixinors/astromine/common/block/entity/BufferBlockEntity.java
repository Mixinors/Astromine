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
import net.minecraft.block.entity.BlockEntityType;

import com.github.mixinors.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.mixinors.astromine.common.component.base.ItemComponent;

import java.util.function.Supplier;

public abstract class BufferBlockEntity extends ComponentItemBlockEntity {
	public BufferBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	public static class Primitive extends BufferBlockEntity {
		public Primitive() {
			super(AMBlockEntityTypes.PRIMITIVE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return ItemComponent.of(this, 6 * 9);
		}
	}

	public static class Basic extends BufferBlockEntity {
		public Basic() {
			super(AMBlockEntityTypes.BASIC_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return ItemComponent.of(this, 12 * 9);
		}
	}

	public static class Advanced extends BufferBlockEntity {
		public Advanced() {
			super(AMBlockEntityTypes.ADVANCED_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return ItemComponent.of(this, 18 * 9);
		}
	}

	public static class Elite extends BufferBlockEntity {
		public Elite() {
			super(AMBlockEntityTypes.ELITE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return ItemComponent.of(this, 24 * 9);
		}
	}

	public static class Creative extends BufferBlockEntity {
		public Creative() {
			super(AMBlockEntityTypes.CREATIVE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return ItemComponent.of(this, 6 * 9);
		}

		@Override
		public void tick() {
			items.forEach(stack -> stack.setCount(stack.getMaxCount()));
		}
	}
}
