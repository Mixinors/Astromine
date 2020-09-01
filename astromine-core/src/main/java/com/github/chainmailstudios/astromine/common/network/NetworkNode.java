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

package com.github.chainmailstudios.astromine.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import com.google.common.base.Objects;

public class NetworkNode {
	private long pos;

	public NetworkNode(BlockPos blockPos) {
		setBlockPos(blockPos);
	}

	public NetworkNode(long pos) {
		setPos(pos);
	}

	public static NetworkNode of(BlockPos blockPos) {
		return new NetworkNode(blockPos);
	}

	public static NetworkNode of(long pos) {
		return new NetworkNode(pos);
	}

	public static NetworkNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"));
	}

	public BlockPos getBlockPos() {
		return BlockPos.fromLong(this.pos);
	}

	public void setBlockPos(BlockPos blockPos) {
		this.pos = blockPos.asLong();
	}

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putLong("pos", pos);
		return tag;
	}

	@Override
	public String toString() {
		return "NetworkMemberNode{" + "pos=" + toShortString(getBlockPos()) + '}';
	}

	private String toShortString(BlockPos pos) {
		return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (!(object instanceof NetworkNode))
			return false;

		NetworkNode that = (NetworkNode) object;
		return this.pos == that.pos;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
