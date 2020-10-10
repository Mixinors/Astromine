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
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
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

import com.github.chainmailstudios.astromine.common.utilities.MovementUtilities;
import com.github.chainmailstudios.astromine.common.utilities.capability.block.FacingBlockWrenchable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

public class ConveyorBlock extends HorizontalFacingBlock implements BlockEntityProvider, Conveyor, FacingBlockWrenchable, Waterloggable {
	private int speed;

	public ConveyorBlock(Settings settings, int speed) {
		super(settings);

		this.speed = speed;
		setDefaultState(getDefaultState().with(ConveyorProperties.LEFT, false).with(ConveyorProperties.RIGHT, false).with(ConveyorProperties.BACK, false).with(ConveyorProperties.UP, false).with(Properties.WATERLOGGED, false));
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
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ConveyorBlockEntity();
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
	public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
		BlockPos pos = new BlockPos(entity.getPos());

		if (!entity.isOnGround() || (entity.getY() - blockPos.getY()) != (4F / 16F))
			return;

		if (entity instanceof PlayerEntity && entity.isSneaking())
			return;

		Direction direction = blockState.get(FACING);

		if (entity instanceof ItemEntity && pos.equals(blockPos) && world.getBlockEntity(blockPos) instanceof ConveyorBlockEntity) {
			ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

			if (blockEntity.isEmpty()) {
				blockEntity.setStack(((ItemEntity) entity).getStack());
				entity.remove();
			}
		} else if (!(entity instanceof ItemEntity)) {
			MovementUtilities.pushEntity(entity, blockPos, 2.0F / getSpeed(), direction);
		}
	}

	@Override
	public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		updateDiagonals(world, this, blockPos);
	}

	@Override
	public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		if (blockState.getBlock() != blockState2.getBlock()) {
			BlockEntity blockEntity_1 = world.getBlockEntity(blockPos);
			if (blockEntity_1 instanceof ConveyorBlockEntity) {
				((ConveyorBlockEntity) blockEntity_1).setRemoved(true);
				ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((ConveyorBlockEntity) blockEntity_1).getStack());
				world.updateComparators(blockPos, this);
			}

			super.onStateReplaced(blockState, world, blockPos, blockState2, boolean_1);
		}

		updateDiagonals(world, this, blockPos);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState blockState, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
		BlockState newState = blockState;
		Direction direction = newState.get(FACING);
		boolean setBack = false;
		boolean backExists = false;

		BlockPos leftPos = blockPos.offset(direction.rotateYCounterclockwise());
		BlockPos rightPos = blockPos.offset(direction.rotateYClockwise());
		BlockPos backPos = blockPos.offset(direction.getOpposite());
		BlockPos upPos = blockPos.up();

		BlockEntity leftBlockEntity = world.getBlockEntity(leftPos);
		BlockEntity leftDownBlockEntity = world.getBlockEntity(leftPos.down());
		if (leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).isOutputSide(direction.rotateYClockwise(), getType())) {
			newState = newState.with(ConveyorProperties.LEFT, true);
			if (backExists) {
				newState = newState.with(ConveyorProperties.BACK, false);
				setBack = true;
			}
		} else if (leftDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) leftDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) leftDownBlockEntity).isOutputSide(direction.rotateYClockwise(), getType())) {
			newState = newState.with(ConveyorProperties.LEFT, true);
			if (backExists) {
				newState = newState.with(ConveyorProperties.BACK, false);
				setBack = true;
			}
		} else {
			newState = newState.with(ConveyorProperties.LEFT, false);
			newState = newState.with(ConveyorProperties.BACK, true);
		}

		BlockEntity rightBlockEntity = world.getBlockEntity(rightPos);
		BlockEntity rightDownBlockEntity = world.getBlockEntity(rightPos.down());
		if (rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).isOutputSide(direction.rotateYCounterclockwise(), getType())) {
			newState = newState.with(ConveyorProperties.RIGHT, true);
			if (backExists) {
				newState = newState.with(ConveyorProperties.BACK, false);
			}
		} else if (rightDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) rightDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) rightDownBlockEntity).isOutputSide(direction.rotateYCounterclockwise(), getType())) {
			newState = newState.with(ConveyorProperties.RIGHT, true);
			if (backExists) {
				newState = newState.with(ConveyorProperties.BACK, false);
			}
		} else {
			newState = newState.with(ConveyorProperties.RIGHT, false);
			if (!setBack) {
				newState = newState.with(ConveyorProperties.BACK, true);
			}
		}

		BlockEntity backBlockEntity = world.getBlockEntity(backPos);
		BlockEntity backDownBlockEntity = world.getBlockEntity(backPos.down());
		if (backBlockEntity instanceof Conveyable && ((Conveyable) backBlockEntity).isOutputSide(direction, getType())) {
			newState = newState.with(ConveyorProperties.BACK, false);
		} else if (backDownBlockEntity instanceof ConveyorConveyable && !((ConveyorConveyable) backDownBlockEntity).hasBeenRemoved() && ((ConveyorConveyable) backDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) backDownBlockEntity)
			.isOutputSide(direction, getType())) {
				newState = newState.with(ConveyorProperties.BACK, false);
			} else if (newState.get(ConveyorProperties.LEFT) || newState.get(ConveyorProperties.RIGHT)) {
				newState = newState.with(ConveyorProperties.BACK, true);
			} else {
				newState = newState.with(ConveyorProperties.BACK, false);
			}

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);
		if (upBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) upBlockEntity).getConveyorType() == ConveyorTypes.NORMAL)
			newState = newState.with(ConveyorProperties.UP, true);
		else newState = newState.with(ConveyorProperties.UP, false);

		return newState;
	}

	@Override
	public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1) {
		Direction direction = blockState.get(FACING);
		ConveyorBlockEntity conveyorBlockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

		BlockPos frontPos = blockPos.offset(direction);

		BlockEntity frontBlockEntity = world.getBlockEntity(blockPos.offset(direction));
		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).validInputSide(direction.getOpposite()))
			conveyorBlockEntity.setFront(true);
		else conveyorBlockEntity.setFront(false);

		BlockEntity frontAcrossBlockEntity = world.getBlockEntity(blockPos.offset(direction).offset(direction));
		if (frontBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) frontBlockEntity).validInputSide(direction.getOpposite()) && ((ConveyorConveyable) frontBlockEntity).validInputSide(direction) && frontAcrossBlockEntity instanceof ConveyorConveyable && world
			.getBlockState(blockPos.offset(direction).offset(direction)).get(HorizontalFacingBlock.FACING) == direction.getOpposite())
			conveyorBlockEntity.setAcross(true);
		else conveyorBlockEntity.setAcross(false);

		BlockEntity downBlockEntity = world.getBlockEntity(blockPos.offset(direction).down());
		if (downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).validInputSide(Direction.UP))
			conveyorBlockEntity.setDown(true);
		else conveyorBlockEntity.setDown(false);

		if (blockPos2.getY() < blockPos.getY()) {
			BlockState newState = blockState.getStateForNeighborUpdate(direction, blockState, world, blockPos, blockPos);
			world.setBlockState(blockPos, newState, 1 | 2);
		}
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorProperties.LEFT, ConveyorProperties.RIGHT, ConveyorProperties.BACK, ConveyorProperties.UP, Properties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState newState = this.getDefaultState().with(FACING, context.getPlayer().isSneaking() ? context.getPlayerFacing().getOpposite() : context.getPlayerFacing()).with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() ==
			Blocks.WATER);

		newState = newState.getStateForNeighborUpdate(null, newState, world, blockPos, blockPos);

		return newState;
	}

	@Override
	public boolean isTranslucent(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
		return false;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext entityContext) {
		VoxelShape conveyor = VoxelShapes.cuboid(0, 0, 0, 1, (4F / 16F), 1);
		if (blockState.get(ConveyorProperties.UP)) {
			return VoxelShapes.fullCube();
		}
		return conveyor;
	}
}
