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


import com.github.mixinors.astromine.techreborn.common.util.VoxelShapeUtils;
import org.jetbrains.annotations.Nullable;

public class CatwalkBlock extends Block implements Waterloggable {
	private static final VoxelShape BOTTOM = VoxelShapes.cuboid(0, 0, 0, 1, (1F / 16F), 1);
	private static final VoxelShape SIDE_WALL = VoxelShapes.cuboid(0, 0, 0, 1, 1, (1F / 16F));

	protected static final VoxelShape[] SHAPE_CACHE = new VoxelShape[256];

	protected static final BooleanProperty[] PROPERTIES = new BooleanProperty[] { ConveyorBlock.NO_FLOOR, Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST };

	public CatwalkBlock(Settings settings) {
		super(settings);

		setDefaultState(this.getDefaultState().with(ConveyorBlock.NO_FLOOR, false).with(Properties.NORTH, false).with(Properties.EAST, false).with(Properties.SOUTH, false).with(Properties.WEST, false).with(Properties.WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getStateForNeighborUpdate(this.getDefaultState(), null, null, ctx.getWorld(), ctx.getBlockPos(), null).with(Properties.WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);
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
		return switch (direction) {
			case NORTH -> Properties.NORTH;
			case EAST -> Properties.EAST;
			case SOUTH -> Properties.SOUTH;
			case WEST -> Properties.WEST;
			default -> null;
		};
	}


	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess access, BlockPos pos, BlockPos neighborPos) {
		var newStateSameType = neighborState != null && neighborState.getBlock() instanceof CatwalkBlock;

		if ((direction == Direction.NORTH && newStateSameType) || (direction == null && access.getBlockState(pos.offset(Direction.NORTH)).getBlock() instanceof CatwalkBlock))
			state = state.with(Properties.NORTH, true);
		if ((direction == Direction.EAST && newStateSameType)|| (direction == null && access.getBlockState(pos.offset(Direction.EAST)).getBlock() instanceof CatwalkBlock))
			state = state.with(Properties.EAST, true);
		if ((direction == Direction.SOUTH && newStateSameType)|| (direction == null && access.getBlockState(pos.offset(Direction.SOUTH)).getBlock() instanceof CatwalkBlock))
			state = state.with(Properties.SOUTH, true);
		if ((direction == Direction.WEST && newStateSameType)|| (direction == null && access.getBlockState(pos.offset(Direction.WEST)).getBlock() instanceof CatwalkBlock))
			state = state.with(Properties.WEST, true);
		if(direction == Direction.DOWN || (direction == null)) {
			if(neighborState == null) neighborState = access.getBlockState(pos.offset(Direction.DOWN));
			state = state.with(ConveyorBlock.NO_FLOOR, !neighborState.isSideSolidFullSquare(access, pos, Direction.UP));
		}

		return state;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		int id = 0;

		for (var property : PROPERTIES) {
			id = (id * 2) + (state.get(property) ? 1 : 0);
		}

		if (SHAPE_CACHE[id] == null) {
			var shape = VoxelShapes.empty();

			if (state.get(ConveyorBlock.NO_FLOOR)) {
				shape = VoxelShapes.union(shape, BOTTOM);
			}

			if (!state.get(Properties.NORTH)) {
				shape = VoxelShapes.union(shape, SIDE_WALL);
			}

			if (!state.get(Properties.EAST)) {
				shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(Direction.EAST, SIDE_WALL));
			}

			if (!state.get(Properties.SOUTH)) {
				shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(Direction.SOUTH, SIDE_WALL));
			}

			if (!state.get(Properties.WEST)) {
				shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(Direction.WEST, SIDE_WALL));
			}

			SHAPE_CACHE[id] = shape;
		}

		return SHAPE_CACHE[id];
	}
}
