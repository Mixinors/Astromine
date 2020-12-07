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

package com.github.chainmailstudios.astromine.common.utilities.data.position;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * A {@link BlockPos} with an associated {@link World}.
 */
public final class WorldPos {
	private final World world;

	private final BlockPos pos;

	private BlockState blockState;

	/** Instantiates a {@link WorldPos}. */
	private WorldPos(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	/** Instantiates a {@link WorldPos}. */
	public static WorldPos of(World world, BlockPos pos) {
		return new WorldPos(world, pos);
	}

	/** Returns a position offset to this one by the given {@link Direction}. */
	public WorldPos offset(Direction direction) {
		return of(world, getBlockPos().offset(direction));
	}

	/** Returns the {@link Block} at this position's {@link BlockPos}. */
	public Block getBlock() {
		return getBlockState().getBlock();
	}

	/** Returns the {@link BlockState} at this position's {@link BlockPos}. */
	public BlockState getBlockState() {
		if (blockState == null) {
			this.blockState = world.getBlockState(pos);
		}

		return blockState;
	}

	/** Returns the {@link BlockEntity} at this position's {@link BlockPos}. */
	public BlockEntity getBlockEntity() {
		return world.getBlockEntity(pos);
	}

	/** Returns this position's {@link World}. */
	public World getWorld() {
		return world;
	}

	/** Returns this position's {@link BlockPos}. */
	public BlockPos getBlockPos() {
		return pos;
	}

	/** Returns this position's {@link BlockPos}'s X-axis coordinate. */
	public int getX() {
		return pos.getX();
	}

	/** Returns this position's {@link BlockPos}'s Y-axis coordinate. */
	public int getY() {
		return pos.getY();
	}

	/** Returns this position's {@link BlockPos}'s Z-axis coordinate. */
	public int getZ() {
		return pos.getZ();
	}
}
