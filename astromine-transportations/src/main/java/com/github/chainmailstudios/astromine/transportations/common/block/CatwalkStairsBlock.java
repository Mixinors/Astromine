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

package com.github.chainmailstudios.astromine.transportations.common.block;

import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;
import com.github.chainmailstudios.astromine.common.utilities.RotationUtilities;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatwalkStairsBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
	private static final VoxelShape FIRST_STEP = Shapes.box(0, 0, (12F / 16F), 1, (3F / 16F), 1);
	private static final VoxelShape SECOND_STEP = Shapes.box(0, 0, (8F / 16F), 1, (7F / 16F), (12F / 16F));
	private static final VoxelShape THIRD_STEP = Shapes.box(0, 0, (4F / 16F), 1, (11F / 16F), (8F / 16F));
	private static final VoxelShape FOURTH_STEP = Shapes.box(0, 0, 0, 1, (15F / 16F), (4F / 16F));

	private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[6];

	public CatwalkStairsBlock(Properties settings) {
		super(settings);

		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(ConveyorProperties.LEFT, false).setValue(ConveyorProperties.RIGHT, false).setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ConveyorProperties.LEFT, ConveyorProperties.RIGHT, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getPlayer().isShiftKeyDown() ? context.getHorizontalDirection().getOpposite() : context.getHorizontalDirection()).setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.WATER);
	}

	public boolean isAdjacentBlockOfMyType(LevelAccessor world, BlockPos position, Direction direction) {
		BlockPos newPosition = position.relative(direction);

		BlockState blockState = world.getBlockState(newPosition);

		Block block = (null == blockState) ? null : blockState.getBlock();

		return this == block;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return state.setValue(ConveyorProperties.RIGHT, this.isAdjacentBlockOfMyType(world, pos, state.getValue(FACING).getClockWise())).setValue(ConveyorProperties.LEFT, this.isAdjacentBlockOfMyType(world, pos, state.getValue(FACING).getCounterClockWise()));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext entityContext) {
		Direction facing = state.getValue(FACING);

		if (SHAPE_CACHE[facing.get3DDataValue()] == null) {
			SHAPE_CACHE[facing.get3DDataValue()] = Shapes.or(VoxelShapeUtilities.rotate(facing, FIRST_STEP), VoxelShapeUtilities.rotate(facing, SECOND_STEP), VoxelShapeUtilities.rotate(facing, THIRD_STEP), VoxelShapeUtilities.rotate(facing, FOURTH_STEP));
		}

		return SHAPE_CACHE[facing.get3DDataValue()];
	}
}
