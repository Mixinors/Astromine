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

import com.github.chainmailstudios.astromine.common.utilities.capability.block.FacingBlockWrenchable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.AlternatorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.base.AbstractConveyableBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class AlternatorBlock extends HorizontalDirectionalBlock implements EntityBlock, ConveyableBlock, FacingBlockWrenchable {
	public AlternatorBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter blockView) {
		return new AlternatorBlockEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManagerBuilder) {
		stateManagerBuilder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
		return this.defaultBlockState().setValue(FACING, itemPlacementContext.getPlayer().isShiftKeyDown() ? itemPlacementContext.getHorizontalDirection().getOpposite() : itemPlacementContext.getHorizontalDirection());
	}

	@Override
	public void onPlace(BlockState blockState, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		updateDiagonals(world, this, pos);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			
			if (blockEntity instanceof AbstractConveyableBlockEntity) {
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((AbstractConveyableBlockEntity) blockEntity).getItemComponent().getFirst());
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), ((AbstractConveyableBlockEntity) blockEntity).getItemComponent().getSecond());

				blockEntity.setRemoved();
			}

			super.onRemove(state, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction direction = state.getValue(FACING);

		AbstractConveyableBlockEntity machineBlockEntity = (AbstractConveyableBlockEntity) world.getBlockEntity(pos);

		BlockPos leftPos = pos.relative(direction.getCounterClockWise());
		BlockPos rightPos = pos.relative(direction.getClockWise());

		BlockEntity leftBlockEntity = world.getBlockEntity(leftPos);

		machineBlockEntity.setLeft(leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).canInsert(direction.getClockWise()));

		BlockEntity rightBlockEntity = world.getBlockEntity(rightPos);

		machineBlockEntity.setRight(rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).canInsert(direction.getCounterClockWise()));
	}
}
