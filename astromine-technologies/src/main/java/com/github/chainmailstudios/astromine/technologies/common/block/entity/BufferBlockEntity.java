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

import com.github.chainmailstudios.astromine.common.volume.handler.ItemHandler;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentInventoryBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemInventoryComponent;
import com.github.chainmailstudios.astromine.common.utilities.tier.BufferTier;
import com.github.chainmailstudios.astromine.technologies.registry.AstromineTechnologiesBlockEntityTypes;
import org.jetbrains.annotations.NotNull;

public abstract class BufferBlockEntity extends ComponentInventoryBlockEntity {
	public BufferBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	public static class Primitive extends BufferBlockEntity {
		public Primitive() {
			super(AstromineTechnologiesBlockEntityTypes.PRIMITIVE_BUFFER);
		}

		@Override
		protected ItemInventoryComponent createItemComponent() {
			return new SimpleItemInventoryComponent(6 * 9);
		}
	}

	public static class Basic extends BufferBlockEntity {
		public Basic() {
			super(AstromineTechnologiesBlockEntityTypes.BASIC_BUFFER);
		}

		@Override
		protected ItemInventoryComponent createItemComponent() {
			return new SimpleItemInventoryComponent(12 * 9);
		}
	}

	public static class Advanced extends BufferBlockEntity {
		public Advanced() {
			super(AstromineTechnologiesBlockEntityTypes.ADVANCED_BUFFER);
		}

		@Override
		protected ItemInventoryComponent createItemComponent() {
			return new SimpleItemInventoryComponent(18 * 9);
		}
	}

	public static class Elite extends BufferBlockEntity {
		public Elite() {
			super(AstromineTechnologiesBlockEntityTypes.ELITE_BUFFER);
		}

		@Override
		protected ItemInventoryComponent createItemComponent() {
			return new SimpleItemInventoryComponent(24 * 9);
		}
	}

	public static class Creative extends BufferBlockEntity {
		public Creative() {
			super(AstromineTechnologiesBlockEntityTypes.CREATIVE_BUFFER);
		}

		@Override
		protected ItemInventoryComponent createItemComponent() {
			return new SimpleItemInventoryComponent(6 * 9);
		}

		@Override
		public void tick() {
			ItemHandler.ofOptional(this).ifPresent(items -> {
				items.forEach(stack -> {
					stack.setCount(stack.getMaxCount());
				});
			});
		}
	}
}
