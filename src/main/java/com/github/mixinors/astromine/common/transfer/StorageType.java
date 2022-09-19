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

package com.github.mixinors.astromine.common.transfer;

import com.github.mixinors.astromine.common.util.extra.Codecs;
import com.github.mixinors.astromine.registry.common.AMItems;
import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

/**
 * A {@link StorageType} which dictates which of a machine's storages should be used.
 */
public enum StorageType {
	ITEM,
	FLUID,
	ENERGY;
	
	public static final Codec<StorageType> CODEC = Codecs.createEnumCodec(StorageType.class);
	
	public Item getItem() {
		return switch (this) {
			case ITEM -> AMItems.ITEM.get();
			case FLUID -> AMItems.FLUID.get();
			case ENERGY -> AMItems.ENERGY.get();
		};
	}
	
	public Text getName() {
		return switch (this) {
			case ITEM -> Text.translatable("text.astromine.item");
			case FLUID -> Text.translatable("text.astromine.fluid");
			case ENERGY -> Text.translatable("text.astromine.energy");
		};
	}
}
