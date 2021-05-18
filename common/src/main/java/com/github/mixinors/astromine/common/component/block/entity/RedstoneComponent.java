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

package com.github.mixinors.astromine.common.component.block.entity;

import com.github.mixinors.astromine.common.component.ProtoComponent;
import com.github.mixinors.astromine.common.component.general.provider.RedstoneComponentProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.block.entity.BlockEntity;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link ProtoComponent} representing a {@link BlockEntity}'s
 * redstone information.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public class RedstoneComponent implements ProtoComponent {
	private RedstoneType type = RedstoneType.WORK_WHEN_OFF;

	/** Returns this component's {@link RedstoneType}. */
	public RedstoneType getType() {
		return type;
	}

	/** Sets this component's {@link RedstoneType} to the specified value. */
	public void setType(RedstoneType type) {
		this.type = type;
	}

	/** Serializes this {@link RedstoneComponent} to a {@link CompoundTag}. */
	@Override
	public void toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();
		
		dataTag.putString("Type", type.toString());
		
		tag.put("Data", dataTag);
	}

	/** Deserializes this {@link RedstoneComponent} from a {@link CompoundTag}. */
	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("Data");

		type = RedstoneType.fromString(dataTag.getString("Type"));
	}

	/** Returns the {@link RedstoneComponent} of the given {@link V}. */
	@Nullable
	public static <V> RedstoneComponent get(V v) {
		if (v instanceof RedstoneComponentProvider) {
			return ((RedstoneComponentProvider) v).getRedstoneComponent();
		}
		
		return null;
	}
}
