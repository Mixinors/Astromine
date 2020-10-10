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

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import com.github.chainmailstudios.astromine.AstromineCommon;

public class ExplosionUtilities {
	private static final BlockState AIR = Blocks.AIR.getDefaultState();

	public static void attemptExplosion(World world, int x, int y, int z, int power) {
		if (!world.isClient) {
			long start = System.currentTimeMillis();
			long blocks = explode(world, x, y, z, power);
			long end = System.currentTimeMillis();
			AstromineCommon.LOGGER.info(String.format("Took %dms to destroy %d blocks from explosion with power %d.", end - start, blocks, power));
		}
	}

	private static long explode(World access, int x, int y, int z, int radius) {
		int cr = radius >> 4;
		long blocks = 0;
		for (int cox = -cr; cox <= cr + 1; cox++) {
			for (int coz = -cr; coz <= cr + 1; coz++) {
				int box = cox * 16, boz = coz * 16;
				if (touchesOrIsIn(box, 0, boz, box + 15, 255, boz + 15, radius)) {
					int cx = (x >> 4) + cox, cz = (z >> 4) + coz;
					WorldChunk chunk = access.getChunk(cx, cz);
					blocks += forSubchunks(chunk, box, boz, x, y, z, radius);
					chunk.markDirty();
					ServerChunkManager manager = (ServerChunkManager) access.getChunkManager();
					manager.threadedAnvilChunkStorage.getPlayersWatchingChunk(new ChunkPos(cx, cz), false).forEach(s -> s.networkHandler.sendPacket(new ChunkDataS2CPacket(chunk, 65535)));
				}
			}
		}
		return blocks;
	}

	/**
	 * copied from https://stackoverflow.com/a/4579069/9773993 and converted to java
	 */
	private static boolean touchesOrIsIn(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		int squared = radius * radius;
		/* assume C1 and C2 are element-wise sorted, if not, do that now */
		if (0 < x1) {
			squared -= x1 * x1;
		} else if (0 > x2) {
			squared -= x2 * x2;
		}
		if (0 < y1) {
			squared -= y1 * y1;
		} else if (0 > y2) {
			squared -= y2 * y2;
		}
		if (0 < z1) {
			squared -= z1 * z1;
		} else if (0 > z2) {
			squared -= z2 * z2;
		}
		return squared > 0;
	}

	private static long forSubchunks(WorldChunk chunk, int bx, int bz, int x, int y, int z, int radius) {
		int scr = radius >> 4;
		int sc = y >> 4;
		long destroyed = 0;
		ChunkSection[] sections = chunk.getSectionArray();
		for (int i = -scr; i <= scr; i++) {
			int by = i * 16;
			int val = i + sc;
			if (val >= 0 && val < 16) {
				ChunkSection section = sections[val];
				if (section != null) {
					for (int ox = 0; ox < 16; ox++) {
						for (int oy = 0; oy < 16; oy++) {
							for (int oz = 0; oz < 16; oz++) {
								if (in(bx + ox, by + oy, bz + oz, radius)) {
									if (section.getBlockState(ox, oy, oz).getHardness(chunk, BlockPos.ORIGIN) != -1) {
										section.setBlockState(ox, oy, oz, AIR);
										destroyed++;
									}
								}
							}
						}
					}
					chunk.getLightingProvider().setSectionStatus(ChunkSectionPos.from(bx >> 4, i, bz >> 4), false);
				}
			}
		}
		return destroyed;
	}

	private static boolean encompassed(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		return in(x1, y1, z1, radius) && in(x1, y1, z2, radius) && in(x1, y2, z1, radius) && in(x1, y2, z2, radius) && in(x2, y1, z1, radius) && in(x2, y1, z2, radius) && in(x2, y2, z1, radius) && in(x2, y2, z2, radius);
	}

	private static boolean in(int ox, int oy, int oz, int radius) {
		return ox * ox + oy * oy + oz * oz <= radius * radius;
	}
}
