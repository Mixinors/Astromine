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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class WorldPos {
	@NotNull
	private final World world;
	@NotNull
	private final BlockPos pos;
	private BlockState blockState;

	private WorldPos(World world, BlockPos pos) {
		this.world = Objects.requireNonNull(world);
		this.pos = Objects.requireNonNull(pos);
	}

	public static WorldPos of(World world, BlockPos pos) {
		return new WorldPos(world, pos);
	}

	public WorldPos offset(Direction direction) {
		return of(world, getBlockPos().offset(direction));
	}

	public BlockState getBlockState() {
		if (blockState == null) {
			this.blockState = world.getBlockState(pos);
		}

		return blockState;
	}

	public void setBlockState(BlockState state) {
		this.blockState = null;

		this.world.setBlockState(pos, state);
	}

	public Block getBlock() {
		return getBlockState().getBlock();
	}

	@NotNull
	public World getWorld() {
		return world;
	}

	@NotNull
	public BlockPos getBlockPos() {
		return pos;
	}

	public int getX() {
		return pos.getX();
	}

	public int getY() {
		return pos.getY();
	}

	public int getZ() {
		return pos.getZ();
	}

	@Nullable
	public BlockEntity getBlockEntity() {
		return world.getBlockEntity(pos);
	}
}
