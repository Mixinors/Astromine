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

package com.github.chainmailstudios.astromine.technologies.common.block.entity;

import net.minecraft.block.entity.BlockEntityType;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import net.minecraft.item.ItemStack;

public abstract class BufferBlockEntity extends ComponentItemBlockEntity {
	public BufferBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	public static class Primitive extends BufferBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return SimpleItemComponent.of(6 * 9);
		}
	}

	public static class Basic extends BufferBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return SimpleItemComponent.of(12 * 9);
		}
	}

	public static class Advanced extends BufferBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return SimpleItemComponent.of(18 * 9);
		}
	}

	public static class Elite extends BufferBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return SimpleItemComponent.of(24 * 9);
		}
	}

	public static class Creative extends BufferBlockEntity {
		public Creative() {
			super(AstromineTechnologiesBlockEntityTypes.CREATIVE_BUFFER);
		}

		@Override
		public ItemComponent createItemComponent() {
			return SimpleItemComponent.of(6 * 9);
		}

		@Override
		public void tick() {
			ItemComponent itemComponent = getItemComponent();

			if (itemComponent != null) {
				itemComponent.forEach(entry -> {
					ItemStack stack = entry.getValue();

					stack.setCount(stack.getMaxCount());
				});
			}
		}
	}
}
