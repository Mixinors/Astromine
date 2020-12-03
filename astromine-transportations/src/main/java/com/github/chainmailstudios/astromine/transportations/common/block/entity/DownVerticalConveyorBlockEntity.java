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

import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleItemComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import com.github.chainmailstudios.astromine.transportations.common.block.property.ConveyorProperties;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

public class DownVerticalConveyorBlockEntity extends ConveyorBlockEntity {
	protected boolean down = false;
	protected int horizontalPosition;
	protected int prevHorizontalPosition;

	public DownVerticalConveyorBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.DOWNWARD_VERTICAL_CONVEYOR);
	}

	@Override
	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(1) {
			@Override
			public void setStack(int slot, ItemStack stack) {
				super.setStack(slot, stack);

				if (stack.isEmpty()) {
					position = 0;
					prevPosition = 0;
				}

				horizontalPosition = 0;
				prevHorizontalPosition = 0;

				if (level != null && !level.isClientSide) {
					sendPacket((ServerLevel) level, save(new CompoundTag()));
				}
			}

			@Override
			public ItemStack removeStack(int slot) {
				ItemStack stack = super.removeStack(slot);

				position = 0;
				prevPosition = 0;

				horizontalPosition = 0;
				prevHorizontalPosition = 0;

				if (level != null && !level.isClientSide) {
					sendPacket((ServerLevel) level, save(new CompoundTag()));
				}

				return stack;
			}

			@Override
			public void clear() {
				super.clear();

				if (level != null && !level.isClientSide) {
					sendPacket((ServerLevel) level, save(new CompoundTag()));
				}
			}
		}.withListener((inventory) -> {
			if (level != null && !level.isClientSide) {
				sendPacket((ServerLevel) level, save(new CompoundTag()));
			}
		});
	}

	@Override
	public void give(ItemStack stack) {
		super.give(stack);
	}

	@Override
	public void tick() {
		Direction direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		int speed = ((Conveyor) getBlockState().getBlock()).getSpeed();

		if (!getItemComponent().isEmpty()) {
			if (getBlockState().getValue(ConveyorProperties.FRONT)) {
				BlockPos frontPos = getBlockPos().relative(direction.getOpposite());

				if (getLevel().getBlockEntity(frontPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(frontPos);

					if (getBlockState().getValue(ConveyorProperties.CONVEYOR)) {
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
				BlockPos downPos = getBlockPos().below();

				if (getLevel().getBlockEntity(downPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(downPos);

					if (getBlockState().getValue(ConveyorProperties.CONVEYOR)) {
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
		boolean accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted) {
			if (horizontalPosition < speed) {
				setHorizontalPosition(getHorizontalPosition() + 1);
			} else if (transition && horizontalPosition >= speed) {
				ItemStack given = getItemComponent().getFirst();
				getItemComponent().setFirst(ItemStack.EMPTY);

				conveyable.give(given);
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
	public boolean canInsert(Direction direction) {
		return direction == Direction.UP || direction == getBlockState().getValue(HorizontalDirectionalBlock.FACING);
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite() == direction || direction == Direction.DOWN;
	}

	@Override
	public boolean hasDown() {
		return down;
	}

	@Override
	public void setDown(boolean down) {
		this.down = down;

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
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
		if (horizontalPosition == 0) {
			this.prevHorizontalPosition = 0;
		} else {
			this.prevHorizontalPosition = this.horizontalPosition;
		}

		this.horizontalPosition = horizontalPosition;
	}

	@Override
	public void load(BlockState state, CompoundTag compoundTag) {
		super.load(state, compoundTag);

		down = compoundTag.getBoolean("down_vertical");

		horizontalPosition = compoundTag.getInt("horizontalPosition");
		prevHorizontalPosition = horizontalPosition = compoundTag.getInt("horizontalPosition");
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		load(getBlockState(), compoundTag);
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		compoundTag.putBoolean("down_vertical", down);

		compoundTag.putInt("horizontalPosition", horizontalPosition);

		return super.save(compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return save(compoundTag);
	}
}
