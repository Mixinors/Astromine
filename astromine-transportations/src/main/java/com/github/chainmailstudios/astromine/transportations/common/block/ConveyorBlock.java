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

import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.MovementUtilities;
import com.github.chainmailstudios.astromine.common.utilities.capability.block.FacingBlockWrenchable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
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
import org.jetbrains.annotations.Nullable;

public class ConveyorBlock extends HorizontalDirectionalBlock implements EntityBlock, Conveyor, FacingBlockWrenchable, SimpleWaterloggedBlock {
	private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, (4F / 16F), 1);

	private final int speed;

	public ConveyorBlock(Properties settings, int speed) {
		super(settings);

		this.speed = speed;
		
		registerDefaultState(defaultBlockState().setValue(ConveyorProperties.LEFT, false).setValue(ConveyorProperties.RIGHT, false).setValue(ConveyorProperties.BACK, false).setValue(ConveyorProperties.UP, false).setValue(BlockStateProperties.WATERLOGGED, false));
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
	public @Nullable BlockEntity newBlockEntity(BlockGetter world) {
		return new ConveyorBlockEntity();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((ConveyorBlockEntity) world.getBlockEntity(pos)).getItemComponent().isEmpty() ? 0 : 15;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!entity.isOnGround() || (entity.getY() - pos.getY()) != (4F / 16F)) {
			return;
		}

		if (entity instanceof Player && entity.isShiftKeyDown()) {
			return;
		}

		Direction direction = state.getValue(FACING);

		if (entity instanceof ItemEntity && pos.equals(pos) && world.getBlockEntity(pos) instanceof ConveyorBlockEntity) {
			ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);
			
			if (blockEntity.getItemComponent().isEmpty()) {
				blockEntity.getItemComponent().setFirst(((ItemEntity) entity).getItem());

				entity.remove();
			}
		} else if (!(entity instanceof ItemEntity)) {
			MovementUtilities.pushEntity(entity, pos, 2.0F / getSpeed(), direction);
		}
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		updateDiagonals(world, this, pos);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof ConveyorBlockEntity) {
				blockEntity.setRemoved();

				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((ConveyorBlockEntity) blockEntity).getItemComponent().getFirst());

				world.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}
	

	@Override
	public BlockState updateShape(BlockState state, Direction fromDirection, BlockState fromState, LevelAccessor world, BlockPos blockPos, BlockPos fromPos) {
		Direction direction = state.getValue(FACING);

		boolean setBack = false;

		boolean backExists = false;

		BlockPos leftPos = blockPos.relative(direction.getCounterClockWise());
		BlockPos rightPos = blockPos.relative(direction.getClockWise());
		BlockPos backPos = blockPos.relative(direction.getOpposite());
		BlockPos upPos = blockPos.above();

		BlockEntity leftBlockEntity = world.getBlockEntity(leftPos);
		BlockEntity leftDownBlockEntity = world.getBlockEntity(leftPos.below());

		if (leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).canExtract(direction.getClockWise(), getType())) {
			state = state.setValue(ConveyorProperties.LEFT, true);

			if (backExists) {
				state = state.setValue(ConveyorProperties.BACK, false);

				setBack = true;
			}
		} else if (leftDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) leftDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) leftDownBlockEntity).canExtract(direction.getClockWise(), getType())) {
			state = state.setValue(ConveyorProperties.LEFT, true);

			if (backExists) {
				state = state.setValue(ConveyorProperties.BACK, false);

				setBack = true;
			}
		} else {
			state = state.setValue(ConveyorProperties.LEFT, false);
			state = state.setValue(ConveyorProperties.BACK, true);
		}

		BlockEntity rightBlockEntity = world.getBlockEntity(rightPos);
		BlockEntity rightDownBlockEntity = world.getBlockEntity(rightPos.below());

		if (rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).canExtract(direction.getCounterClockWise(), getType())) {
			state = state.setValue(ConveyorProperties.RIGHT, true);

			if (backExists) {
				state = state.setValue(ConveyorProperties.BACK, false);
			}
		} else if (rightDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) rightDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) rightDownBlockEntity).canExtract(direction.getCounterClockWise(), getType())) {
			state = state.setValue(ConveyorProperties.RIGHT, true);

			if (backExists) {
				state = state.setValue(ConveyorProperties.BACK, false);
			}
		} else {
			state = state.setValue(ConveyorProperties.RIGHT, false);

			if (!setBack) {
				state = state.setValue(ConveyorProperties.BACK, true);
			}
		}

		BlockEntity backBlockEntity = world.getBlockEntity(backPos);
		BlockEntity backDownBlockEntity = world.getBlockEntity(backPos.below());

		if (backBlockEntity instanceof Conveyable && ((Conveyable) backBlockEntity).canExtract(direction, getType())) {
			state = state.setValue(ConveyorProperties.BACK, false);
		} else if (backDownBlockEntity instanceof ConveyorConveyable && !backDownBlockEntity.isRemoved() && ((ConveyorConveyable) backDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) backDownBlockEntity).canExtract(direction, getType())) {
			state = state.setValue(ConveyorProperties.BACK, false);
		} else if (state.getValue(ConveyorProperties.LEFT) || state.getValue(ConveyorProperties.RIGHT)) {
			state = state.setValue(ConveyorProperties.BACK, true);
		} else {
			state = state.setValue(ConveyorProperties.BACK, false);
		}

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);

		if (upBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) upBlockEntity).getConveyorType() == ConveyorTypes.NORMAL) {
			state = state.setValue(ConveyorProperties.UP, true);
		} else {
			state = state.setValue(ConveyorProperties.UP, false);
		}

		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction direction = state.getValue(FACING);

		ConveyorBlockEntity conveyorBlockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockEntity frontBlockEntity = world.getBlockEntity(pos.relative(direction));

		conveyorBlockEntity.setFront(frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canInsert(direction.getOpposite()));

		BlockEntity frontAcrossBlockEntity = world.getBlockEntity(pos.relative(direction).relative(direction));

		conveyorBlockEntity.setAcross(frontBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) frontBlockEntity).canInsert(direction.getOpposite()) && ((ConveyorConveyable) frontBlockEntity).canInsert(direction) && frontAcrossBlockEntity instanceof ConveyorConveyable && world.getBlockState(pos.relative(direction).relative(direction)).getValue(HorizontalDirectionalBlock.FACING) == direction.getOpposite());

		BlockEntity downBlockEntity = world.getBlockEntity(pos.relative(direction).below());

		conveyorBlockEntity.setDown(downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).canInsert(Direction.UP));

		if (fromPos.getY() < pos.getY()) {
			BlockState newState = state.updateShape(direction, state, world, pos, pos);

			world.setBlock(pos, newState, 1 | 2);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		if (!player.getItemInHand(hand).isEmpty() && Block.byItem(player.getItemInHand(hand).getItem()) instanceof Conveyor) {
			return InteractionResult.PASS;
		} else if (!player.getItemInHand(hand).isEmpty() && blockEntity.getItemComponent().isEmpty()) {
			blockEntity.getItemComponent().setFirst(player.getItemInHand(hand));

			player.setItemInHand(hand, ItemStack.EMPTY);

			return InteractionResult.SUCCESS;
		} else if (!blockEntity.getItemComponent().isEmpty()) {
			player.inventory.placeItemBackInInventory(world, blockEntity.getItemComponent().getFirst());

			blockEntity.getItemComponent().setFirst(ItemStack.EMPTY);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorProperties.LEFT, ConveyorProperties.RIGHT, ConveyorProperties.BACK, ConveyorProperties.UP, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) ? Fluids.WATER.defaultFluidState() : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();

		BlockPos blockPos = context.getClickedPos();

		BlockState state = this.defaultBlockState().setValue(FACING, context.getPlayer().isShiftKeyDown() ? context.getHorizontalDirection().getOpposite() : context.getHorizontalDirection()).setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getBlockState(context.getClickedPos()).getBlock() == Blocks.WATER);

		return state.updateShape(null, state, world, blockPos, blockPos);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockView, BlockPos blockPos, CollisionContext entityContext) {
		if (state.getValue(ConveyorProperties.UP)) {
			return Shapes.block();
		}

		return SHAPE;
	}
}
