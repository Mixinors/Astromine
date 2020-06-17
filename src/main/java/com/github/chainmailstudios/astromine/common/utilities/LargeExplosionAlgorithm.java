package com.github.chainmailstudios.astromine.common.utilities;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

public class LargeExplosionAlgorithm {
	private static final Logger LOGGER = Logger.getLogger("LargeExplosions");
	private static final BlockState AIR = Blocks.AIR.getDefaultState();

	public static void explode(World world, int x, int y, int z, int power) {
		if (!world.isClient) {
			long start = System.currentTimeMillis();
			long blocks = nukeChunks(world, x, y, z, power);
			long end = System.currentTimeMillis();
			LOGGER.info(String.format("Took %dms to destroy %d blocks from explosion with power %d", end - start, blocks, power));
		}
	}

	private static long nukeChunks(World access, int x, int y, int z, int radius) {
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

	private static long forSubchunks(WorldChunk chunk, int bx, int bz, int x, int y, int z, int radius) {
		int scr = radius >> 4;
		int sc = y >> 4;
		long destroyed = 0;
		ChunkSection[] sections = chunk.getSectionArray();
		for (int i = -scr; i <= scr; i++) {
			int by = i * 16;
			if (encompassed(bx, by, bz, bx + 15, by + 15, bz + 15, radius)) {
				int val = i + sc;
				if (val >= 0 && val < 16) {
					((WorldChunkAccess) chunk).astromine_yeet(val);
					destroyed += 4096;
				}
			} else {
				int val = i + sc;
				if (val >= 0 && val < 16) {
					ChunkSection section = sections[val];
					if (section != null) {
						for (int ox = 0; ox < 16; ox++) {
							for (int oy = 0; oy < 16; oy++) {
								for (int oz = 0; oz < 16; oz++) {
									if (in(bx + ox, by + oy, bz + oz, radius)) {
										section.setBlockState(ox, oy, oz, AIR);
										destroyed++;
									}
								}
							}
						}
						chunk.getLightingProvider().updateSectionStatus(ChunkSectionPos.from(bx >> 4, i, bz >> 4), false);
					}
				}

			}
		}
		return destroyed;
	}

	/**
	 * copied from https://stackoverflow.com/a/4579069/9773993 and converted to java
	 */
	private static boolean touchesOrIsIn(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		int dist_squared = radius * radius;
		/* assume C1 and C2 are element-wise sorted, if not, do that now */
		if (0 < x1) {
			dist_squared -= x1 * x1;
		} else if (0 > x2) {
			dist_squared -= x2 * x2;
		}
		if (0 < y1) {
			dist_squared -= y1 * y1;
		} else if (0 > y2) {
			dist_squared -= y2 * y2;
		}
		if (0 < z1) {
			dist_squared -= z1 * z1;
		} else if (0 > z2) {
			dist_squared -= z2 * z2;
		}
		return dist_squared > 0;
	}

	private static boolean encompassed(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		return in(x1, y1, z1, radius) && in(x1, y1, z2, radius) && in(x1, y2, z1, radius) && in(x1, y2, z2, radius) && in(x2, y1, z1, radius) && in(x2, y1, z2, radius) && in(x2, y2, z1, radius) && in(x2, y2, z2, radius);
	}

	private static boolean in(int ox, int oy, int oz, int radius) {
		return ox * ox + oy * oy + oz * oz <= radius * radius;
	}
}
