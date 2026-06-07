/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public class ExplosionUtils {
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();
	
	public static void attemptExplosion(Level world, int x, int y, int z, int power) {
		if (!world.isClientSide) {
			explode(world, x, y, z, power);
		}
	}
	
	private static long explode(Level access, int x, int y, int z, int radius) {
		var cr = radius >> 4;
		var blocks = 0;
		for (var cox = -cr; cox <= cr + 1; cox++) {
			for (var coz = -cr; coz <= cr + 1; coz++) {
				var box = cox * 16;
				var boz = coz * 16;
				
				if (touchesOrIsIn(box, 0, boz, box + 15, 255, boz + 15, radius)) {
					var cx = (x >> 4) + cox;
					var cz = (z >> 4) + coz;
					
					var chunk = access.getChunk(cx, cz);
					
					blocks += forSubChunks(chunk, box, boz, x, y, z, radius);
					
					chunk.setUnsaved(true);
					
					var manager = (ServerChunkCache) access.getChunkSource();
					
					manager.chunkMap.getPlayers(new ChunkPos(cx, cz), false).forEach(player ->
							player.connection.send(new ClientboundLevelChunkWithLightPacket(chunk, manager.getLightEngine(), null, null))
					);
				}
			}
		}
		
		return blocks;
	}
	
	/**
	 * Asserts whether a certain point is inside a given sphere or not. Originally from {@see https://stackoverflow.com/a/4579069/9773993}, adapted to Java.
	 */
	private static boolean touchesOrIsIn(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
		var squared = radius * radius;
		
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
	
	/**
	 * Explodes all sub chunks in the given sphere.
	 */
	private static long forSubChunks(LevelChunk chunk, int bx, int bz, int x, int y, int z, int radius) {
		var scr = radius >> 4;
		
		var sc = y >> 4;
		
		var destroyed = 0;
		
		var sections = chunk.getSections();
		
		for (var i = -scr; i <= scr; i++) {
			var by = i * 16;
			var val = i + sc;
			
			if (val >= 0 && val < 16) {
				var section = sections[val];
				
				if (section != null) {
					for (var ox = 0; ox < 16; ox++) {
						for (var oy = 0; oy < 16; oy++) {
							for (var oz = 0; oz < 16; oz++) {
								if (isIn(bx + ox, by + oy, bz + oz, radius)) {
									if (section.getBlockState(ox, oy, oz).getDestroySpeed(chunk, BlockPos.ZERO) != -1) {
										section.setBlockState(ox, oy, oz, AIR);
										
										destroyed++;
									}
								}
							}
						}
					}
					
					chunk.getLevel().getLightEngine().updateSectionStatus(SectionPos.of(bx >> 4, i, bz >> 4), false);
				}
			}
		}
		
		return destroyed;
	}
	
	private static boolean isIn(int ox, int oy, int oz, int radius) {
		return ox * ox + oy * oy + oz * oz <= radius * radius;
	}
}
