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
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.github.mixinors.astromine.techreborn.common.util.MovementUtils;
import com.github.mixinors.astromine.common.block.entity.ConveyorBlockEntity;
import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.Conveyor;
import com.github.mixinors.astromine.common.conveyor.ConveyorConveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;
import org.jetbrains.annotations.Nullable;

public class ConveyorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Conveyor, Waterloggable {
	private static final VoxelShape SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, (4F / 16F), 1);
	
	public static final BooleanProperty FRONT = BooleanProperty.of("front");
	public static final BooleanProperty LEFT = BooleanProperty.of("left");
	public static final BooleanProperty RIGHT = BooleanProperty.of("right");
	public static final BooleanProperty BACK = BooleanProperty.of("back");
	public static final BooleanProperty UP = BooleanProperty.of("up");
	public static final BooleanProperty CONVEYOR = BooleanProperty.of("conveyor");
	public static final BooleanProperty NO_FLOOR = BooleanProperty.of("floor");

	private final int speed;

	public ConveyorBlock(Settings settings, int speed) {
		super(settings);

		this.speed = speed;
		
		setDefaultState(getDefaultState().with(ConveyorBlock.LEFT, false).with(ConveyorBlock.RIGHT, false).with(ConveyorBlock.BACK, false).with(ConveyorBlock.UP, false).with(Properties.WATERLOGGED, false));
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.NORMAL;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockView world) {
		return new ConveyorBlockEntity();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ((ConveyorBlockEntity) world.getBlockEntity(pos)).isEmpty() ? 0 : 15;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!entity.isOnGround() || (entity.getY() - pos.getY()) != (4F / 16F)) {
			return;
		}

		if (entity instanceof PlayerEntity && entity.isSneaking()) {
			return;
		}

		var facing = state.get(FACING);

		if (entity instanceof ItemEntity itemEntity && pos.equals(pos) && world.getBlockEntity(pos) instanceof ConveyorBlockEntity conveyorBlockEntity) {
			if (conveyorBlockEntity.isEmpty()) {
				conveyorBlockEntity.getItemComponent().setFirst(itemEntity.getStack());

				entity.remove();
			}
		} else if (!(entity instanceof ItemEntity)) {
			MovementUtils.pushEntity(entity, pos, 2.0F / getSpeed(), facing);
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

			if (blockEntity instanceof ConveyorBlockEntity) {
				blockEntity.markRemoved();

				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((ConveyorBlockEntity) blockEntity).getItemComponent().getFirst());

				world.updateComparators(pos, this);
			}

			super.onStateReplaced(oldState, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}
	

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess access, BlockPos pos, BlockPos neighborPos) {
		var facing = state.get(FACING);

		boolean setBack = false;

		boolean backExists = false;

		var leftPos = pos.offset(facing.rotateYCounterclockwise());
		var rightPos = pos.offset(facing.rotateYClockwise());
		var backPos = pos.offset(facing.getOpposite());
		var upPos = pos.up();

		var leftBlockEntity = access.getBlockEntity(leftPos);
		var leftDownBlockEntity = access.getBlockEntity(leftPos.down());

		if (leftBlockEntity instanceof Conveyable conveyable && conveyable.canExtract(facing.rotateYClockwise(), getType())) {
			state = state.with(ConveyorBlock.LEFT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);

				setBack = true;
			}
		} else if (leftDownBlockEntity instanceof ConveyorConveyable conveyable && conveyable.getConveyorType() == ConveyorTypes.VERTICAL && conveyable.canExtract(facing.rotateYClockwise(), getType())) {
			state = state.with(ConveyorBlock.LEFT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);

				setBack = true;
			}
		} else {
			state = state.with(ConveyorBlock.LEFT, false);
			state = state.with(ConveyorBlock.BACK, true);
		}

		var rightBlockEntity = access.getBlockEntity(rightPos);
		var rightDownBlockEntity = access.getBlockEntity(rightPos.down());

		if (rightBlockEntity instanceof Conveyable conveyable && conveyable.canExtract(facing.rotateYCounterclockwise(), getType())) {
			state = state.with(ConveyorBlock.RIGHT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);
			}
		} else if (rightDownBlockEntity instanceof ConveyorConveyable conveyable && conveyable.getConveyorType() == ConveyorTypes.VERTICAL && conveyable.canExtract(facing.rotateYCounterclockwise(), getType())) {
			state = state.with(ConveyorBlock.RIGHT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);
			}
		} else {
			state = state.with(ConveyorBlock.RIGHT, false);

			if (!setBack) {
				state = state.with(ConveyorBlock.BACK, true);
			}
		}

		var backBlockEntity = access.getBlockEntity(backPos);
		var backDownBlockEntity = access.getBlockEntity(backPos.down());

		if (backBlockEntity instanceof Conveyable conveyable && conveyable.canExtract(facing, getType())) {
			state = state.with(ConveyorBlock.BACK, false);
		} else if (backDownBlockEntity instanceof ConveyorConveyable conveyable && !backDownBlockEntity.isRemoved() && conveyable.getConveyorType() == ConveyorTypes.VERTICAL && conveyable.canExtract(facing, getType())) {
			state = state.with(ConveyorBlock.BACK, false);
		} else if (state.get(ConveyorBlock.LEFT) || state.get(ConveyorBlock.RIGHT)) {
			state = state.with(ConveyorBlock.BACK, true);
		} else {
			state = state.with(ConveyorBlock.BACK, false);
		}

		var upBlockEntity = access.getBlockEntity(upPos);

		if (upBlockEntity instanceof ConveyorConveyable conveyable && conveyable.getConveyorType() == ConveyorTypes.NORMAL) {
			state = state.with(ConveyorBlock.UP, true);
		} else {
			state = state.with(ConveyorBlock.UP, false);
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean notify) {
		var facing = state.get(FACING);

		var conveyorBlockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		var frontBlockEntity = world.getBlockEntity(pos.offset(facing));

		conveyorBlockEntity.setFront(frontBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(facing.getOpposite()));

		var frontAcrossBlockEntity = world.getBlockEntity(pos.offset(facing).offset(facing));

		conveyorBlockEntity.setAcross(frontBlockEntity instanceof ConveyorConveyable conveyable && conveyable.canInsert(facing.getOpposite()) && ((ConveyorConveyable) frontBlockEntity).canInsert(facing) && frontAcrossBlockEntity instanceof ConveyorConveyable && world.getBlockState(pos.offset(facing).offset(facing)).get(HorizontalFacingBlock.FACING) == facing.getOpposite());

		var downBlockEntity = world.getBlockEntity(pos.offset(facing).down());

		conveyorBlockEntity.setDown(downBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(Direction.UP));

		if (neighborPos.getY() < pos.getY()) {
			var newState = state.getStateForNeighborUpdate(facing, state, world, pos, pos);

			world.setBlockState(pos, newState, 1 | 2);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		var blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		if (!player.getStackInHand(hand).isEmpty() && Block.getBlockFromItem(player.getStackInHand(hand).getItem()) instanceof Conveyor) {
			return ActionResult.PASS;
		} else if (!player.getStackInHand(hand).isEmpty() && blockEntity.isEmpty()) {
			blockEntity.getItemComponent().setFirst(player.getStackInHand(hand));

			player.setStackInHand(hand, ItemStack.EMPTY);

			return ActionResult.SUCCESS;
		} else if (!blockEntity.isEmpty()) {
			player.inventory.offerOrDrop(world, blockEntity.getItemComponent().getFirst());

			blockEntity.getItemComponent().setFirst(ItemStack.EMPTY);

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ConveyorBlock.LEFT, ConveyorBlock.RIGHT, ConveyorBlock.BACK, ConveyorBlock.UP, Properties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		var world = ctx.getWorld();

		var pos = ctx.getBlockPos();

		var state = this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing()).with(Properties.WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);

		return state.getStateForNeighborUpdate(null, state, world, pos, pos);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
		if (state.get(ConveyorBlock.UP)) {
			return VoxelShapes.fullCube();
		}

		return SHAPE;
	}
}
