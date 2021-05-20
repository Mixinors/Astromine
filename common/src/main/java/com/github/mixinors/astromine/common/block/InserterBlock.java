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

package com.github.mixinors.astromine.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.block.entity.InserterBlockEntity;
import com.github.mixinors.astromine.common.conveyor.ConveyableBlock;

import java.util.Random;

public class InserterBlock extends HorizontalFacingBlock implements BlockEntityProvider, ConveyableBlock, Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, (1 / 16F), 1);;

	private final String type;

	private final int speed;

	public InserterBlock(String type, int speed, Settings settings) {
		super(settings);

		this.type = type;

		this.speed = speed;

		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	public String getType() {
		return type;
	}

	public int getSpeed() {
		return speed;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new InserterBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, Properties.POWERED, Properties.WATERLOGGED);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(Properties.POWERED, false).with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing()).with(Properties.WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean notify) {
		if (!world.isClient) {
			var powered = state.get(Properties.POWERED);

			if (powered != world.isReceivingRedstonePower(pos)) {
				if (powered) {
					world.getBlockTickScheduler().schedule(pos, this, 4);
				} else {
					world.setBlockState(pos, state.cycle(Properties.POWERED), 2);
				}
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(Properties.POWERED) && !world.isReceivingRedstonePower(pos)) {
			world.setBlockState(pos, state.cycle(Properties.POWERED), 2);
		}
	}


	@Override
	public void onBlockAdded(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean notify) {
		updateDiagonals(world, this, pos);
	}

	@Override
	public void onStateReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (oldState.getBlock() != newState.getBlock()) {
			var blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof InserterBlockEntity inserterBlockEntity) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), inserterBlockEntity.getItemComponent().getFirst());
			}

			super.onStateReplaced(oldState, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}
