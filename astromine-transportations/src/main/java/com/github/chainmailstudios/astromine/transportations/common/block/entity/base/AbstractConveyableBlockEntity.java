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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;

public abstract class AbstractConveyableBlockEntity extends ComponentItemBlockEntity implements Conveyable, RenderAttachmentBlockEntity {
	int leftPosition = 0;
	int prevLeftPosition = 0;
	
	int rightPosition = 0;
	int prevRightPosition = 0;
	
	boolean left = false;
	boolean right = false;
	
	public AbstractConveyableBlockEntity(BlockEntityType type) {
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
			if (level != null && !level.isClientSide) {
				sendPacket((ServerLevel) level, save(new CompoundTag()));
			}
		});
	}

	@Override
	public void tick() {
		if (level == null || !tickRedstone())
			return;

		Direction direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		int speed = 16;

		if (!getItemComponent().getFirst().isEmpty()) {
			if (left) {
				BlockPos leftPos = getBlockPos().relative(direction.getCounterClockWise());

				if (getLevel().getBlockEntity(leftPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(leftPos);

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
				BlockPos rightPos = getBlockPos().relative(direction.getClockWise());

				if (getLevel().getBlockEntity(rightPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(rightPos);

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
		boolean accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted) {
			if (leftPosition < speed) {
				setLeftPosition(getLeftPosition() + 1);
			} else if (transition) {
				ItemStack given = getItemComponent().getFirst();
				getItemComponent().setFirst(ItemStack.EMPTY);

				conveyable.give(given);
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
		boolean accepted = conveyable.accepts(getItemComponent().getSecond());

		if (accepted) {
			if (rightPosition < speed) {
				setRightPosition(getRightPosition() + 1);
			} else if (transition) {
				ItemStack given = getItemComponent().getSecond();
				getItemComponent().setSecond(ItemStack.EMPTY);

				conveyable.give(given);
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

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
		}
	}

	public void setRight(boolean right) {
		this.right = right;

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
		}
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return  (getItemComponent().getFirst().isEmpty() || StackUtilities.areItemsAndTagsEqual(stack, getItemComponent().getFirst())) ||  (getItemComponent().getSecond().isEmpty() || StackUtilities.areItemsAndTagsEqual(stack, getItemComponent().getSecond()));
	}

	@Override
	public boolean canInsert(Direction direction) {
		return direction == getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite();
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return getBlockState().getValue(HorizontalDirectionalBlock.FACING).getCounterClockWise() == direction || getBlockState().getValue(HorizontalDirectionalBlock.FACING).getClockWise() == direction;
	}

	@Override
	public int[] getRenderAttachmentData() {
		return new int[] { leftPosition, prevLeftPosition, rightPosition, prevRightPosition };
	}

	protected void sendPacket(ServerLevel world, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getKey(getType()).toString());

		sendPacket(world, new ClientboundBlockEntityDataPacket(getBlockPos(), 127, tag));
	}

	protected void sendPacket(ServerLevel world, ClientboundBlockEntityDataPacket packet) {
		world.getPlayers(player -> player.distanceToSqr(Vec3.atLowerCornerOf(getBlockPos())) < 40 * 40).forEach(player -> player.connection.send(packet));
	}

	@Override
	public void load(BlockState state, CompoundTag compoundTag) {
		super.load(state, compoundTag);

		getItemComponent().setFirst(ItemStack.of(compoundTag.getCompound("leftStack")));
		getItemComponent().setSecond(ItemStack.of(compoundTag.getCompound("rightStack")));

		leftPosition = compoundTag.getInt("leftPosition");
		prevLeftPosition = compoundTag.getInt("prevLeftPosition");

		rightPosition = compoundTag.getInt("rightPosition");
		prevRightPosition = compoundTag.getInt("prevRightPosition");

		left = compoundTag.getBoolean("left");
		right = compoundTag.getBoolean("right");
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		compoundTag.put("leftStack", getItemComponent().getFirst().save(new CompoundTag()));
		compoundTag.put("rightStack", getItemComponent().getSecond().save(new CompoundTag()));


		compoundTag.putInt("leftPosition", leftPosition);
		compoundTag.putInt("prevLeftPosition", prevLeftPosition);

		compoundTag.putInt("rightPosition", rightPosition);
		compoundTag.putInt("prevRightPosition", prevRightPosition);

		compoundTag.putBoolean("left", left);
		compoundTag.putBoolean("right", right);

		return super.save(compoundTag);
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		load(getBlockState(), compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return save(compoundTag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}
}
