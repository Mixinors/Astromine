package com.github.chainmailstudios.astromine.common.multiblock;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MultiblockNode {
	private long pos;

	public static MultiblockNode of(BlockPos blockPos) {
		return new MultiblockNode(blockPos);
	}

	public static MultiblockNode of(long pos) {
		return new MultiblockNode(pos);
	}

	public MultiblockNode(BlockPos blockPos) {
		setBlockPos(blockPos);
	}

	public MultiblockNode(long pos) {
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

	public static MultiblockNode fromTag(CompoundTag tag) {
		return of(tag.getLong("pos"));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MultiblockNode)) return false;
		MultiblockNode that = (MultiblockNode) o;
		return pos == that.pos;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(pos);
	}
}
