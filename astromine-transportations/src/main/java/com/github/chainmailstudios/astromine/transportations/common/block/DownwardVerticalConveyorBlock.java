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

import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.DownVerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DownwardVerticalConveyorBlock extends VerticalConveyorBlock {
	public DownwardVerticalConveyorBlock(Properties settings, int speed) {
		super(settings, speed);

		registerDefaultState(defaultBlockState().setValue(ConveyorProperties.FRONT, false).setValue(ConveyorProperties.CONVEYOR, false));
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.DOWN_VERTICAL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter blockView) {
		return new DownVerticalConveyorBlockEntity();
	}

	@Override
	public void onPlace(BlockState blockState, Level world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		updateDiagonals(world, this, blockPos.above());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction fromDirection, BlockState fromState, LevelAccessor world, BlockPos blockPos, BlockPos fromPos) {
		Direction direction = state.getValue(FACING);

		BlockPos frontPos = blockPos.relative(direction.getOpposite());
		BlockPos conveyorPos = blockPos.relative(direction).above();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);

		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canInsert(direction)) {
			state = state.setValue(ConveyorProperties.FRONT, true);
		} else {
			state = state.setValue(ConveyorProperties.FRONT, false);
		}

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		if (world.isEmptyBlock(blockPos.above()) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canExtract(direction.getOpposite(), getType())) {
			state = state.setValue(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.setValue(ConveyorProperties.CONVEYOR, false);
		}

		return state;
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction direction = state.getValue(FACING);
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockPos downPos = pos.below(1);
		BlockPos conveyorPos = pos.relative(direction).above();

		BlockEntity downBlockEntity = world.getBlockEntity(downPos);

		blockEntity.setDown(downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).canInsert(Direction.UP));

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		checkForConveyor(world, state, conveyorBlockEntity, direction, pos, pos.above());
	}

	@Override
	public void checkForConveyor(Level world, BlockState state, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		if (world.isEmptyBlock(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canExtract(direction.getOpposite(), getType())) {
			state = state.setValue(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.setValue(ConveyorProperties.CONVEYOR, false);
		}

		world.setBlock(pos, state, 8);
	}
}
