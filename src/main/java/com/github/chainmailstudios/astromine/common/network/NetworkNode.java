package com.github.chainmailstudios.astromine.common.network;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class NetworkNode {
	private long pos;

	public static NetworkNode of(BlockPos blockPos) {
		return new NetworkNode(blockPos);
	}

	public static NetworkNode of(long pos) {
		return new NetworkNode(pos);
	}

	public NetworkNode(BlockPos blockPos) {
		setBlockPos(blockPos);
	}

	public NetworkNode(long pos) {
		setPos(pos);
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

	public static NetworkNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"));
	}

	@Override
	public String toString() {
		return "NetworkMemberNode{" +
		       "pos=" + toShortString(getBlockPos()) +
		       '}';
	}

	private String toShortString(BlockPos pos) {
		return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof NetworkNode)) return false;

		NetworkNode that = (NetworkNode) object;
		return this.pos == that.pos;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
