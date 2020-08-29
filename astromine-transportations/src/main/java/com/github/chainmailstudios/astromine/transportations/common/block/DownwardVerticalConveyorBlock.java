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
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import com.github.chainmailstudios.astromine.transportations.common.block.entity.ConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.DownVerticalConveyorBlockEntity;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

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
	public BlockState getStateForNeighborUpdate(BlockState blockState, Direction fromDirection, BlockState fromState, WorldAccess world, BlockPos blockPos, BlockPos fromPos) {
		BlockState newState = blockState;
		Direction direction = newState.get(FACING);

		BlockPos frontPos = blockPos.offset(direction.getOpposite());
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity frontBlockEntity = world.getBlockEntity(frontPos);
		if (frontBlockEntity instanceof Conveyable && ((Conveyable) frontBlockEntity).validInputSide(direction))
			newState = newState.with(ConveyorProperties.FRONT, true);
		else newState = newState.with(ConveyorProperties.FRONT, false);

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
		if (world.isAir(blockPos.up()) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).isOutputSide(direction.getOpposite(), getType()))
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		else newState = newState.with(ConveyorProperties.CONVEYOR, false);

		return newState;
	}

	@Override
	public void neighborUpdate(BlockState blockState, World world, BlockPos blockPos, Block block, BlockPos blockPos2, boolean boolean_1) {
		Direction direction = blockState.get(FACING);
		ConveyorBlockEntity blockEntity = (ConveyorBlockEntity) world.getBlockEntity(blockPos);

		BlockPos downPos = blockPos.down(1);
		BlockPos conveyorPos = blockPos.offset(direction).up();

		BlockEntity downBlockEntity = world.getBlockEntity(downPos);
		if (downBlockEntity instanceof Conveyable && ((Conveyable) downBlockEntity).validInputSide(Direction.UP))
			blockEntity.setDown(true);
		else blockEntity.setDown(false);

		BlockEntity conveyorBlockEntity = world.getBlockEntity(conveyorPos);
		checkForConveyor(world, blockState, conveyorBlockEntity, direction, blockPos, blockPos.up());
	}

	@Override
	public void checkForConveyor(World world, BlockState blockState, BlockEntity conveyorBlockEntity, Direction direction, BlockPos pos, BlockPos upPos) {
		BlockState newState = blockState;

		if (world.isAir(upPos) && conveyorBlockEntity instanceof Conveyable && !((Conveyable) conveyorBlockEntity).hasBeenRemoved() && ((Conveyable) conveyorBlockEntity).isOutputSide(direction.getOpposite(), getType()))
			newState = newState.with(ConveyorProperties.CONVEYOR, true);
		else newState = newState.with(ConveyorProperties.CONVEYOR, false);

		world.setBlockState(pos, newState, 8);
	}
}
