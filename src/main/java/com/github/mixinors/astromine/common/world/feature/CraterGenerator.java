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

package com.github.mixinors.astromine.common.world.feature;

import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.ShiftableStructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class CraterGenerator extends ShiftableStructurePiece {
	private static OpenSimplexNoise noise;
	
	public CraterGenerator(Random random, int x, int z) {
		super(AMFeatures.CRATER_STRUCTURE_PIECE.get(), x, 64, z, 16, 16, 16, getRandomHorizontalDirection(random));
	}
	
	public CraterGenerator(NbtCompound nbt) {
		super(AMFeatures.CRATER_STRUCTURE_PIECE.get(), nbt);
	}
	
	public static void buildSphere(StructureWorldAccess world, BlockPos originPos, int radius, BlockState state) {
		for (var x = -radius; x <= radius; x++) {
			for (var z = -radius; z <= radius; z++) {
				for (var y = -radius; y <= radius; y++) {
					var distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));
					
					// place blocks within spherical radius
					if (distance <= radius - ((radius * 1f / 3f) * noise.sample((originPos.getX() + x) / 10f, (originPos.getY() + y) / 10f, (originPos.getZ() + z) / 10f))) {
						world.setBlockState(originPos.add(x, y, z), state, 3);
					}
				}
			}
		}
	}
	
	@Override
	public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		generate(world, chunkPos, random, blockPos);
	}
	
	public boolean generate(StructureWorldAccess world, ChunkPos chunkPos, Random random, BlockPos blockPos) {
		noise = new OpenSimplexNoise(world.getSeed());
		
		var oP = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		
		var mutable = new BlockPos.Mutable();
		
		// Add randomly-sized spheres on 40% of all chunks.
		if (random.nextInt(10) < 4) {
			var cX = oP.getX();
			var cZ = oP.getZ();
			var cY = oP.getY();
			
			// We first depress the ground in a spherical manner.
			var diameter = random.nextInt(4, 38);
			var radius = diameter / 2.0F;
			
			// Max. radius is radius * 1.25F || 48.
			
			var a = diameter * 1.1F;
			var b = diameter * random.nextFloat(0.5F, 0.75F);
			var c = diameter * 1.1F;
			
			// TODO: Fix ellipsoid in Hammer!
			
			for (var x = cX - (diameter * 1.5F); x < cX + (diameter * 1.5F); ++x) {
				for (var z = cZ - (diameter * 1.5F); z < cZ + (diameter * 1.5F); ++z) {
					
					// Create empty ellipsoid inside the area.
					var oTY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, mutable.set(x, 0, z)).getY();
					
					var oBS = world.getBlockState(mutable.set(x, oTY - 1, z));
					
					for (var y = oTY - (diameter * 0.5F); y < oTY + (diameter * 0.5F); ++y) {
						if ((Math.pow(x - cX, 2.0D) / Math.pow(a / 2.0D, 2.0D)) +
								(Math.pow(y - oTY, 2.0D) / Math.pow(b / 2.0D, 2.0D)) +
								(Math.pow(z - cZ, 2.0D) / Math.pow(c / 2.0D, 2.0D)) < 1.0F) {
							world.setBlockState(mutable.set(x, y, z), Blocks.AIR.getDefaultState(), 0x0);
						}
					}
					
					var d = (Math.pow(x - cX, 2.0D) + Math.pow(z - cZ, 2.0D));
					
					var borderHeight = random.nextInt(1, 4);
					
					if (d > (diameter * 1.1F / 2.0F) * (diameter * 1.1F / 2.0F) && d < ((diameter * 1.1F / 2.0F) + borderHeight) * ((diameter * 1.1F / 2.0F) + borderHeight)) {
						var tY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, mutable.set(x, 0, z)).getY();
						
						var delta = ((diameter * 1.1F / 2.0F) + borderHeight) - Math.sqrt(d);
						
						for (var bY = tY; bY < tY + delta; ++bY) {
							world.setBlockState(mutable.set(x, bY, z), oBS, 0);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private BlockPos emptySphere(StructureWorldAccess world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
		var hasWater = false;
		var placedPositions = new ArrayList<BlockPos>();
		
		for (var x = -radius; x <= radius; x++) {
			for (var z = -radius; z <= radius; z++) {
				for (var y = -radius; y <= radius; y++) {
					var distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y * 1.3, 2));
					
					// place blocks within spherical radius
					if (distance <= radius + (5 * noise.sample((originPos.getX() + x) / 10f, (originPos.getZ() + z) / 10f))) {
						var offsetPos = originPos.add(x, y, z);
						if (!hasWater && world.getFluidState(offsetPos).getFluid().matchesType(Fluids.WATER)) {
							hasWater = true;
						}
						
						world.setBlockState(offsetPos, Blocks.AIR.getDefaultState(), 3);
						
						placedPositions.add(offsetPos);
					}
				}
			}
		}
		
		for (var placedPosition : placedPositions) {
			world.setBlockState(placedPosition, hasWater && placedPosition.getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : Blocks.AIR.getDefaultState(), 3);
		}
		
		var bottomPositions = new ArrayList<BlockPos>();
		var underneathPositions = new ArrayList<BlockPos>();
		
		for (var pos : placedPositions) {
			// store bottom block
			if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isSolidBlock(world, pos)) {
				bottomPositions.add(pos);
				underneathPositions.add(pos.down());
			}
		}
		
		for (var pos : bottomPositions) {
			world.setBlockState(pos, hasWater && pos.getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : world.getRandom().nextInt(10) == 0 ? Blocks.FIRE.getDefaultState() : Blocks.AIR.getDefaultState(), 3);
		}
		
		for (var pos : underneathPositions) {
			world.setBlockState(pos, underneath.manipulate(world.getBlockState(pos)), 3);
		}
		
		return placedPositions.stream().filter(pos -> pos.getX() == originPos.getX() && pos.getZ() == originPos.getZ()).min(Comparator.comparingInt(Vec3i::getY)).orElse(originPos).down();
	}
	
	@FunctionalInterface
	public interface GroundManipulator {
		BlockState manipulate(BlockState state);
	}
}
