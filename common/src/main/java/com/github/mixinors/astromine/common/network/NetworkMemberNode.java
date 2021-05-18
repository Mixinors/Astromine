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
import net.minecraft.util.math.Direction;

import com.google.common.base.Objects;

/**
 * A node for a {@link NetworkMember} of a network.
 *
 * Serialization and deserialization methods are provided for:
 * - {@link CompoundTag} - through {@link #toTag()} and {@link #fromTag(CompoundTag)}.
 */
public final class NetworkMemberNode {
	private long pos;

	private int directionId;

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkMemberNode(BlockPos blockPos, Direction direction) {
		setBlockPosition(blockPos);
		setDirection(direction);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkMemberNode(long pos, int directionId) {
		setLongPosition(pos);
		setDirectionId(directionId);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkMemberNode(BlockPos blockPos) {
		setBlockPosition(blockPos);
		setDirectionId(-1);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	private NetworkMemberNode(long pos) {
		setLongPosition(pos);
		setDirectionId(-1);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkMemberNode of(BlockPos blockPos, Direction direction) {
		return new NetworkMemberNode(blockPos, direction);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkMemberNode of(long pos, int dir) {
		return new NetworkMemberNode(pos, dir);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkMemberNode of(BlockPos blockPos) {
		return new NetworkMemberNode(blockPos);
	}

	/** Instantiates a {@link NetworkMemberNode}. */
	public static NetworkMemberNode of(long pos) {
		return new NetworkMemberNode(pos);
	}

	/** Returns this node's {@link BlockPos} from its long representation. */
	public BlockPos getBlockPosition() {
		return BlockPos.fromLong(this.pos);
	}

	/** Sets this node's {@link BlockPos} to the long representation of the specified value. */
	public void setBlockPosition(BlockPos blockPos) {
		this.pos = blockPos.asLong();
	}

	/** Returns this node's {@link Direction} from its ID. */
	public Direction getDirection() {
		return Direction.byId(directionId);
	}

	/** Sets this node's {@link Direction} ID to the ID of the specified value. */
	public void setDirection(Direction direction) {
		this.directionId = direction.getId();
	}

	/** Returns this node's {@link BlockPos} long representation. */
	public long getLongPosition() {
		return pos;
	}

	/** Sets this node's {@link BlockPos} long representation to the specified value. */
	public void setLongPosition(long pos) {
		this.pos = pos;
	}

	/** Returns this node's {@link Direction} ID */
	public int getDirectionId() {
		return directionId;
	}

	/** Sets this node's {@link Direction} ID to the specified value. */
	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof NetworkMemberNode))
			return false;

		NetworkMemberNode that = (NetworkMemberNode) object;

		if (directionId == -1) {
			return this.pos == that.pos;
		} else {
			return this.pos == that.pos && this.directionId == that.directionId;
		}
	}

	/** Returns the hash for this node. */
	@Override
	public int hashCode() {
		return Objects.hashCode(pos, directionId);
	}

	/** Returns this node's string representation.
	 * For example, it may be "[273, 98, -479], NORTH" */
	@Override
	public String toString() {
		return String.format("[%s, %s, %s], %s", getBlockPosition().getX(), getBlockPosition().getY(), getBlockPosition().getY(), getDirection().asString().toUpperCase());
	}

	/** Deserializes a {@link NetworkMemberNode} from a {@link CompoundTag}. */
	public static NetworkMemberNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"), tag.getInt("dir"));
	}

	/** Serializes a {@link NetworkMemberNode} to a {@link CompoundTag}. */
	public CompoundTag toTag() {
		var tag = new CompoundTag();

		tag.putLong("pos", pos);
		tag.putInt("dir", directionId);

		return tag;
	}
}
