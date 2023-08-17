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

package com.github.mixinors.astromine.common.block;

import com.github.mixinors.astromine.common.util.VoxelShapeUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
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
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

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
		super(settings.pistonBehavior(PistonBehavior.DESTROY));
		
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(HALF, DoubleBlockHalf.LOWER).with(LEFT, false).with(RIGHT, false).with(Properties.WATERLOGGED, false));
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction facing = state.get(FACING);
		VoxelShape shape = VoxelShapes.union(VoxelShapes.empty(), VoxelShapeUtils.rotate(facing, DOOR_SHAPE));
		
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, BOTTOM_SHAPE));
		} else {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, TOP_SHAPE));
		}
		
		if (!state.get(LEFT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, LEFT_SHAPE));
		}
		
		if (!state.get(RIGHT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, RIGHT_SHAPE));
		}
		
		return shape;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var shape = VoxelShapes.empty();
		var facing = state.get(FACING);
		
		if (state.get(HALF) == DoubleBlockHalf.LOWER) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, BOTTOM_SHAPE));
		} else {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, TOP_SHAPE));
		}
		
		if (!state.get(LEFT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, LEFT_SHAPE));
		}
		
		if (!state.get(RIGHT)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, RIGHT_SHAPE));
		}
		
		if (!state.get(POWERED)) {
			shape = VoxelShapes.union(shape, VoxelShapeUtils.rotate(facing, DOOR_SHAPE));
		}
		
		return shape;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		var doubleBlockHalf = state.get(HALF);
		var facing = state.get(FACING);
		var changedState = state;
		
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
		return switch (type) {
			case LAND, AIR -> state.get(POWERED);
			default -> false;
		};
	}
	
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		var blockPos = ctx.getBlockPos();
		if (blockPos.getY() < 255 && ctx.getWorld().getBlockState(blockPos.up()).canReplace(ctx)) {
			var world = ctx.getWorld();
			var bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
			return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing()).with(POWERED, bl).with(HALF, DoubleBlockHalf.LOWER).with(Properties.WATERLOGGED, world.getFluidState(blockPos).isEqualAndStill(Fluids.WATER));
		} else {
			return null;
		}
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), 3);
	}
	
	public boolean isPowered(BlockState state) {
		return state.get(POWERED);
	}
	
	public void setOpen(World world, BlockState state, BlockPos blockPos, boolean open, boolean notify) {
		if (state.isOf(this) && isPowered(state) != open) {
			byte flags = Block.REDRAW_ON_MAIN_THREAD;
			if (notify) {
				flags |= Block.NOTIFY_LISTENERS;
			}
			world.setBlockState(blockPos, state.with(POWERED, open), flags);
			this.playOpenCloseSound(world, blockPos, open);
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		var open = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.offset(state.get(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
		if (block != this) {
			setOpen(world, state, pos, open, notify);
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		var blockPos = pos.down();
		var blockState = world.getBlockState(blockPos);
		return state.get(HALF) == DoubleBlockHalf.LOWER ? blockState.isSideSolidFullSquare(world, blockPos, Direction.UP) : blockState.isOf(this);
	}
	
	private void playOpenCloseSound(World world, BlockPos pos, boolean open) {
		world.emitGameEvent(null, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
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
