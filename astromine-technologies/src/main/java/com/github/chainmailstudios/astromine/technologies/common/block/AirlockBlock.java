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

package com.github.chainmailstudios.astromine.technologies.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;

import javax.annotation.Nullable;

public class AirlockBlock extends Block implements SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	public static final VoxelShape LEFT_SHAPE = Block.box(0, 0, 6, 1, 16, 10);
	public static final VoxelShape RIGHT_SHAPE = Block.box(15, 0, 6, 16, 16, 10);
	public static final VoxelShape DOOR_SHAPE = Block.box(1, 0, 7, 15, 16, 9);
	public static final VoxelShape BOTTOM_SHAPE = Block.box(0, 0, 5, 16, 1, 11);
	public static final VoxelShape TOP_SHAPE = Block.box(0, 15, 6, 16, 16, 10);

	private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[64];

	public AirlockBlock(Properties settings) {
		super(settings);

		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(POWERED, false).setValue(HALF, DoubleBlockHalf.LOWER).setValue(LEFT, false).setValue(RIGHT, false).setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);

		int id = facing.get3DDataValue() + (state.getValue(HALF) == DoubleBlockHalf.LOWER ? 1 : 2) + (!state.getValue(LEFT) ? 4 : 8) + (!state.getValue(RIGHT) ? 16: 32);

		if (SHAPE_CACHE[id] == null) {
			VoxelShape shape = Shapes.or(Shapes.empty(), VoxelShapeUtilities.rotate(facing, DOOR_SHAPE));

			if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
				shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, BOTTOM_SHAPE));
			} else {
				shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, TOP_SHAPE));
			}

			if (!state.getValue(LEFT)) {
				shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, LEFT_SHAPE));
			}

			if (!state.getValue(RIGHT)) {
				shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, RIGHT_SHAPE));
			}

			SHAPE_CACHE[id] = shape;
		}

		return SHAPE_CACHE[id];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		VoxelShape shape = Shapes.empty();
		Direction facing = state.getValue(FACING);

		if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
			shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, BOTTOM_SHAPE));
		} else {
			shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, TOP_SHAPE));
		}

		if (!state.getValue(LEFT)) {
			shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, LEFT_SHAPE));
		}

		if (!state.getValue(RIGHT)) {
			shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, RIGHT_SHAPE));
		}

		if (!state.getValue(POWERED)) {
			shape = Shapes.or(shape, VoxelShapeUtilities.rotate(facing, DOOR_SHAPE));
		}

		return shape;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
		Direction facing = state.getValue(FACING);
		BlockState changedState = state;

		if (direction == facing.getClockWise() || direction == facing.getCounterClockWise()) {
			if (newState.is(this) && (newState.getValue(FACING) == facing || newState.getValue(FACING) == facing.getOpposite())) {
				if (direction == facing.getCounterClockWise()) {
					changedState = changedState.setValue(LEFT, true);
				} else if (direction == facing.getClockWise()) {
					changedState = changedState.setValue(RIGHT, true);
				}
			} else if (direction == facing.getCounterClockWise()) {
				changedState = changedState.setValue(LEFT, false);
			} else if (direction == facing.getClockWise()) {
				changedState = changedState.setValue(RIGHT, false);
			}
		}

		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
			return newState.is(this) && newState.getValue(HALF) != doubleBlockHalf ? changedState.setValue(FACING, newState.getValue(FACING)).setValue(POWERED, newState.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !changedState.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : changedState;
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide && player.isCreative()) {
			DoublePlantBlock.preventCreativeDropFromBottomPart(world, pos, state, player);
		}

		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		switch (type) {
			case LAND:
			case AIR:
				return state.getValue(POWERED);
			default:
				return false;
		}
	}

	private int getOpenSoundEventId() {
		return 1011;
	}

	private int getCloseSoundEventId() {
		return 1005;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos blockPos = ctx.getClickedPos();
		if (blockPos.getY() < 255 && ctx.getLevel().getBlockState(blockPos.above()).canBeReplaced(ctx)) {
			Level world = ctx.getLevel();
			boolean bl = world.hasNeighborSignal(blockPos) || world.hasNeighborSignal(blockPos.above());
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(POWERED, bl).setValue(HALF, DoubleBlockHalf.LOWER).setValue(BlockStateProperties.WATERLOGGED, world.getBlockState(blockPos).getBlock() == Blocks.WATER);
		} else {
			return null;
		}
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		world.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public boolean method_30841(BlockState blockState) {
		return blockState.getValue(POWERED);
	}

	public void setOpen(Level world, BlockState blockState, BlockPos blockPos, boolean bl) {
		if (blockState.is(this) && blockState.getValue(POWERED) != bl) {
			world.setBlock(blockPos, blockState.setValue(POWERED, bl), 10);
			this.playOpenCloseSound(world, blockPos, bl);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		boolean bl = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.relative(state.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (block != this && bl != state.getValue(POWERED)) {
			if (bl != state.getValue(POWERED)) {
				this.playOpenCloseSound(world, pos, bl);
			}

			world.setBlock(pos, state.setValue(POWERED, bl), 2);
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos blockPos = pos.below();
		BlockState blockState = world.getBlockState(blockPos);
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? blockState.isFaceSturdy(world, blockPos, Direction.UP) : blockState.is(this);
	}

	private void playOpenCloseSound(Level world, BlockPos pos, boolean open) {
		world.levelEvent(null, open ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return mirror == Mirror.NONE ? state : state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public long getSeed(BlockState state, BlockPos pos) {
		return Mth.getSeed(pos.getX(), pos.below(state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF, FACING, POWERED, LEFT, RIGHT, BlockStateProperties.WATERLOGGED);
	}
}
