package com.github.chainmailstudios.astromine.common.network;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class NetworkMemberNode {
	private long pos;

	private int dir;

	public static NetworkMemberNode of(BlockPos blockPos, Direction direction) {
		return new NetworkMemberNode(blockPos, direction);
	}

	public static NetworkMemberNode of(long pos, int dir) {
		return new NetworkMemberNode(pos, dir);
	}

	public static NetworkMemberNode of(BlockPos blockPos) {
		return new NetworkMemberNode(blockPos);
	}

	public static NetworkMemberNode of(long pos) {
		return new NetworkMemberNode(pos);
	}

	public NetworkMemberNode(BlockPos blockPos, Direction direction) {
		setBlockPos(blockPos);
		setDirection(direction);
	}

	public NetworkMemberNode(long pos, int dir) {
		setPos(pos);
		setDir(dir);
	}

	public NetworkMemberNode(BlockPos blockPos) {
		setBlockPos(blockPos);
		setDir(-1);
	}

	public NetworkMemberNode(long pos) {
		setPos(pos);
		setDir(-1);
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

	public static NetworkMemberNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"), tag.getInt("dir"));
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof NetworkMemberNode)) return false;

		NetworkMemberNode that = (NetworkMemberNode) object;

		if (dir == -1) {
			return this.pos == that.pos;
		} else {
			return this.pos == that.pos && this.dir == that.dir;
		}
	}

	@Override
	public String toString() {
		return "NetworkMemberNode{" +
		       "pos=" + toShortString(getBlockPos()) +
		       ", dir=" + getDirection().getName() +
		       '}';
	}

	private String toShortString(BlockPos pos) {
		return "" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
