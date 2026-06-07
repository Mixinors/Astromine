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

import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMStructures;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.material.Fluids;

public class MeteorGenerator extends ScatteredFeaturePiece {
	private static OpenSimplexNoise noise;
	
	public MeteorGenerator(RandomSource random, int x, int z) {
		super(AMStructures.METEOR_STRUCTURE_PIECE.get(), x, 64, z, 16, 16, 16, getRandomHorizontalDirection(random));
	}
	
	public MeteorGenerator(CompoundTag nbt) {
		super(AMStructures.METEOR_STRUCTURE_PIECE.get(), nbt);
	}
	
	public static void buildSphere(WorldGenLevel world, BlockPos originPos, int radius, BlockState state) {
		for (var x = -radius; x <= radius; x++) {
			for (var z = -radius; z <= radius; z++) {
				for (var y = -radius; y <= radius; y++) {
					var distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));
					
					// place blocks within spherical radius
					if (distance <= radius - ((radius * 1f / 3f) * noise.sample((originPos.getX() + x) / 10f, (originPos.getY() + y) / 10f, (originPos.getZ() + z) / 10f))) {
						world.setBlock(originPos.offset(x, y, z), state, 3);
					}
				}
			}
		}
	}
	
	@Override
	public void postProcess(WorldGenLevel world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, net.minecraft.util.RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
		if (!world.getLevel().dimension().equals(Level.OVERWORLD)) {
			return;
		}
		
		noise = new OpenSimplexNoise(world.getSeed());
		
		var originPos = world.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getMinBlockX() + 8, 0, chunkPos.getMinBlockZ() + 8));
		
		originPos = emptySphere(world, originPos, 16, state -> {
			if (world.getRandom().nextInt(10) == 0) {
				return Blocks.FIRE.defaultBlockState();
			} else {
				return Blocks.AIR.defaultBlockState();
			}
		}, state -> Blocks.COBBLESTONE.defaultBlockState());
		buildSphere(world, originPos, 8, AMBlocks.METEOR_STONE.get().defaultBlockState());
		
		for (var x = -4; x <= 4; ++x) {
			for (var y = -4; y <= 4; ++y) {
				for (var z = -4; z <= 4; ++z) {
					if (x * x + y * y + z * z > 16) {
						continue;
					}
					
					var orePosition = originPos.offset(x, y, z);
					
					if (world.getBlockState(orePosition).getBlock() == AMBlocks.METEOR_STONE.get()) {
						world.setBlock(orePosition, AMBlocks.METEOR_METITE_ORE.get().defaultBlockState(), 0b0110100);
					}
				}
			}
		}
	}
	
	private BlockPos emptySphere(WorldGenLevel world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
		var hasWater = false;
		var placedPositions = new ArrayList<BlockPos>();
		
		for (var x = -radius; x <= radius; x++) {
			for (var z = -radius; z <= radius; z++) {
				for (var y = -radius; y <= radius; y++) {
					var distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y * 1.3, 2));
					
					// place blocks within spherical radius
					if (distance <= radius + (5 * noise.sample((originPos.getX() + x) / 10f, (originPos.getZ() + z) / 10f))) {
						var offsetPos = originPos.offset(x, y, z);
						if (!hasWater && world.getFluidState(offsetPos).getType().isSame(Fluids.WATER)) {
							hasWater = true;
						}
						
						world.setBlock(offsetPos, Blocks.AIR.defaultBlockState(), 3);
						
						placedPositions.add(offsetPos);
					}
				}
			}
		}
		
		for (var placedPosition : placedPositions) {
			world.setBlock(placedPosition, hasWater && placedPosition.getY() < world.getSeaLevel() ? Fluids.WATER.getSource().defaultFluidState().createLegacyBlock() : Blocks.AIR.defaultBlockState(), 3);
		}
		
		var bottomPositions = new ArrayList<BlockPos>();
		var underneathPositions = new ArrayList<BlockPos>();
		
		for (var pos : placedPositions) {
			// store bottom block
			if (world.getBlockState(pos).isAir() && world.getBlockState(pos.below()).isRedstoneConductor(world, pos)) {
				bottomPositions.add(pos);
				underneathPositions.add(pos.below());
			}
		}
		
		for (var pos : bottomPositions) {
			world.setBlock(pos, hasWater && pos.getY() < world.getSeaLevel() ? Fluids.WATER.getSource().defaultFluidState().createLegacyBlock() : world.getRandom().nextInt(10) == 0 ? Blocks.FIRE.defaultBlockState() : Blocks.AIR.defaultBlockState(), 3);
		}
		
		for (var pos : underneathPositions) {
			world.setBlock(pos, underneath.manipulate(world.getBlockState(pos)), 3);
		}
		
		return placedPositions.stream().filter(pos -> pos.getX() == originPos.getX() && pos.getZ() == originPos.getZ()).min(Comparator.comparingInt(Vec3i::getY)).orElse(originPos).below();
	}
	
	@FunctionalInterface
	public interface GroundManipulator {
		BlockState manipulate(BlockState state);
	}
}
