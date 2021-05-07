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
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.github.mixinors.astromine.common.block.entity.ConveyorBlockEntity;
import com.github.mixinors.astromine.common.block.entity.DownVerticalConveyorBlockEntity;
import com.github.mixinors.astromine.common.block.property.ConveyorProperties;
import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

public class DownwardVerticalConveyorBlock extends VerticalConveyorBlock {
	public DownwardVerticalConveyorBlock(Settings settings, int speed) {
		super(settings, speed);

		setDefaultState(getDefaultState().with(ConveyorProperties.FRONT, false).with(ConveyorProperties.CONVEYOR, false));
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.DOWN_VERTICAL;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new DownVerticalConveyorBlockEntity();
	}

	@Override
	public void onBlockAdded(BlockState blockState, World world, BlockPos blockPos, BlockState blockState2, boolean boolean_1) {
		updateDiagonals(world, this, blockPos.up());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
		Direction direction = state.get(FACING);

		BlockPos frontPos = blockPos.offset(direction.getOpposite());
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);

		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).canInsert(direction)) {
			state = state.with(ConveyorProperties.FRONT, true);
		} else {
			state = state.with(ConveyorProperties.FRONT, false);
		}

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		if (world.isAir(blockPos.up()) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canExtract(direction.getOpposite(), getType())) {
			state = state.with(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.with(ConveyorProperties.CONVEYOR, false);
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		Direction direction = state.get(FACING);
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		BlockPos downPos = pos.down(1);
		BlockPos conveyorPos = pos.offset(direction).up();

		BlockEntity downBlockEntity = world.getBlockEntity(downPos);

		blockEntity.setDown(downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).canInsert(Direction.UP));

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		checkForConveyor(world, state, conveyorBlockEntity, direction, pos, pos.up());
	}

	@Override
	public void checkForConveyor(World world, BlockState state, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !conveyorBlockEntity.isRemoved() && ((Conveyable) conveyorBlockEntity).canExtract(direction.getOpposite(), getType())) {
			state = state.with(ConveyorProperties.CONVEYOR, true);
		} else {
			state = state.with(ConveyorProperties.CONVEYOR, false);
		}

		world.setBlockState(pos, state, 8);
	}
}
