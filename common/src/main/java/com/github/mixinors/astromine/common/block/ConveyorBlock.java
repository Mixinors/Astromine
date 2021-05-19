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

import com.github.mixinors.astromine.common.util.MovementUtils;
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

		var direction = state.get(FACING);

		if (entity instanceof ItemEntity && pos.equals(pos) && world.getBlockEntity(pos) instanceof ConveyorBlockEntity) {
			ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);
			
			if (blockEntity.isEmpty()) {
				blockEntity.getItemComponent().setFirst(((ItemEntity) entity).getStack());

				entity.remove();
			}
		} else if (!(entity instanceof ItemEntity)) {
			MovementUtils.pushEntity(entity, pos, 2.0F / getSpeed(), direction);
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		updateDiagonals(world, this, pos);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);

			if (blockEntity instanceof ConveyorBlockEntity) {
				blockEntity.markRemoved();

				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), ((ConveyorBlockEntity) blockEntity).getItemComponent().getFirst());

				world.updateComparators(pos, this);
			}

			super.onStateReplaced(state, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}
	

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
		var direction = state.get(FACING);

		boolean setBack = false;

		boolean backExists = false;

		var leftPos = blockPos.offset(direction.rotateYCounterclockwise());
		var rightPos = blockPos.offset(direction.rotateYClockwise());
		BlockPos backPos = blockPos.offset(direction.getOpposite());
		BlockPos upPos = blockPos.up();

		BlockEntity leftBlockEntity = world.getBlockEntity(leftPos);
		BlockEntity leftDownBlockEntity = world.getBlockEntity(leftPos.down());

		if (leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).canExtract(direction.rotateYClockwise(), getType())) {
			state = state.with(ConveyorBlock.LEFT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);

				setBack = true;
			}
		} else if (leftDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) leftDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) leftDownBlockEntity).canExtract(direction.rotateYClockwise(), getType())) {
			state = state.with(ConveyorBlock.LEFT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);

				setBack = true;
			}
		} else {
			state = state.with(ConveyorBlock.LEFT, false);
			state = state.with(ConveyorBlock.BACK, true);
		}

		BlockEntity rightBlockEntity = world.getBlockEntity(rightPos);
		BlockEntity rightDownBlockEntity = world.getBlockEntity(rightPos.down());

		if (rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).canExtract(direction.rotateYCounterclockwise(), getType())) {
			state = state.with(ConveyorBlock.RIGHT, true);

			if (backExists) {
				state = state.with(ConveyorBlock.BACK, false);
			}
		} else if (rightDownBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) rightDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) rightDownBlockEntity).canExtract(direction.rotateYCounterclockwise(), getType())) {
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

		BlockEntity backBlockEntity = world.getBlockEntity(backPos);
		BlockEntity backDownBlockEntity = world.getBlockEntity(backPos.down());

		if (backBlockEntity instanceof Conveyable && ((Conveyable) backBlockEntity).canExtract(direction, getType())) {
			state = state.with(ConveyorBlock.BACK, false);
		} else if (backDownBlockEntity instanceof ConveyorConveyable && !backDownBlockEntity.isRemoved() && ((ConveyorConveyable) backDownBlockEntity).getConveyorType() == ConveyorTypes.VERTICAL && ((ConveyorConveyable) backDownBlockEntity).canExtract(direction, getType())) {
			state = state.with(ConveyorBlock.BACK, false);
		} else if (state.get(ConveyorBlock.LEFT) || state.get(ConveyorBlock.RIGHT)) {
			state = state.with(ConveyorBlock.BACK, true);
		} else {
			state = state.with(ConveyorBlock.BACK, false);
		}

		BlockEntity upBlockEntity = world.getBlockEntity(upPos);

		if (upBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) upBlockEntity).getConveyorType() == ConveyorTypes.NORMAL) {
			state = state.with(ConveyorBlock.UP, true);
		} else {
			state = state.with(ConveyorBlock.UP, false);
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		var direction = state.get(FACING);

		ConveyorBlockEntity conveyorBlockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockEntity frontBlockEntity = world.getBlockEntity(pos.offset(direction));

		conveyorBlockEntity.setFront(frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canInsert(direction.getOpposite()));

		BlockEntity frontAcrossBlockEntity = world.getBlockEntity(pos.offset(direction).offset(direction));

		conveyorBlockEntity.setAcross(frontBlockEntity instanceof ConveyorConveyable && ((ConveyorConveyable) frontBlockEntity).canInsert(direction.getOpposite()) && ((ConveyorConveyable) frontBlockEntity).canInsert(direction) && frontAcrossBlockEntity instanceof ConveyorConveyable && world.getBlockState(pos.offset(direction).offset(direction)).get(HorizontalFacingBlock.FACING) == direction.getOpposite());

		BlockEntity downBlockEntity = world.getBlockEntity(pos.offset(direction).down());

		conveyorBlockEntity.setDown(downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).canInsert(Direction.UP));

		if (fromPos.getY() < pos.getY()) {
			BlockState newState = state.getStateForNeighborUpdate(direction, state, world, pos, pos);

			world.setBlockState(pos, newState, 1 | 2);
		}
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
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING, ConveyorBlock.LEFT, ConveyorBlock.RIGHT, ConveyorBlock.BACK, ConveyorBlock.UP, Properties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) ? Fluids.WATER.getDefaultState() : super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();

		BlockPos blockPos = context.getBlockPos();

		BlockState state = this.getDefaultState().with(FACING, context.getPlayer().isSneaking() ? context.getPlayerFacing().getOpposite() : context.getPlayerFacing()).with(Properties.WATERLOGGED, context.getWorld().getBlockState(context.getBlockPos()).getBlock() == Blocks.WATER);

		return state.getStateForNeighborUpdate(null, state, world, blockPos, blockPos);
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
