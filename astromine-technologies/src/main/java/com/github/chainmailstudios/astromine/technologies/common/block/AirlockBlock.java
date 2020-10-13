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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import com.github.chainmailstudios.astromine.common.utilities.VoxelShapeUtilities;

import javax.annotation.Nullable;

public class AirlockBlock extends Block implements Waterloggable {
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty POWERED = Properties.POWERED;
	public static final BooleanProperty LEFT = BooleanProperty.of("left");
	public static final BooleanProperty RIGHT = BooleanProperty.of("right");
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	public static final VoxelShape LEFT_SHAPE = Block.createCuboidShape(0, 0, 6, 1, 16, 10);
	public static final VoxelShape RIGHT_SHAPE = Block.createCuboidShape(15, 0, 6, 16, 16, 10);
	public static final VoxelShape DOOR_SHAPE = Block.createCuboidShape(1, 0, 7, 15, 16, 9);
	public static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0, 0, 5, 16, 1, 11);
	public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0, 15, 6, 16, 16, 10);

	public AirlockBlock(Settings settings) {
		super(settings);

		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(HALF, DoubleBlockHalf.LOWER).with(LEFT, false).with(RIGHT, false).with(Properties.WATERLOGGED, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction facing = state.get(FACING);
		VoxelShape shape = VoxelShapes.union(VoxelShapes.empty(), VoxelShapeUtilities.rotateDirection(facing, DOOR_SHAPE));

		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, BOTTOM_SHAPE));
		} else {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, TOP_SHAPE));
		}

		if (!state.get(LEFT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, LEFT_SHAPE));
		}

		if (!state.get(RIGHT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, RIGHT_SHAPE));
		}

		return shape;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = VoxelShapes.empty();
		Direction facing = state.get(FACING);

		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, BOTTOM_SHAPE));
		} else {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, TOP_SHAPE));
		}

		if (!state.get(LEFT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, LEFT_SHAPE));
		}

		if (!state.get(RIGHT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, RIGHT_SHAPE));
		}

		if (!state.get(POWERED)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtilities.rotateDirection(facing, DOOR_SHAPE));
		}

		return shape;
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		Direction facing = state.get(FACING);
		BlockState changedState = state;

		if (direction == facing.rotateYClockwise() || direction == facing.rotateYCounterclockwise()) {
			if (newState.isOf(this) && (newState.get(FACING) == facing || newState.get(FACING) == facing.getOpposite())) {
				if (direction == facing.rotateYCounterclockwise()) {
					changedState = changedState.with(LEFT, true);
				} else if (direction == facing.rotateYClockwise()) {
					changedState = changedState.with(RIGHT, true);
				}
			} else if (direction == facing.rotateYCounterclockwise()) {
				changedState = changedState.with(LEFT, false);
			} else if (direction == facing.rotateYClockwise()) {
				changedState = changedState.with(RIGHT, false);
			}
		}

		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
			return newState.isOf(this) && newState.get(HALF) != doubleBlockHalf ? changedState.with(FACING, newState.get(FACING)).with(POWERED, newState.get(POWERED)) : Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !changedState.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : changedState;
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient && player.isCreative()) {
			TallPlantBlock.onBreakInCreative(world, pos, state, player);
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		switch (type) {
			case LAND:
			case AIR:
				return state.get(POWERED);
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
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		if (blockPos.getY() < 255 && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx)) {
			World world = ctx.getWorld();
			boolean bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
			return this.getDefaultState().with(FACING, ctx.getPlayerFacing()).with(POWERED, bl).with(HALF, DoubleBlockHalf.LOWER).with(Properties.WATERLOGGED, world.getBlockState(blockPos).getBlock() == Blocks.WATER);
		} else {
			return null;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public boolean method_30841(BlockState blockState) {
		return blockState.get(POWERED);
	}

	public void setOpen(World world, BlockState blockState, BlockPos blockPos, boolean bl) {
		if (blockState.isOf(this) && blockState.get(POWERED) != bl) {
			world.setBlockState(blockPos, blockState.with(POWERED, bl), 10);
			this.playOpenCloseSound(world, blockPos, bl);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (block != this && bl != state.get(POWERED)) {
			if (bl != state.get(POWERED)) {
				this.playOpenCloseSound(world, pos, bl);
			}

			world.setBlockState(pos, state.with(POWERED, bl), 2);
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		return state.get(HALF) == DoubleBlockHalf.LOWER ? blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
	}

	private void playOpenCloseSound(World world, BlockPos pos, boolean open) {
		world.syncWorldEvent(null, open ? this.getCloseSoundEventId() : this.getOpenSoundEventId(), pos, 0);
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return mirror == BlockMirror.NONE ? state : state.rotate(mirror.getRotation(state.get(FACING)));
	}

	@Environment(EnvType.CLIENT)
	@Override
	public long getRenderingSeed(BlockState state, BlockPos pos) {
		return MathHelper.hashCode(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF, FACING, POWERED, LEFT, RIGHT, Properties.WATERLOGGED);
	}
}
