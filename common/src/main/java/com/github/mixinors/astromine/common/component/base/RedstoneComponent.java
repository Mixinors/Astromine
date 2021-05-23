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

package com.github.mixinors.astromine.common.component.base;

import com.github.mixinors.astromine.common.component.Component;
import com.github.mixinors.astromine.common.component.general.provider.RedstoneComponentProvider;
import com.github.mixinors.astromine.registry.common.AMComponents;
import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.block.entity.BlockEntity;

import com.github.mixinors.astromine.common.block.redstone.RedstoneType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Component} representing a {@link BlockEntity}'s
 * redstone information.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag(CompoundTag)} and {@link #fromTag(CompoundTag)}.
 */
public interface RedstoneComponent extends Component {
	/** Instantiates a new {@link RedstoneComponent}. */
	static RedstoneComponent of() {
		return new RedstoneComponentImpl();
	}

	/** Returns this component's {@link RedstoneType}. */
	RedstoneType getType();

	/** Sets this component's {@link RedstoneType} to the specified value. */
	void setType(RedstoneType type);

	/** Serializes this {@link RedstoneComponent} to a {@link CompoundTag}. */
	@Override
	default void toTag(CompoundTag tag) {
		var dataTag = new CompoundTag();
		
		dataTag.putString("Type", getType().toString());
		
		tag.put("Data", dataTag);
	}

	/** Deserializes this {@link RedstoneComponent} from a {@link CompoundTag}. */
	@Override
	default void fromTag(CompoundTag tag) {
		var dataTag = tag.getCompound("Data");

		setType(RedstoneType.fromString(dataTag.getString("Type")));
	}

	/** Returns the {@link RedstoneComponent} of the given {@link V}. */
	@Nullable
	static <V> RedstoneComponent from(V v) {
		if (v instanceof RedstoneComponentProvider) {
			return ((RedstoneComponentProvider) v).getRedstoneComponent();
		}
		
		return fromPost(v);
	}
	
	@ExpectPlatform
	static <V> RedstoneComponent fromPost(V v) {
		throw new AssertionError();
	}
	
	/** Returns this component's {@link Identifier}. */
	@Override
	default Identifier getId() {
		return AMComponents.REDSTONE;
	}
}
