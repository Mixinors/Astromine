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

package com.github.mixinors.astromine.common.block.entity;

import com.github.mixinors.astromine.common.block.ConveyorBlock;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.SimpleItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.Conveyor;
import com.github.mixinors.astromine.common.conveyor.ConveyorConveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

import java.util.function.Supplier;

public class VerticalConveyorBlockEntity extends ConveyorBlockEntity {
	protected boolean up = false;

	protected int horizontalPosition;
	protected int prevHorizontalPosition;

	public VerticalConveyorBlockEntity() {
		super(AMBlockEntityTypes.VERTICAL_CONVEYOR);
	}

	public VerticalConveyorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(1) {
			@Override
			public ItemStack removeStack(int slot) {
				position = 0;
				prevPosition = 0;

				horizontalPosition = 0;
				prevHorizontalPosition = 0;

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
		Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
		int speed = ((Conveyor) getCachedState().getBlock()).getSpeed();

		if (!isEmpty()) {
			if (getCachedState().get(ConveyorBlock.CONVEYOR)) {
				BlockPos conveyorPos = getPos().offset(direction).up();
				if (getWorld().getBlockEntity(conveyorPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(conveyorPos);
					if (position < speed) {
						handleMovement(conveyable, speed, false);
					} else {
						prevPosition = speed;
						handleMovementHorizontal(conveyable, speed, true);
					}
				}
			} else if (up) {
				BlockPos upPos = getPos().up();
				if (getWorld().getBlockEntity(upPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(upPos);
					handleMovement(conveyable, speed, true);
				}
			} else {
				setPosition(0);
			}
		} else {
			setPosition(0);
		}
	}

	public void handleMovementHorizontal(Conveyable conveyable, int speed, boolean transition) {
		int accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted > 0) {
			if (horizontalPosition < speed) {
				setHorizontalPosition(getHorizontalPosition() + 2);
			} else if (transition && horizontalPosition >= speed) {
				ItemStack split = getItemComponent().getFirst().copy();
				split.setCount(Math.min(accepted, split.getCount()));

				getItemComponent().getFirst().decrement(accepted);
				getItemComponent().updateListeners();

				conveyable.give(split);
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (horizontalPosition < speed && horizontalPosition + 4 < conveyor.getPosition() && conveyor.getPosition() > 4) {
				setHorizontalPosition(getHorizontalPosition() + 2);
			} else {
				prevHorizontalPosition = horizontalPosition;
			}
		}
	}

	@Override
	public boolean canInsert(Direction direction) {
		return !getCachedState().get(ConveyorBlock.FRONT) && direction == Direction.DOWN || direction == getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return type == ConveyorTypes.NORMAL ? getCachedState().get(HorizontalFacingBlock.FACING) == direction : direction == Direction.UP;
	}

	public boolean hasUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;

		markDirty();

		if (!world.isClient) {
			sendPacket((ServerWorld) world, writeNbt(new NbtCompound()));
		}
	}

	@Override
	public int[] getRenderAttachmentData() {
		return new int[]{ position, prevPosition, horizontalPosition, prevHorizontalPosition };
	}

	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	public void setHorizontalPosition(int horizontalPosition) {
		if (horizontalPosition == 0)
			this.prevHorizontalPosition = 0;
		else this.prevHorizontalPosition = this.horizontalPosition;

		this.horizontalPosition = horizontalPosition;
	}

	@Override
	public void readNbt(BlockState state, NbtCompound compoundTag) {
		super.readNbt(state, compoundTag);

		up = compoundTag.getBoolean("up");

		horizontalPosition = compoundTag.getInt("horizontalPosition");
		prevHorizontalPosition = horizontalPosition = compoundTag.getInt("horizontalPosition");
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compoundTag) {
		compoundTag.putBoolean("up", up);

		compoundTag.putInt("horizontalPosition", horizontalPosition);

		return super.writeNbt(compoundTag);
	}
}
