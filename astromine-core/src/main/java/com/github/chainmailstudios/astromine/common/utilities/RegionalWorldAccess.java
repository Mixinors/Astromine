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

package com.github.chainmailstudios.astromine.common.utilities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;

/**
 * faster world access for small areas (like when u don't need to access blocks on the other side of the world) warning:
 * not threadsafe!
 */
public class RegionalWorldAccess {
	public final World world;
	public final boolean isClient;
	private final BlockPos.Mutable current = new BlockPos.Mutable();
	private WorldChunk chunk;
	private int cx, cz;

	public RegionalWorldAccess(World world, BlockPos pos) {
		this(world.getWorldChunk(pos));
	}

	public RegionalWorldAccess(WorldChunk chunk) {
		this.chunk = chunk;
		this.world = chunk.getWorld();
		this.isClient = this.world.isClient;
		ChunkPos pos = chunk.getPos();
		this.cx = pos.x;
		this.cz = pos.z;
		this.addDetatch();
	}

	private void addDetatch() {
		((WorldChunkAccess) this.chunk).astromine_addUnloadListener(() -> this.chunk = null);
	}

	public boolean setBlockState(BlockState state, int x, int y, int z) {
		return this.setBlockState(state, x, y, z, 3);
	}

	// from World#setBlockState
	public boolean setBlockState(BlockState state, int x, int y, int z, int flags) {
		if (y < 0 || y > 255) {
			return false;
		} else {
			WorldChunk chunk = this.chunk;
			boolean isClient = this.isClient;
			World world = this.world;
			BlockPos pos = this.current.toImmutable();
			this.moveTo(x >> 4, z >> 4);
			Block block = state.getBlock();
			this.current.set(x, y, z);
			BlockState blockState = chunk.setBlockState(pos, state, (flags & 64) != 0);
			if (blockState == null) {
				return false;
			} else if (!isClient && world.isDebugWorld()) {
				return false;
			} else {
				BlockState blockState2 = this.getBlockState(x, y, z);
				if (blockState2 != blockState && (blockState2.getOpacity(world, pos) != blockState.getOpacity(world, pos) || blockState2.getLuminance() != blockState.getLuminance() || blockState2.hasSidedTransparency() || blockState.hasSidedTransparency())) {
					world.getChunkManager().getLightingProvider().checkBlock(pos);
				}

				if (blockState2 == state) {
					if (blockState != blockState2) {
						world.scheduleBlockRerenderIfNeeded(pos, blockState, blockState2);
					}

					if ((flags & 2) != 0 && (!isClient || (flags & 4) == 0) && (isClient || chunk.getLevelType() != null && chunk.getLevelType().isAfter(ChunkHolder.LevelType.TICKING))) {
						world.updateListeners(pos, blockState, state, flags);
					}

					if ((flags & 1) != 0) {
						if (!world.isDebugWorld()) {
							// World#updateNeighbors or something
							this.updateNeighbor(pos.west(), block, pos);
							this.updateNeighbor(pos.east(), block, pos);
							this.updateNeighbor(pos.down(), block, pos);
							this.updateNeighbor(pos.up(), block, pos);
							this.updateNeighbor(pos.north(), block, pos);
							this.updateNeighbor(pos.south(), block, pos);
						}

						world.updateNeighbors(pos, blockState.getBlock());
						if (!this.isClient && state.hasComparatorOutput()) {
							world.updateComparators(pos, block);
						}
					}

					if ((flags & 16) == 0) {
						int i = flags & -34;
						blockState.prepare(world, pos, i, 512);
						state.updateNeighbors(world, pos, i, 512);
						state.prepare(world, pos, i, 512);
					}

					world.onBlockChanged(pos, blockState, blockState2);
				}

				return true;
			}
		}
	}

	public BlockState getBlockState(int x, int y, int z) {
		this.moveTo(x >> 4, z >> 4);
		this.current.set(x, y, z);
		return this.chunk.getBlockState(this.current);
	}

	public WorldChunk getChunk(int cx, int cz) {
		this.moveTo(cx, cz);
		return this.chunk;
	}

	private void moveTo(int toCx, int toCz) {
		this.moveIn(toCx - this.cx, toCz - this.cz);
	}

	private void moveIn(int offX, int offZ) {
		this.cz += offZ;
		this.cx += offX;

		if (this.chunk != null) {
			while (offX > 0) {
				this.chunk = ((WorldChunkAccess) this.chunk).astromine_east();
				offX--;
			}

			while (offX < 0) {
				this.chunk = ((WorldChunkAccess) this.chunk).astromine_west();
				offX++;
			}

			while (offZ > 0) {
				this.chunk = ((WorldChunkAccess) this.chunk).astromine_south();
				offZ--;
			}
			while (offZ < 0) {
				this.chunk = ((WorldChunkAccess) this.chunk).astromine_north();
				offZ++;
			}
		} else {
			this.chunk = this.world.getChunk(this.cx, this.cz);
		}

		this.addDetatch();
	}

	public void updateNeighbor(BlockPos sourcePos, Block sourceBlock, BlockPos neighborPos) {
		if (!this.isClient) {
			BlockState blockState = this.getBlockState(sourcePos.getX(), sourcePos.getY(), sourcePos.getZ());
			try {
				blockState.neighborUpdate(this.world, sourcePos, sourceBlock, neighborPos, false);
			} catch (Throwable var8) {
				CrashReport crashReport = CrashReport.create(var8, "Exception while updating neighbours");
				CrashReportSection crashReportSection = crashReport.addElement("Block being updated");
				crashReportSection.add("Source block type", () -> {
					try {
						return String.format("ID #%s (%s // %s)", Registry.BLOCK.getId(sourceBlock), sourceBlock.getTranslationKey(), sourceBlock.getClass().getCanonicalName());
					} catch (Throwable var2) {
						return "ID #" + Registry.BLOCK.getId(sourceBlock);
					}
				});
				CrashReportSection.addBlockInfo(crashReportSection, sourcePos, blockState);
				throw new CrashException(crashReport);
			}
		}
	}
}
