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

package com.github.chainmailstudios.astromine.transportations.common.block.entity;

import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.block.entity.base.AbstractConveyableBlockEntity;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SplitterBlockEntity extends AbstractConveyableBlockEntity {
	public SplitterBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.SPLITTER);
	}

	public SplitterBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public boolean accepts(ItemStack stack) {
		Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		BlockPos leftPos = getBlockPos().relative(facing.getCounterClockWise());
		BlockPos rightPos = getBlockPos().relative(facing.getClockWise());

		BlockEntity leftBlockEntity = level.getBlockEntity(leftPos);
		BlockEntity rightBlockEntity = level.getBlockEntity(rightPos);

		if (leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).accepts(stack)) {
			return getItemComponent().getFirst().isEmpty() || getItemComponent().getFirst().getMaxStackSize() - getItemComponent().getFirst().getCount() >= stack.getCount() && StackUtilities.areItemsAndTagsEqual(getItemComponent().getFirst(), stack);
		}

		if (rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).accepts(stack)) {
			return getItemComponent().getSecond().isEmpty() || getItemComponent().getSecond().getMaxStackSize() - getItemComponent().getSecond().getCount() >= stack.getCount() && StackUtilities.areItemsAndTagsEqual(getItemComponent().getSecond(), stack);
		}

		return false;
	}

	@Override
	public void give(ItemStack stack) {
		Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		BlockPos leftPos = getBlockPos().relative(facing.getCounterClockWise());
		BlockPos rightPos = getBlockPos().relative(facing.getClockWise());

		BlockEntity leftBlockEntity = level.getBlockEntity(leftPos);
		BlockEntity rightBlockEntity = level.getBlockEntity(rightPos);

		boolean allowsLeft = false;
		boolean allowsRight = false;

		if (leftBlockEntity instanceof Conveyable && ((Conveyable) leftBlockEntity).accepts(stack)) {
			allowsLeft = super.accepts(stack);
		}

		if (rightBlockEntity instanceof Conveyable && ((Conveyable) rightBlockEntity).accepts(stack)) {
			allowsRight = super.accepts(stack);
		}

		int size = stack.getCount();
		int smallHalf = allowsLeft ? size / 2 : 0;
		int largeHalf = allowsRight ? size - smallHalf : 0;

		if (allowsLeft && !allowsRight) {
			smallHalf = stack.getCount();
			largeHalf = 0;
		}

		if (allowsRight && !allowsLeft) {
			largeHalf = stack.getCount();
			smallHalf = 0;
		}

		ItemStack smallStack = stack.copy();
		ItemStack largeStack = stack.copy();

		smallStack.setCount(smallHalf);
		largeStack.setCount(largeHalf);

		if (smallStack.getCount() > 0)
			getItemComponent().setFirst(smallStack);

		if (largeStack.getCount() > 0)
			getItemComponent().setSecond(largeStack);

		level.playSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), AstromineTransportationsSoundEvents.MACHINE_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
	}
}