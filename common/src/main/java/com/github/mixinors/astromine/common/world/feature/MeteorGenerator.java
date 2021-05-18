/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.noise.OpenSimplexNoise;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.github.mixinors.astromine.registry.common.AMFeatures;
import com.terraformersmc.terraform.shapes.api.Position;
import com.terraformersmc.terraform.shapes.api.Quaternion;
import com.terraformersmc.terraform.shapes.api.Shape;
import com.terraformersmc.terraform.shapes.impl.Shapes;
import com.terraformersmc.terraform.shapes.impl.layer.transform.RotateLayer;
import com.terraformersmc.terraform.shapes.impl.layer.transform.TranslateLayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePieceWithDimensions;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MeteorGenerator extends StructurePieceWithDimensions {

	private static OpenSimplexNoise noise;

	public MeteorGenerator(Random random, int x, int z) {
		super(AMFeatures.METEOR_STRUCTURE.get(), random, x, 64, z, 16, 16, 16);
	}

	public MeteorGenerator(StructureManager manager, CompoundTag tag) {
		super(AMFeatures.METEOR_STRUCTURE.get(), tag);
	}
	
	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		if (!world.toServerWorld().getRegistryKey().equals(World.OVERWORLD)) {
			return false;
		}
		
		noise = new OpenSimplexNoise(world.getSeed());
		
		var topCenterPos = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(chunkPos.getStartX() + 8, 0, chunkPos.getStartZ() + 8));
		
		var ref = new Object() {
			boolean hasWater = false;
		};
		
		// Generate the impact region.
		var centerPos = Shapes.ellipsoid(16.0D, 16.0D, 16.0D)
				.applyLayer(
						TranslateLayer.of(Position.of(topCenterPos))
				).stream()
				.filter(pos -> Math.sqrt(topCenterPos.getSquaredDistance(pos.getX(), pos.getY(), pos.getZ(), false)) <= 16.0F + (5 * noise.sample(pos.getX() / 10.0F, pos.getZ() / 10.0F)))
				.peek(pos -> {
					if (!ref.hasWater && world.getFluidState(pos.toBlockPos()).isIn(FluidTags.WATER)) {
						ref.hasWater = true;
					}
					
					world.setBlockState(pos.toBlockPos(), Blocks.AIR.getDefaultState(), 0b011);
					world.setBlockState(pos.toBlockPos(), ref.hasWater && pos.getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : Blocks.AIR.getDefaultState(), 0b011);
					
					if (world.getBlockState(pos.toBlockPos()).isAir() && world.getBlockState(pos.toBlockPos().down()).isSolidBlock(world, pos.toBlockPos())) {
						world.setBlockState(pos.toBlockPos(), ref.hasWater && pos.toBlockPos().getY() < world.getSeaLevel() ? Fluids.WATER.getStill().getDefaultState().getBlockState() : world.getRandom().nextInt(10) == 0 ? Blocks.FIRE.getDefaultState() : Blocks.AIR.getDefaultState(), 0b011);
						world.setBlockState(pos.toBlockPos().down(), Blocks.COBBLESTONE.getDefaultState(), 0b011);
					}
				})
				.filter(pos -> pos.getX() == topCenterPos.getX() && pos.getZ() == topCenterPos.getZ())
				.min(Comparator.comparingDouble(Position::getY))
				.orElse(Position.of(topCenterPos))
				.toBlockPos()
				.down();

		// Generate the meteor body.
		Shapes.ellipsoid(16.0D, 16.0D, 16.0D)
				.applyLayer(
						TranslateLayer.of(Position.of(centerPos))
				).stream()
				.filter(pos -> Math.sqrt(centerPos.getSquaredDistance(pos.getX(), pos.getY(), pos.getZ(), false)) <= 16.0F - ((16.0F / 3.0F) * noise.sample(pos.getX() / 10.0F, pos.getY() / 10.0F, pos.getZ() / 10.0F)))
				.forEach(pos -> world.setBlockState(pos.toBlockPos(), AMBlocks.METEOR_STONE.get().getDefaultState(), 0b011));

		// Generate the ore inside the meteor.
		Shapes.ellipsoid(4.0D, 4.0D, 4.0D)
			.applyLayer(
					RotateLayer.of(Quaternion.of(random.nextDouble() * 360.0D, random.nextDouble() * 360.0D, random.nextDouble() * 360.0D, true))
			).applyLayer(
					TranslateLayer.of(Position.of(centerPos))
			).stream()
			.filter(pos -> world.getBlockState(pos.toBlockPos()).getBlock() == AMBlocks.METEOR_STONE.get())
			.forEach(pos -> world.setBlockState(pos.toBlockPos(), AMBlocks.METEOR_METITE_ORE.get().getDefaultState(), 0b0110100));

		return true;
	}
}
