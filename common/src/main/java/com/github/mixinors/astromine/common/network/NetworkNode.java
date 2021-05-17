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

package com.github.mixinors.astromine.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import com.google.common.base.Objects;

/**
 * A node for
 * a network, with no attached {@link NetworkMember}.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
 */
public final class NetworkNode {
	private long pos;

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkNode(BlockPos blockPos) {
		setBlockPosition(blockPos);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkNode(long pos) {
		setLongPosition(pos);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkNode of(BlockPos blockPos) {
		return new NetworkNode(blockPos);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkNode of(long pos) {
		return new NetworkNode(pos);
	}

	/** Returns this node's {@link BlockPos} from its long representation. */
	public BlockPos getBlockPosition() {
		return BlockPos.fromLong(this.pos);
	}

	/** Sets this node's {@link BlockPos} to the long representation of the specified value. */
	public void setBlockPosition(BlockPos blockPos) {
		this.pos = blockPos.asLong();
	}

	/** Returns this node's {@link BlockPos} long representation. */
	public long getLongPosition() {
		return pos;
	}

	/** Sets this node's {@link BlockPos} long representation to the specified value. */
	public void setLongPosition(long pos) {
		this.pos = pos;
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof NetworkNode))
			return false;

		NetworkNode that = (NetworkNode) object;
		return this.pos == that.pos;
	}

	/** Returns the hash for this node. */
	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}

	/** Returns this node's string representation.
	 * For example, it may be "[184, 47, -759]" */
	@Override
	public String toString() {
		return String.format("[%s, %s, %s]", getBlockPosition().getX(), getBlockPosition().getY(), getBlockPosition().getY());
	}

	/** Deserializes a {@link NetworkNode} from a {@link CompoundTag}. */
	public static NetworkNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"));
	}

	/** Serializes a {@link NetworkNode} to a {@link CompoundTag}. */
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();

		tag.putLong("pos", pos);

		return tag;
	}
}
