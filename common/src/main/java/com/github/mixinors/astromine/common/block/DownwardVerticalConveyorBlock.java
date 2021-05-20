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
import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

public class DownwardVerticalConveyorBlock extends VerticalConveyorBlock {
	public DownwardVerticalConveyorBlock(Settings settings, int speed) {
		super(settings, speed);

		setDefaultState(getDefaultState().with(ConveyorBlock.FRONT, false).with(ConveyorBlock.CONVEYOR, false));
	}

	@Override
	public ConveyorTypes getType() {
		return ConveyorTypes.DOWN_VERTICAL;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new DownVerticalConveyorBlockEntity();
	}

	@Override
	public void onBlockAdded(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean notify) {
		updateDiagonals(world, this, pos.up());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess access, BlockPos pos, BlockPos neighborPos) {
		var facing = state.get(FACING);

		var frontPos = pos.offset(facing.getOpposite());
		var conveyorPos = pos.offset(facing).up();

		var frontBlockEntity = access.getBlockEntity(frontPos);

		if (frontBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(facing)) {
			state = state.with(ConveyorBlock.FRONT, true);
		} else {
			state = state.with(ConveyorBlock.FRONT, false);
		}

		var conveyorBlockEntity = access.getBlockEntity(conveyorPos);

		if (access.isAir(pos.up()) && conveyorBlockEntity instanceof Conveyable conveyable && !conveyorBlockEntity.isRemoved() && conveyable.canExtract(facing.getOpposite(), getType())) {
			state = state.with(ConveyorBlock.CONVEYOR, true);
		} else {
			state = state.with(ConveyorBlock.CONVEYOR, false);
		}

		return state;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean notify) {
		var facing = state.get(FACING);
		var blockEntity = (ConveyorBlockEntity) world.getBlockEntity(pos);

		var downPos = pos.down(1);
		var conveyorPos = pos.offset(facing).up();

		var downBlockEntity = world.getBlockEntity(downPos);

		blockEntity.setDown(downBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(Direction.UP));

		var conveyorBlockEntity = world.getBlockEntity(conveyorPos);

		checkForConveyor(world, state, conveyorBlockEntity, facing, pos, pos.up());
	}

	@Override
	public void checkForConveyor(World world, BlockState state, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable conveyable && !conveyorBlockEntity.isRemoved() && conveyable.canExtract(direction.getOpposite(), getType())) {
			state = state.with(ConveyorBlock.CONVEYOR, true);
		} else {
			state = state.with(ConveyorBlock.CONVEYOR, false);
		}

		world.setBlockState(pos, state, 8);
	}
}
