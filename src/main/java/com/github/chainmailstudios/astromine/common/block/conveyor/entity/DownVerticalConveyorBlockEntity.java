package com.github.chainmailstudios.astromine.common.block.conveyor.entity;

import com.github.chainmailstudios.astromine.common.block.conveyor.ConveyorProperties;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyable;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.Conveyor;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.ConveyorConveyable;
import com.github.chainmailstudios.astromine.common.block.conveyor.interfaces.ConveyorType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DownVerticalConveyorBlockEntity extends ConveyorBlockEntity {
    protected boolean down = false;
    protected int horizontalPosition;
    protected int prevHorizontalPosition;

    public DownVerticalConveyorBlockEntity() {
        super(AstromineBlockEntityTypes.DOWN_VERTICAL_CONVEYOR);
    }

    @Override
    public void tick() {
        Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
		int speed = ((Conveyor) getCachedState().getBlock()).getSpeed();

		if (!isEmpty()) {
			if (getCachedState().get(ConveyorProperties.FRONT)) {
				BlockPos frontPos = getPos().offset(direction.getOpposite());
				if (getWorld().getBlockEntity(frontPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(frontPos);
					if (getCachedState().get(ConveyorProperties.CONVEYOR)) {
						if (position < speed) {
							handleMovement(conveyable, speed, false);
						} else {
							prevPosition = speed;
							handleMovementHorizontal(conveyable, speed, true);
						}
					} else {
						handleMovementHorizontal(conveyable, speed, true);
					}
				}
			} else if (down) {
				BlockPos downPos = getPos().down();
				if (getWorld().getBlockEntity(downPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(downPos);
					if (getCachedState().get(ConveyorProperties.CONVEYOR)) {
						handleMovement(conveyable, speed * 2, true);
					} else {
						handleMovement(conveyable, speed, true);
					}
				}
			} else {
				setPosition(0);
			}
		} else {
			setPosition(0);
		}
    }

	public void handleMovementHorizontal(Conveyable conveyable, int speed, boolean transition) {
		if (conveyable.accepts(getStack())) {
			if (horizontalPosition < speed) {
				setHorizontalPosition(getHorizontalPosition() + 1);
			} else if (transition && !getWorld().isClient() && horizontalPosition >= speed) {
				conveyable.give(getStack());
				removeStack();
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (horizontalPosition < speed && horizontalPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setHorizontalPosition(getHorizontalPosition() + 1);
			} else {
				prevHorizontalPosition = horizontalPosition;
			}
		}
	}

	@Override
	public boolean validInputSide(Direction direction) {
		return direction == Direction.UP || direction == getCachedState().get(HorizontalFacingBlock.FACING);
	}

	@Override
	public boolean isOutputSide(Direction direction, ConveyorType type) {
		return getCachedState().get(HorizontalFacingBlock.FACING).getOpposite() == direction || direction == Direction.DOWN;
	}

	@Override
    public ItemStack removeStack() {
        horizontalPosition = 0;
        prevHorizontalPosition = 0;
        return super.removeStack();
    }

    @Override
    public boolean hasDown() {
        return down;
    }

    @Override
    public void setDown(boolean down) {
        this.down = down;
        markDirty();
    }

    @Override
    public int[] getRenderAttachmentData() {
        return new int[] { position, prevPosition, horizontalPosition, prevHorizontalPosition };
    }

    public int getHorizontalPosition() {
        return horizontalPosition;
    }

    public void setHorizontalPosition(int horizontalPosition) {
        if (horizontalPosition == 0)
            this.prevHorizontalPosition = 0;
        else
            this.prevHorizontalPosition = this.horizontalPosition;

        this.horizontalPosition = horizontalPosition;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compoundTag) {
        super.fromTag(state, compoundTag);
        down = compoundTag.getBoolean("down_vertical");
        horizontalPosition = compoundTag.getInt("horizontalPosition");
        prevHorizontalPosition = horizontalPosition = compoundTag.getInt("horizontalPosition");
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(getCachedState(), compoundTag);
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        compoundTag.putBoolean("down_vertical", down);
        compoundTag.putInt("horizontalPosition", horizontalPosition);
        return super.toTag(compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }
}
