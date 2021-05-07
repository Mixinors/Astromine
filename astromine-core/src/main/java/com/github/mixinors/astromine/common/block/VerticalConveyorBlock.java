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

import com.github.mixinors.astromine.common.utilities.VoxelShapeUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
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

import com.github.mixinors.astromine.common.block.entity.ConveyorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.VerticalConveyorBlockEntity;
import com.github.mixinors.astromine.common.block.property.ConveyorProperties;
import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.Conveyor;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

public class VerticalConveyorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Conveyor, Waterloggable {
	private static final VoxelShape FIRST_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, 1, (4F / 16F));
	private static final VoxelShape SECOND_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, (4F / 16F), 1);

	private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[12];

	private final int speed;

	public VerticalConveyorBlock(Settings settings, int speed) {
		super(settings);

		this.speed = speed;

		setDefaultState(getDefaultState().with(ConveyorProperties.FRONT, false).with(ConveyorProperties.CONVEYOR, false).with(Properties.WATERLOGGED, false));
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.VERTICAL;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new VerticalConveyorBlockEntity();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

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
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		Direction direction = state.get(FACING);

		world.updateNeighbor(pos.offset(direction).up(), this, pos);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		Direction direction = state.get(FACING);

		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof VerticalConveyorBlockEntity) {
				blockEntity.markRemoved();

				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((VerticalConveyorBlockEntity) blockEntity).getItemComponent().getFirst());

				world.updateComparators(pos, this);
			}

			world.updateNeighbor(pos.offset(direction).up(), this, pos);

			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		Direction facing = state.get(FACING);

		BlockPos frontPos = pos.offset(facing.getOpposite());
		BlockPos upPos = pos.up();
		BlockPos conveyorPos = pos.offset(facing).up();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);

		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canExtract(facing, getType())) {
			state = state.with(ConveyorProperties.FRONT, true);
		} else {
			state = state.with(ConveyorProperties.FRONT, false);
		}

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canInsert(facing.getOpposite())) {
			state = state.with(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.with(ConveyorProperties.CONVEYOR, false);
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction facing = state.get(FACING);

		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockPos upPos = pos.up();
		BlockPos conveyorPos = pos.offset(facing).up();

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);

		((VerticalConveyorBlockEntity) blockEntity).setUp(upBlockEntity instanceof Conveyable && ((Conveyable) upBlockEntity).canInsert(Direction.DOWN));

		if (fromPos.getY() > pos.getY()) {
			BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

			checkForConveyor(world, state, conveyorBlockEntity, facing, pos, upPos);
		}
	}

	public void checkForConveyor(World world, BlockState state, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		BlockState newState = state;

		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canInsert(direction.getOpposite())) {
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		} else {
			newState = newState.with(ConveyorProperties.CONVEYOR, false);
		}

		world.setBlockState(pos, newState, 8);
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorProperties.FRONT, ConveyorProperties.CONVEYOR, Properties.WATERLOGGED);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();

		BlockPos blockPos = context.getBlockPos();

		BlockState state = this.getDefaultState().with(FACING, context.getPlayerFacing());

		state = state.getStateForNeighborUpdate(null, state, world, blockPos, blockPos);

		return state.with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Direction facing = state.get(FACING);

		int id = facing.getId() + (state.get(ConveyorProperties.FRONT) ? 6 : 0);

		if (SHAPE_CACHE[id] == null) {
			VoxelShape firstShape = VoxelShapeUtilities.rotate(facing, FIRST_SHAPE);
			VoxelShape secondShape = VoxelShapeUtilities.rotate(facing, SECOND_SHAPE);

			if (state.get(ConveyorProperties.FRONT)) {
				SHAPE_CACHE[id] = VoxelShapes.union(firstShape, secondShape);
			} else {
				SHAPE_CACHE[id] = firstShape;
			}
		}

		return SHAPE_CACHE[id];
	}
}
