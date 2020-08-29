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

package com.github.chainmailstudios.astromine.foundations.common.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceWithDimensions;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsBlocks;
import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsFeatures;
import com.terraformersmc.shapes.api.Position;
import com.terraformersmc.shapes.api.Quaternion;
import com.terraformersmc.shapes.api.Shape;
import com.terraformersmc.shapes.impl.Shapes;
import com.terraformersmc.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.shapes.impl.layer.transform.TranslateLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MeteorGenerator extends StructurePieceWithDimensions {

	private static OpenSimplexNoise noise;

	public MeteorGenerator(Random random, int x, int z) {
		super(AstromineFoundationsFeatures.METEOR_STRUCTURE, random, x, 64, z, 16, 16, 16);
	}

	public MeteorGenerator(StructureManager manager, CompoundTag tag) {
		super(AstromineFoundationsFeatures.METEOR_STRUCTURE, tag);
	}

	public static void buildSphere(StructureWorldAccess world, BlockPos originPos, int radius, BlockState state) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y, 2));

					// place blocks within spherical radius
					if (distance <= radius - ((radius * 1f / 3f) * noise.sample((originPos.getX() + x) / 10f, (originPos.getY() + y) / 10f, (originPos.getZ() + z) / 10f))) {
						world.setBlockState(originPos.add(x, y, z), state, 3);
					}
				}
			}
		}
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		return generate(world, chunkPos, random, blockPos);
	}

	public boolean generate(StructureWorldAccess world, ChunkPos chunkPos, Random random, BlockPos blockPos) {
		if (!world.toServerWorld().getRegistryKey().equals(World.OVERWORLD))
			return false;
		noise = new OpenSimplexNoise(world.getSeed());
		BlockPos originPos = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		originPos = emptySphere(world, originPos, 16, state -> {
			if (world.getRandom().nextInt(10) == 0) {
				return Blocks.FIRE.getDefaultState();
			} else {
				return Blocks.AIR.getDefaultState();
			}
		}, state -> Blocks.COBBLESTONE.getDefaultState());
		buildSphere(world, originPos, 8, AstromineFoundationsBlocks.METEOR_STONE.getDefaultState());

		Shape vein = Shapes.ellipsoid((float) 4, (float) 4, (float) 4).applyLayer(RotateLayer.of(Quaternion.of(random.nextDouble() * 360, random.nextDouble() * 360, random.nextDouble() * 360, true))).applyLayer(TranslateLayer.of(Position.of(originPos)));

		Block metiteOre = Registry.BLOCK.getOrEmpty(AstromineCommon.identifier("meteor_metite_ore")).orElse(null);
		if (metiteOre != null) {
			for (Position streamPosition : vein.stream().collect(Collectors.toSet())) {
				BlockPos orePosition = streamPosition.toBlockPos();

				if (world.getBlockState(orePosition).getBlock() == AstromineFoundationsBlocks.METEOR_STONE) {
					world.setBlockState(orePosition, metiteOre.getDefaultState(), 0b0110100);
				}
			}
		}

		return true;
	}

	private BlockPos emptySphere(StructureWorldAccess world, BlockPos originPos, int radius, GroundManipulator bottom, GroundManipulator underneath) {
		boolean hasWater = false;
		List<BlockPos> placedPositions = new ArrayList<>();

		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2) + Math.pow(y * 1.3, 2));

					// place blocks within spherical radius
					if (distance <= radius + (5 * noise.sample((originPos.getX() + x) / 10f, (originPos.getZ() + z) / 10f))) {
						BlockPos offsetPos = originPos.add(x, y, z);
						if (!hasWater && world.getFluidState(offsetPos).getFluid().matchesType(Fluids.WATER)) {
							hasWater = true;
						}

						world.setBlockState(offsetPos, Blocks.AIR.getDefaultState(), 3);

						placedPositions.add(offsetPos);
					}
				}
			}
		}

		for (BlockPos placedPosition : placedPositions) {
			world.setBlockState(placedPosition, hasWater && placedPosition.getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : Blocks.AIR.getDefaultState(), 3);
		}

		List<BlockPos> bottomPositions = new ArrayList<>();
		List<BlockPos> underneathPositions = new ArrayList<>();

		for (BlockPos pos : placedPositions) {
			// store bottom block
			if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isSolidBlock(world, pos)) {
				bottomPositions.add(pos);
				underneathPositions.add(pos.down());
			}
		}

		for (BlockPos pos : bottomPositions) {
			world.setBlockState(pos, hasWater && pos.getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : world.getRandom().nextInt(10) == 0 ? Blocks.FIRE.getDefaultState() : Blocks.AIR.getDefaultState(), 3);
		}

		for (BlockPos pos : underneathPositions) {
			world.setBlockState(pos, underneath.manipulate(world.getBlockState(pos)), 3);
		}

		return placedPositions.stream().filter(pos -> pos.getX() == originPos.getX() && pos.getZ() == originPos.getZ()).min(Comparator.comparingInt(Vec3i::getY)).orElse(originPos).offset(Direction.DOWN);
	}

	@FunctionalInterface
	public interface GroundManipulator {
		BlockState manipulate(BlockState state);
	}
}
