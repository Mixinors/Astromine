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

package com.github.chainmailstudios.astromine.transportations.common.block.entity.base;

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.chainmailstudios.astromine.common.inventory.DoubleStackInventory;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

public class AbstractConveyableBlockEntity extends ComponentBlockEntity implements Conveyable, DoubleStackInventory, RenderAttachmentBlockEntity {
	int leftPosition = 0;
	int prevLeftPosition = 0;
	int rightPosition = 0;
	int prevRightPosition = 0;
	boolean hasBeenRemoved = false;
	boolean left = false;
	boolean right = false;
	private DefaultedList<ItemStack> stacks = DefaultedList.ofSize(2, ItemStack.EMPTY);

	public AbstractConveyableBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public void tick() {
		if (world == null || !tickRedstone())
			return;

		Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
		int speed = 16;

		if (!getLeftStack().isEmpty()) {
			if (left) {
				BlockPos leftPos = getPos().offset(direction.rotateYCounterclockwise());
				if (getWorld().getBlockEntity(leftPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(leftPos);
					handleLeftMovement(conveyable, speed, true);
				}
			} else {
				setLeftPosition(0);
			}
		} else {
			setLeftPosition(0);
		}

		if (!getRightStack().isEmpty()) {
			if (right) {
				BlockPos rightPos = getPos().offset(direction.rotateYClockwise());
				if (getWorld().getBlockEntity(rightPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(rightPos);
					handleRightMovement(conveyable, speed, true);
				}
			} else {
				setRightPosition(0);
			}
		} else {
			setRightPosition(0);
		}
	}

	public void handleLeftMovement(Conveyable conveyable, int speed, boolean transition) {
		if (conveyable.accepts(getLeftStack())) {
			if (leftPosition < speed) {
				setLeftPosition(getLeftPosition() + 1);
			} else if (transition && leftPosition >= speed) {
				conveyable.give(getLeftStack());
				if (!world.isClient() || world.isClient && MinecraftClient.getInstance().player.squaredDistanceTo(Vec3d.of(getPos())) > 40 * 40)
					removeLeftStack();
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (leftPosition < speed && leftPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setLeftPosition(getLeftPosition() + 1);
			} else {
				prevLeftPosition = leftPosition;
			}
		} else if (leftPosition > 0) {
			setLeftPosition(leftPosition - 1);
		} else if (prevLeftPosition != leftPosition) {
			prevLeftPosition = leftPosition;
		}
	}

	public void handleRightMovement(Conveyable conveyable, int speed, boolean transition) {
		if (conveyable.accepts(getRightStack())) {
			if (rightPosition < speed) {
				setRightPosition(getRightPosition() + 1);
			} else if (transition && rightPosition >= speed) {
				conveyable.give(getRightStack());
				if (!world.isClient() || world.isClient && MinecraftClient.getInstance().player.squaredDistanceTo(Vec3d.of(getPos())) > 40 * 40)
					removeRightStack();
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (rightPosition < speed && rightPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setRightPosition(getRightPosition() + 1);
			} else {
				prevRightPosition = rightPosition;
			}
		} else if (rightPosition > 0) {
			setRightPosition(rightPosition - 1);
		} else if (prevRightPosition != rightPosition) {
			prevRightPosition = rightPosition;
		}
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return stacks;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		DoubleStackInventory.super.setStack(slot, stack);
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = DoubleStackInventory.super.removeStack(slot);
		leftPosition = 0;
		rightPosition = 0;
		prevLeftPosition = 0;
		prevRightPosition = 0;
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
		return stack;
	}

	@Override
	public void clear() {
		DoubleStackInventory.super.clear();
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		if (leftPosition == 0)
			this.prevLeftPosition = 0;
		else this.prevLeftPosition = this.leftPosition;
		this.leftPosition = leftPosition;
	}

	public int getRightPosition() {
		return rightPosition;
	}

	public void setRightPosition(int rightPosition) {
		if (rightPosition == 0)
			this.prevRightPosition = 0;
		else this.prevRightPosition = this.rightPosition;
		this.rightPosition = rightPosition;
	}

	@Override
	public boolean hasBeenRemoved() {
		return hasBeenRemoved;
	}

	@Override
	public void setRemoved(boolean hasBeenRemoved) {
		this.hasBeenRemoved = hasBeenRemoved;
	}

	public boolean hasLeft() {
		return left;
	}

	public boolean hasRight() {
		return right;
	}

	public void setLeft(boolean left) {
		this.left = left;
		markDirty();
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
	}

	public void setRight(boolean right) {
		this.right = right;
		markDirty();
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return !(!getLeftStack().isEmpty() && !getRightStack().isEmpty());
	}

	@Override
	public boolean validInputSide(Direction direction) {
		return direction == getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
	}

	@Override
	public boolean isOutputSide(Direction direction, ConveyorTypes type) {
		return getCachedState().get(HorizontalFacingBlock.FACING).rotateYCounterclockwise() == direction || getCachedState().get(HorizontalFacingBlock.FACING).rotateYClockwise() == direction;
	}

	@Override
	public void give(ItemStack stack) {

	}

	@Override
	public int[] getRenderAttachmentData() {
		return new int[]{ leftPosition, prevLeftPosition, rightPosition, prevRightPosition };
	}

	protected void sendPacket(ServerWorld w, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getId(getType()).toString());
		sendPacket(w, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
	}

	protected void sendPacket(ServerWorld w, BlockEntityUpdateS2CPacket packet) {
		w.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(getPos())) < 40 * 40).forEach(player -> player.networkHandler.sendPacket(packet));
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compoundTag) {
		super.fromTag(state, compoundTag);
		getItems().set(0, ItemStack.fromTag(compoundTag.getCompound("leftStack")));
		getItems().set(1, ItemStack.fromTag(compoundTag.getCompound("rightStack")));
		left = compoundTag.getBoolean("left");
		right = compoundTag.getBoolean("right");
		leftPosition = compoundTag.getInt("leftPosition");
		prevLeftPosition = compoundTag.getInt("prevLeftPosition");
		rightPosition = compoundTag.getInt("rightPosition");
		prevRightPosition = compoundTag.getInt("prevRightPosition");
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(getCachedState(), compoundTag);
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.put("leftStack", getLeftStack().toTag(new CompoundTag()));
		compoundTag.put("rightStack", getRightStack().toTag(new CompoundTag()));
		compoundTag.putBoolean("left", left);
		compoundTag.putBoolean("right", right);
		compoundTag.putInt("leftPosition", leftPosition);
		compoundTag.putInt("prevLeftPosition", prevLeftPosition);
		compoundTag.putInt("rightPosition", rightPosition);
		compoundTag.putInt("prevRightPosition", prevRightPosition);
		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}
}
