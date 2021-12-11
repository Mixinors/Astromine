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

package com.github.mixinors.astromine.common.block.entity.base;

import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.SimpleItemComponent;
import com.github.mixinors.astromine.common.util.StackUtils;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorConveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

import java.util.function.Supplier;

public abstract class AbstractConveyableBlockEntity extends ComponentItemBlockEntity implements Conveyable {
	int leftPosition = 0;
	int prevLeftPosition = 0;
	
	int rightPosition = 0;
	int prevRightPosition = 0;
	
	boolean left = false;
	boolean right = false;
	
	public AbstractConveyableBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(2) {
			@Override
			public ItemStack removeStack(int slot) {
				leftPosition = 0;
				rightPosition = 0;

				prevLeftPosition = 0;
				prevRightPosition = 0;

				return super.removeStack(slot);
			}
		}.withListener((inventory) -> {
			if (world != null && !world.isClient) {
				sendPacket((ServerWorld) world, writeNbt(new NbtCompound()));
			}
		});
	}

	@Override
	public void tick() {
		if (world == null || !tickRedstone())
			return;

		Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);

		int speed = 16;

		if (!getItemComponent().getFirst().isEmpty()) {
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

		if (!getItemComponent().getSecond().isEmpty()) {
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
		int accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted > 0) {
			if (leftPosition < speed) {
				setLeftPosition(getLeftPosition() + 1);
			} else if (transition) {
				ItemStack split = getItemComponent().getFirst().copy();
				split.setCount(Math.min(accepted, split.getCount()));

				getItemComponent().getFirst().decrement(accepted);
				getItemComponent().updateListeners();

				conveyable.give(split);
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
		int accepted = conveyable.accepts(getItemComponent().getSecond());

		if (accepted > 0) {
			if (rightPosition < speed) {
				setRightPosition(getRightPosition() + 1);
			} else if (transition) {
				ItemStack split = getItemComponent().getSecond().copy();
				split.setCount(Math.min(accepted, split.getCount()));

				getItemComponent().getSecond().decrement(accepted);
				getItemComponent().updateListeners();

				conveyable.give(split);
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


	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		if (leftPosition == 0) {
			this.prevLeftPosition = 0;
		} else {
			this.prevLeftPosition = this.leftPosition;
		}

		this.leftPosition = leftPosition;
	}

	public int getRightPosition() {
		return rightPosition;
	}

	public void setRightPosition(int rightPosition) {
		if (rightPosition == 0) {
			this.prevRightPosition = 0;
		} else {
			this.prevRightPosition = this.rightPosition;
		}

		this.rightPosition = rightPosition;
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

		if (!world.isClient) {
			sendPacket((ServerWorld) world, writeNbt(new NbtCompound()));
		}
	}

	public void setRight(boolean right) {
		this.right = right;

		markDirty();

		if (!world.isClient) {
			sendPacket((ServerWorld) world, writeNbt(new NbtCompound()));
		}
	}

	@Override
	public int accepts(ItemStack stack) {
		if (getItemComponent().getFirst().isEmpty() || StackUtils.areItemsAndTagsEqual(stack, getItemComponent().getFirst())) {
			return getItemComponent().getFirst().getMaxCount() - getItemComponent().getFirst().getCount();
		} else if (getItemComponent().getSecond().isEmpty() || StackUtils.areItemsAndTagsEqual(stack, getItemComponent().getSecond())) {
			return getItemComponent().getSecond().getMaxCount() - getItemComponent().getSecond().getCount();
		} else {
			return 0;
		}
	}

	@Override
	public boolean canInsert(Direction direction) {
		return direction == getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return getCachedState().get(HorizontalFacingBlock.FACING).rotateYCounterclockwise() == direction || getCachedState().get(HorizontalFacingBlock.FACING).rotateYClockwise() == direction;
	}

	public int[] getRenderAttachmentData() {
		return new int[] { leftPosition, prevLeftPosition, rightPosition, prevRightPosition };
	}

	protected void sendPacket(ServerWorld world, NbtCompound tag) {
		tag.putString("id", BlockEntityType.getId(getType()).toString());

		sendPacket(world, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
	}

	protected void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet) {
		world.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(getPos())) < 40 * 40).forEach(player -> player.networkHandler.sendPacket(packet));
	}

	@Override
	public void readNbt(BlockState state, NbtCompound compoundTag) {
		super.readNbt(state, compoundTag);

		getItemComponent().setFirst(ItemStack.fromNbt(compoundTag.getCompound("leftStack")));
		getItemComponent().setSecond(ItemStack.fromNbt(compoundTag.getCompound("rightStack")));

		leftPosition = compoundTag.getInt("leftPosition");
		prevLeftPosition = compoundTag.getInt("prevLeftPosition");

		rightPosition = compoundTag.getInt("rightPosition");
		prevRightPosition = compoundTag.getInt("prevRightPosition");

		left = compoundTag.getBoolean("left");
		right = compoundTag.getBoolean("right");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compoundTag) {
		compoundTag.put("leftStack", getItemComponent().getFirst().writeNbt(new NbtCompound()));
		compoundTag.put("rightStack", getItemComponent().getSecond().writeNbt(new NbtCompound()));


		compoundTag.putInt("leftPosition", leftPosition);
		compoundTag.putInt("prevLeftPosition", prevLeftPosition);

		compoundTag.putInt("rightPosition", rightPosition);
		compoundTag.putInt("prevRightPosition", prevRightPosition);

		compoundTag.putBoolean("left", left);
		compoundTag.putBoolean("right", right);

		return super.writeNbt(compoundTag);
	}

	@Override
	public void loadClientData(BlockState state, NbtCompound compoundTag) {
		readNbt(state, compoundTag);
	}

	@Override
	public NbtCompound saveClientData(NbtCompound compoundTag) {
		return writeNbt(compoundTag);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return writeNbt(new NbtCompound());
	}
}
