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

import com.github.chainmailstudios.astromine.AstromineCommon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

/**
 * This is a concerning utility class - vini2003.
 * @author HalfOf2
 */
public class ExplosionUtilities {
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();

	/** Attempts to explode at specified position with the given power. */
	public static void attemptExplosion(Level world, int x, int y, int z, int power) {
		if (!world.isClientSide) {
			long start = System.currentTimeMillis();
			long blocks = explode(world, x, y, z, power);
			long end = System.currentTimeMillis();
			AstromineCommon.LOGGER.info(String.format("Took %dms to destroy %d blocks from explosion with power %d.", end - start, blocks, power));
		}
	}

	/** Explodes at specified position with the given power. */
	private static long explode(Level access, int x, int y, int z, int radius) {
		int cr = radius >> 4;
		long blocks = 0;
		for (int cox = -cr; cox <= cr + 1; cox++) {
			for (int coz = -cr; coz <= cr + 1; coz++) {
				int box = cox * 16, boz = coz * 16;
				if (touchesOrIsIn(box, 0, boz, box + 15, 255, boz + 15, radius)) {
					int cx = (x >> 4) + cox, cz = (z >> 4) + coz;
					LevelChunk chunk = access.getChunk(cx, cz);
					blocks += forSubchunks(chunk, box, boz, x, y, z, radius);
					chunk.markUnsaved();
					ServerChunkCache manager = (ServerChunkCache) access.getChunkSource();
					manager.chunkMap.getPlayers(new ChunkPos(cx, cz), false).forEach(s -> s.connection.send(new ClientboundLevelChunkPacket(chunk, 65535)));
				}
			}
		}
		return blocks;
	}

	/**
	 * Asserts whether a certain point is inside a given sphere or not.
	 * Originally from {@see https://stackoverflow.com/a/4579069/9773993}, adapted to Java. */
	private static boolean touchesOrIsIn(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		int squared = radius * radius;

		// Assume C1 and C2 are element-wise sorted. If not, sort them now.
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

	/** Explodes all subchunks in the given sphere. */
	private static long forSubchunks(LevelChunk chunk, int bx, int bz, int x, int y, int z, int radius) {
		int scr = radius >> 4;
		int sc = y >> 4;
		long destroyed = 0;
		LevelChunkSection[] sections = chunk.getSections();
		for (int i = -scr; i <= scr; i++) {
			int by = i * 16;
			int val = i + sc;
			if (val >= 0 && val < 16) {
				LevelChunkSection section = sections[val];
				if (section != null) {
					for (int ox = 0; ox < 16; ox++) {
						for (int oy = 0; oy < 16; oy++) {
							for (int oz = 0; oz < 16; oz++) {
								if (in(bx + ox, by + oy, bz + oz, radius)) {
									if (section.getBlockState(ox, oy, oz).getDestroySpeed(chunk, BlockPos.ZERO) != -1) {
										section.setBlockState(ox, oy, oz, AIR);
										destroyed++;
									}
								}
							}
						}
					}
					chunk.getLightEngine().updateSectionStatus(SectionPos.of(bx >> 4, i, bz >> 4), false);
				}
			}
		}
		return destroyed;
	}

	/** Asserts... something?! */
	private static boolean in(int ox, int oy, int oz, int radius) {
		return ox * ox + oy * oy + oz * oz <= radius * radius;
	}
}
