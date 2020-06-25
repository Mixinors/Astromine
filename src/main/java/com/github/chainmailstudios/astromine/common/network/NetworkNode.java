package com.github.chainmailstudios.astromine.common.network;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class NetworkNode {
	private long pos;

	private int dir;

	public static NetworkNode of(BlockPos blockPos, Direction direction) {
		return new NetworkNode(blockPos, direction);
	}

	public static NetworkNode of(long pos, int dir) {
		return new NetworkNode(pos, dir);
	}

	public NetworkNode() {
		pos = Long.MIN_VALUE;
	}

	public NetworkNode(BlockPos blockPos, Direction direction) {
		setBlockPos(blockPos);
		setDirection(direction);
	}

	public NetworkNode(long pos, int dir) {
		setPos(pos);
		setDir(dir);
	}

	public BlockPos getBlockPos() {
		return BlockPos.fromLong(this.pos);
	}

	public void setBlockPos(BlockPos blockPos) {
		this.pos = blockPos.asLong();
	}

	public Direction getDirection() {
		return Direction.values()[dir];
	}

	public void setDirection(Direction direction) {
		this.dir = direction.getId();
	}

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.putLong("pos", pos);
		tag.putInt("dir", dir);
		return tag;
	}

	public static NetworkNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"), tag.getInt("dir"));
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof NetworkNode)) return false;
		NetworkNode that = (NetworkNode) object;
		return pos == that.pos && dir == that.dir;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
