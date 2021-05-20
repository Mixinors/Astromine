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
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.block.entity.SplitterBlockEntity;
import com.github.mixinors.astromine.common.block.entity.base.AbstractConveyableBlockEntity;
import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyableBlock;

public class SplitterBlock extends HorizontalFacingBlock implements BlockEntityProvider, ConveyableBlock {
	public SplitterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new SplitterBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayer().isSneaking() ? ctx.getPlayerFacing().getOpposite() : ctx.getPlayerFacing());
	}
	
	@Override
	public void onBlockAdded(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean notify) {
		updateDiagonals(world, this, pos);
	}
	
	@Override
	public void onStateReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (oldState.getBlock() != newState.getBlock()) {
			var blockEntity = world.getBlockEntity(pos);
			
			if (blockEntity instanceof AbstractConveyableBlockEntity conveyableBlockEntity) {
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), conveyableBlockEntity.getItemComponent().getFirst());
				ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), conveyableBlockEntity.getItemComponent().getSecond());

				blockEntity.markRemoved();
			}

			super.onStateReplaced(oldState, world, pos, newState, moved);
		}

		updateDiagonals(world, this, pos);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean $) {
		var facing = state.get(FACING);

		var machineBlockEntity = (AbstractConveyableBlockEntity) world.getBlockEntity(pos);

		var leftPos = pos.offset(facing.rotateYCounterclockwise());
		var rightPos = pos.offset(facing.rotateYClockwise());

		var leftBlockEntity = world.getBlockEntity(leftPos);

		machineBlockEntity.setLeft(leftBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(facing.rotateYClockwise()));

		var rightBlockEntity = world.getBlockEntity(rightPos);

		machineBlockEntity.setRight(rightBlockEntity instanceof Conveyable conveyable && conveyable.canInsert(facing.rotateYCounterclockwise()));
	}
}
