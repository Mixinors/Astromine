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

package com.github.chainmailstudios.astromine.common.component.block.entity;

import net.minecraft.nbt.CompoundTag;

import com.github.chainmailstudios.astromine.common.block.redstone.RedstoneType;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

public class BlockEntityRedstoneComponent implements Component {
	private RedstoneType type = RedstoneType.WORK_WHEN_OFF;

	@Nullable
	public static <V> BlockEntityRedstoneComponent get(V v) {
		try {
			return AstromineComponents.BLOCK_ENTITY_REDSTONE_COMPONENT.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}

	public RedstoneType getType() {
		return type;
	}

	public void setType(RedstoneType type) {
		this.type = type;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		type = RedstoneType.byNumber(dataTag.getInt("number"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		dataTag.putInt("number", type.asNumber());

		tag.put("data", dataTag);
	}
}
