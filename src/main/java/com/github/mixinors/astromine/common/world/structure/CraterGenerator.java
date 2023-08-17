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

package com.github.mixinors.astromine.common.world.structure;

import com.github.mixinors.astromine.common.registry.DarkMoonOreRegistry;
import com.github.mixinors.astromine.common.registry.MoonOreRegistry;
import com.github.mixinors.astromine.registry.common.AMBiomes;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMStructureTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class CraterGenerator extends ShiftableStructurePiece {
	public CraterGenerator(Random random, int x, int z) {
		super(AMStructureTypes.CRATER_STRUCTURE_PIECE.get(), x, 64, z, 16, 16, 16, getRandomHorizontalDirection(random));
	}
	
	public CraterGenerator(NbtCompound nbt) {
		super(AMStructureTypes.CRATER_STRUCTURE_PIECE.get(), nbt);
	}
	
	@Override
	public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
		// Center surface position of the chunk.
		var cP = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		
		var biome = world.getBiome(cP).getKey().orElseThrow();
		
		var mutable = new BlockPos.Mutable();
		
		// Add randomly-sized spheres on 40% of all chunks.
		if (random.nextInt(10) < 4) {
			var cX = cP.getX();
			var cZ = cP.getZ();
			
			// Calculate maximum diameter.
			int mD;
			
			// If in a Crater Field, maximum diameter is diameter * 1.1F.
			if (!biome.equals(AMBiomes.MOON_CRATER_FIELD_KEY)) {
				mD = 16;
			} else {
				mD = 38;
			}
			
			// Calculate base radius and base diameter.
			var bD = random.nextFloat() * (mD - 4) + 4; // TODO: Check if this works!
			var bR = bD / 2.0F;
			
			var a = bR * 1.1F;
			var b = bR * random.nextFloat() * (0.75F - 0.25F) + 0.5F;
			var c = bR * 1.1F;
			
			var s = world.getBlockState(cP.down());
			
			Block ore = null;
			
			if (s.getBlock() == AMBlocks.MOON_STONE.get()) {
				ore = MoonOreRegistry.INSTANCE.getRandom(random);
			} if (s.getBlock() == AMBlocks.DARK_MOON_STONE.get()) {
				ore = DarkMoonOreRegistry.INSTANCE.getRandom(random);
			}
			
			for (var x = cX - (bD * 1.5F); x < cX + (bD * 1.5F); ++x) {
				for (var z = cZ - (bD * 1.5F); z < cZ + (bD * 1.5F); ++z) {
					var oTY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, mutable.set(x, 0, z)).getY();
					
					var oBS = world.getBlockState(mutable.set(x, oTY - 1, z));
					
					var lowestY = (int) (oTY + (bD * 0.5F));

					// Place empty blocks when inside the ellipsoid defined by [a, b, c].
					for (var y = oTY - (bD * 0.5F); y < oTY + (bD * 0.5F); ++y) {
						if ((Math.pow(x - cX, 2.0D) / Math.pow(a, 2.0D)) +
							(Math.pow(y - oTY, 2.0D) / Math.pow(b, 2.0D)) +
							(Math.pow(z - cZ, 2.0D) / Math.pow(c, 2.0D)) < 1.0F) {
							world.setBlockState(mutable.set(x, y, z), Blocks.AIR.getDefaultState(), 0);
							
							if (y < lowestY) {
								lowestY = (int) y;
							}
						}
					}
					
					--lowestY;
					
					// Get distance from center surface position of the chunk.
					var dC = (Math.pow(x - cX, 2.0D) + Math.pow(z - cZ, 2.0D));
					
					// Get border height.
					var bO = random.nextFloat() * (4 - 1) + 1;
					
					// If distance is outside of radius and inside radius extended by border offset,
					// we create the border.
					if (dC > (bR * 1.1F) * (bR * 1.1F) && dC < ((bR * 1.1F) + bO) * ((bR * 1.1F) + bO)) {
						var tY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, mutable.set(x, 0, z)).getY();
						
						// Get distance from current position to the border edge.
						var dB = ((bR * 1.1F) + bO) - Math.sqrt(dC);
						
						for (var bY = tY; bY < tY + dB; ++bY) {
							world.setBlockState(mutable.set(x, bY, z), oBS, 0);
						}
					}
					
					if (lowestY < oTY && ore != null) {
						// Add ores.
						if (random.nextInt(4) == 0) {
							world.setBlockState(mutable.set(x, lowestY, z), ore.getDefaultState(), 0);
						}
					}
				}
			}
		}
	}
}
