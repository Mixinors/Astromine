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

import com.github.mixinors.astromine.common.component.general.provider.RedstoneComponentProvider;
import net.minecraft.nbt.CompoundTag;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;
import com.github.mixinors.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} representing a {@link BlockEntity}'s
 * redstone information.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #writeToNbt(CompoundTag)} and {@link #readFromNbt(CompoundTag)}.
 */
public class RedstoneComponent implements Component {
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
	public void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		dataTag.putInt("number", type.asNumber());

		tag.put("data", dataTag);
	}

	/** Deserializes this {@link RedstoneComponent} from a {@link CompoundTag}. */
	@Override
	public void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		type = RedstoneType.byNumber(dataTag.getInt("number"));
	}

	/** Returns the {@link RedstoneComponent} of the given {@link V}. */
	@Nullable
	public static <V> RedstoneComponent get(V v) {
		if (v instanceof RedstoneComponentProvider) {
			return ((RedstoneComponentProvider) v).getRedstoneComponent();
		}

		try {
			return AstromineComponents.BLOCK_ENTITY_REDSTONE_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
}
