package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.common.conveyor.ConveyorType;
import com.github.chainmailstudios.astromine.common.inventory.DoubleStackInventory;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DoubleMachineBlockEntity extends BlockEntity implements Conveyable, DoubleStackInventory, BlockEntityClientSerializable, RenderAttachmentBlockEntity, Tickable {
	private DefaultedList<ItemStack> stacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
	int leftPosition = 0;
	int prevLeftPosition = 0;
	int rightPosition = 0;
	int prevRightPosition = 0;
	boolean hasBeenRemoved = false;
	boolean left = false;
	boolean right = false;

    public DoubleMachineBlockEntity(BlockEntityType type) {
        super(type);
    }

	@Override
    public void tick() {
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
			} else if (transition && !getWorld().isClient() && leftPosition >= speed) {
				conveyable.give(getLeftStack());
				removeLeftStack();
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (leftPosition < speed && leftPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setLeftPosition(getLeftPosition() + 1);
			} else {
				prevLeftPosition = leftPosition;
			}
		}
	}

	public void handleRightMovement(Conveyable conveyable, int speed, boolean transition) {
		if (conveyable.accepts(getRightStack())) {
			if (rightPosition < speed) {
				setRightPosition(getRightPosition() + 1);
			} else if (transition && !getWorld().isClient() && rightPosition >= speed) {
				conveyable.give(getRightStack());
				removeRightStack();
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (rightPosition < speed && rightPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setRightPosition(getRightPosition() + 1);
			} else {
				prevRightPosition = rightPosition;
			}
		}
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return stacks;
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public int getRightPosition() {
		return rightPosition;
	}

	public void setLeftPosition(int leftPosition) {
		if (leftPosition == 0)
			this.prevLeftPosition = 0;
		else
			this.prevLeftPosition = this.leftPosition;
		this.leftPosition = leftPosition;
	}

	public void setRightPosition(int rightPosition) {
		if (rightPosition == 0)
			this.prevRightPosition = 0;
		else
			this.prevRightPosition = this.rightPosition;
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
	}

	public void setRight(boolean right) {
		this.right = right;
		markDirty();
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return isEmpty();
	}

	@Override
	public boolean validInputSide(Direction direction) {
		return direction == getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
	}

	@Override
	public boolean isOutputSide(Direction direction, ConveyorType type) {
		return getCachedState().get(HorizontalFacingBlock.FACING).rotateYCounterclockwise() == direction || getCachedState().get(HorizontalFacingBlock.FACING).rotateYClockwise() == direction;
	}

	@Override
	public void give(ItemStack stack) {

	}

	@Override
	public int[] getRenderAttachmentData() {
		return new int[] { leftPosition, prevLeftPosition, rightPosition, prevRightPosition };
	}

	public void sync() {
		if (world instanceof ServerWorld) {
			((ServerWorld)world).getChunkManager().markForUpdate(pos);
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		sync();
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compoundTag) {
		super.fromTag(state, compoundTag);
		clear();
		setLeftStack(ItemStack.fromTag(compoundTag.getCompound("leftStack")));
		setRightStack(ItemStack.fromTag(compoundTag.getCompound("rightStack")));
		left = compoundTag.getBoolean("left");
		right = compoundTag.getBoolean("right");
		leftPosition = compoundTag.getInt("leftPosition");
		prevLeftPosition = compoundTag.getInt("leftPosition");
		rightPosition = compoundTag.getInt("rightPosition");
		prevRightPosition = compoundTag.getInt("rightPosition");
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
		compoundTag.putInt("rightPosition", rightPosition);
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
