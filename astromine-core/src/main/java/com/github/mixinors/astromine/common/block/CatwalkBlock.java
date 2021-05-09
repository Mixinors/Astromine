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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;

public class CatwalkBlock extends Block implements Waterloggable {
	private static final VoxelShape BOTTOM = VoxelShapes.cuboid(0, 0, 0, 1, (1F / 16F), 1);
	private static final VoxelShape NORTH = VoxelShapes.cuboid(0, 0, 0, 1, 1, (1F / 16F));
	private static final VoxelShape EAST = VoxelShapes.cuboid((15F / 16F), 0, 0, 1, 1, 1);
	private static final VoxelShape SOUTH = VoxelShapes.cuboid(0, 0, (15F / 16F), 1, 1, 1);
	private static final VoxelShape WEST = VoxelShapes.cuboid(0, 0, 0, (1F / 16F), 1, 1);

	private static final VoxelShape FULL = VoxelShapes.union(BOTTOM, NORTH, EAST, SOUTH, WEST);

	protected static final VoxelShape[] SHAPE_CACHE = new VoxelShape[256];

	protected static final BooleanProperty[] PROPERTIES = new BooleanProperty[] { ConveyorBlock.NO_FLOOR, Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST };

	public CatwalkBlock(Settings settings) {
		super(settings);

		setDefaultState(this.getDefaultState().with(ConveyorBlock.NO_FLOOR, false).with(Properties.NORTH, false).with(Properties.EAST, false).with(Properties.SOUTH, false).with(Properties.WEST, false).with(Properties.WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(ConveyorBlock.NO_FLOOR, Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST, Properties.WATERLOGGED);
	}

	public BooleanProperty getPropertyFromDirection(Direction direction) {
		switch (direction) {
			case NORTH:
				return Properties.NORTH;
			case EAST:
				return Properties.EAST;
			case SOUTH:
				return Properties.SOUTH;
			case WEST:
				return Properties.WEST;
			default:
				return null;
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		boolean newStateSameType = newState.getBlock() instanceof CatwalkBlock;

		if (direction == Direction.NORTH && newStateSameType)
			newState = newState.with(Properties.NORTH, true);
		if (direction == Direction.EAST && newStateSameType)
			newState = newState.with(Properties.EAST, true);
		if (direction == Direction.SOUTH && newStateSameType)
			newState = newState.with(Properties.SOUTH, true);
		if (direction == Direction.WEST && newStateSameType)
			newState = newState.with(Properties.WEST, true);

		return newState;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return getCollisionShape(state, view, pos, context);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		int id = 0;

		for (BooleanProperty property : PROPERTIES) {
			id += id * (state.get(property) ? 1 : -1);
		}

		if (SHAPE_CACHE[id] == null) {
			VoxelShape shape = VoxelShapes.empty();

			if (!state.get(ConveyorBlock.NO_FLOOR)) {
				shape = VoxelShapes.union(shape, BOTTOM);
			}

			if (!state.get(Properties.NORTH)) {
				shape = VoxelShapes.union(shape, NORTH);
			}

			if (!state.get(Properties.EAST)) {
				shape = VoxelShapes.union(shape, EAST);
			}

			if (!state.get(Properties.SOUTH)) {
				shape = VoxelShapes.union(shape, SOUTH);
			}

			if (!state.get(Properties.WEST)) {
				shape = VoxelShapes.union(shape, WEST);
			}

			SHAPE_CACHE[id] = shape;
		}

		return SHAPE_CACHE[id];
	}
}
