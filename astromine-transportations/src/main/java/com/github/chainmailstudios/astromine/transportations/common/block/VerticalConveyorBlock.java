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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.common.utilities.RotationUtilities;
import com.github.chainmailstudios.astromine.common.utilities.capability.block.FacingBlockWrenchable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.VerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

public class VerticalConveyorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Conveyor, FacingBlockWrenchable, Waterloggable {
	private int speed;

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
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

		if (!playerEntity.getStackInHand(hand).isEmpty() && Block.getBlockFromItem(playerEntity.getStackInHand(hand).getItem()) instanceof Conveyor) {
			return ActionResult.PASS;
		} else if (!playerEntity.getStackInHand(hand).isEmpty() && blockEntity.isEmpty()) {
			blockEntity.setStack(playerEntity.getStackInHand(hand));
			playerEntity.setStackInHand(hand, ItemStack.EMPTY);

			return ActionResult.SUCCESS;
		} else if (!blockEntity.isEmpty()) {
			playerEntity.inventory.offerOrDrop(world, blockEntity.getStack());
			blockEntity.removeStack();

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		Direction direction = blockState.get(FACING);

		world.updateNeighbor(blockPos.offset(direction).up(), this, blockPos);
	}

	@Override
	public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		Direction direction = blockState.get(FACING);
		if (blockState.getBlock() != blockState2.getBlock()) {
			BlockEntity blockEntity_1 = world.getBlockEntity(blockPos);
			if (blockEntity_1 instanceof VerticalConveyorBlockEntity) {
				((VerticalConveyorBlockEntity) blockEntity_1).setRemoved(true);
				ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((VerticalConveyorBlockEntity) blockEntity_1).getStack());
				world.updateComparators(blockPos, this);
			}

			world.updateNeighbor(blockPos.offset(direction).up(), this, blockPos);
			super.onStateReplaced(blockState, world, blockPos, blockState2, boolean_1);
		}
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState blockState, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
		BlockState newState = blockState;
		Direction direction = newState.get(FACING);

		BlockPos frontPos = blockPos.offset(direction.getOpposite());
		BlockPos upPos = blockPos.up();
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);
		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).isOutputSide(direction, getType())) {
			newState = newState.with(ConveyorProperties.FRONT, true);
		} else newState = newState.with(ConveyorProperties.FRONT, false);

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).validInputSide(direction.getOpposite()))
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		else newState = newState.with(ConveyorProperties.CONVEYOR, false);

		return newState;
	}

	@Override
	public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1) {
		Direction direction = blockState.get(FACING);
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

		BlockPos upPos = blockPos.up();
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);
		if (upBlockEntity instanceof Conveyable && ((Conveyable) upBlockEntity).validInputSide(Direction.DOWN))
			((VerticalConveyorBlockEntity) blockEntity).setUp(true);
		else((VerticalConveyorBlockEntity) blockEntity).setUp(false);

		if (blockPos2.getY() > blockPos.getY()) {
			BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
			checkForConveyor(world, blockState, conveyorBlockEntity, direction, blockPos, upPos);
		}
	}

	public void checkForConveyor(World world, BlockState blockState, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		BlockState newState = blockState;

		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).validInputSide(direction.getOpposite())) {
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		} else {
			newState = newState.with(ConveyorProperties.CONVEYOR, false);
		}

		world.setBlockState(pos, newState, 8);
	}

	@Override
	public boolean hasComparatorOutput(BlockState blockState) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World world, BlockPos blockPos) {
		return ((ConveyorBlockEntity) world.getBlockEntity(blockPos)).isEmpty() ? 0 : 15;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorProperties.FRONT, ConveyorProperties.CONVEYOR, Properties.WATERLOGGED);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState newState = this.getDefaultState().with(FACING, context.getPlayerFacing());

		newState = newState.getStateForNeighborUpdate(null, newState, world, blockPos, blockPos);

		return newState.with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
		VoxelShape box1 = RotationUtilities.getRotatedShape(new Box(0, 0, 0, 1, 1, (4F / 16F)), blockState.get(FACING));
		VoxelShape box2 = RotationUtilities.getRotatedShape(new Box(0, 0, 0, 1, (4F / 16F), 1), blockState.get(FACING));

		if (blockState.get(ConveyorProperties.FRONT)) {
			return VoxelShapes.union(box1, box2);
		} else {
			return box1;
		}
	}
}
