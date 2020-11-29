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
import com.github.chainmailstudios.astromine.common.utilities.capability.block.FacingBlockWrenchable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.VerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VerticalConveyorBlock extends HorizontalDirectionalBlock implements EntityBlock, Conveyor, FacingBlockWrenchable, SimpleWaterloggedBlock {
	private static final VoxelShape FIRST_SHAPE = Shapes.box(0, 0, 0, 1, 1, (4F / 16F));
	private static final VoxelShape SECOND_SHAPE = Shapes.box(0, 0, 0, 1, (4F / 16F), 1);

	private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[12];

	private final int speed;

	public VerticalConveyorBlock(Properties settings, int speed) {
		super(settings);

		this.speed = speed;

		registerDefaultState(defaultBlockState().setValue(ConveyorProperties.FRONT, false).setValue(ConveyorProperties.CONVEYOR, false).setValue(BlockStateProperties.WATERLOGGED, false));
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
	public BlockEntity newBlockEntity(BlockGetter blockView) {
		return new VerticalConveyorBlockEntity();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		if (!player.getItemInHand(hand).isEmpty() && Block.byItem(player.getItemInHand(hand).getItem()) instanceof Conveyor) {
			return InteractionResult.PASS;
		} else if (!player.getItemInHand(hand).isEmpty() && blockEntity.isEmpty()) {
			blockEntity.getItemComponent().setFirst(player.getItemInHand(hand));

			player.setItemInHand(hand, ItemStack.EMPTY);

			return InteractionResult.SUCCESS;
		} else if (!blockEntity.isEmpty()) {
			player.inventory.placeItemBackInInventory(world, blockEntity.getItemComponent().getFirst());

			blockEntity.getItemComponent().setFirst(ItemStack.EMPTY);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		Direction direction = state.getValue(FACING);

		world.neighborChanged(pos.relative(direction).above(), this, pos);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		Direction direction = state.getValue(FACING);

		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof VerticalConveyorBlockEntity) {
				blockEntity.setRemoved();

				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((VerticalConveyorBlockEntity) blockEntity).getItemComponent().getFirst());

				world.updateNeighbourForOutputSignal(pos, this);
			}

			world.neighborChanged(pos.relative(direction).above(), this, pos);

			super.onRemove(state, world, pos, newState, moved);
		}
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		Direction facing = state.getValue(FACING);

		BlockPos frontPos = pos.relative(facing.getOpposite());
		BlockPos upPos = pos.above();
		BlockPos conveyorPos = pos.relative(facing).above();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);

		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canExtract(facing, getType())) {
			state = state.setValue(ConveyorProperties.FRONT, true);
		} else {
			state = state.setValue(ConveyorProperties.FRONT, false);
		}

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		if (world.isEmptyBlock(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canInsert(facing.getOpposite())) {
			state = state.setValue(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.setValue(ConveyorProperties.CONVEYOR, false);
		}

		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction facing = state.getValue(FACING);

		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockPos upPos = pos.above();
		BlockPos conveyorPos = pos.relative(facing).above();

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);

		((VerticalConveyorBlockEntity) blockEntity).setUp(upBlockEntity instanceof Conveyable && ((Conveyable) upBlockEntity).canInsert(Direction.DOWN));

		if (fromPos.getY() > pos.getY()) {
			BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

			checkForConveyor(world, state, conveyorBlockEntity, facing, pos, upPos);
		}
	}

	public void checkForConveyor(Level world, BlockState state, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		BlockState newState = state;

		if (world.isEmptyBlock(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canInsert(direction.getOpposite())) {
			newState = newState.setValue(ConveyorProperties.CONVEYOR, true);
		} else {
			newState = newState.setValue(ConveyorProperties.CONVEYOR, false);
		}

		world.setBlock(pos, newState, 8);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((ConveyorBlockEntity) world.getBlockEntity(pos)).isEmpty() ? 0 : 15;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorProperties.FRONT, ConveyorProperties.CONVEYOR, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();

		BlockPos blockPos = context.getClickedPos();

		BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());

		state = state.updateShape(null, state, world, blockPos, blockPos);

		return state.setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.WATER);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);

		int id = facing.get3DDataValue() + (state.getValue(ConveyorProperties.FRONT) ? 6 : 0);

		if (SHAPE_CACHE[id] == null) {
			VoxelShape firstShape = VoxelShapeUtilities.rotate(facing, FIRST_SHAPE);
			VoxelShape secondShape = VoxelShapeUtilities.rotate(facing, SECOND_SHAPE);

			if (state.getValue(ConveyorProperties.FRONT)) {
				SHAPE_CACHE[id] = Shapes.or(firstShape, secondShape);
			} else {
				SHAPE_CACHE[id] = firstShape;
			}
		}

		return SHAPE_CACHE[id];
	}
}
