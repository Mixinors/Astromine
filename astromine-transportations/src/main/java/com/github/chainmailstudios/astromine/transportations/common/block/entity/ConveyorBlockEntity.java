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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentBlockEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.general.base.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.general.SimpleItemComponent;
import com.github.chainmailstudios.astromine.common.utilities.StackUtilities;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

public class ConveyorBlockEntity extends ComponentBlockEntity implements ConveyorConveyable, BlockEntityClientSerializable, RenderAttachmentBlockEntity, TickableBlockEntity {
	protected boolean front = false;
	protected boolean down = false;
	protected boolean across = false;

	protected int position = 0;
	protected int prevPosition = 0;

	private final ItemComponent itemComponent = createItemComponent();

	public ConveyorBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.CONVEYOR);
	}

	public ConveyorBlockEntity(BlockEntityType type) {
		super(type);
	}

	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(1) {
			@Override
			public ItemStack removeStack(int slot) {
				position = 0;
				prevPosition = 0;

				return super.removeStack(slot);
			}
		}.withInsertPredicate(((direction, stack, slot) -> {
			return accepts(stack);
		})).withListener((inventory) -> {
			if (level != null && !level.isClientSide) {
				sendPacket((ServerLevel) level, save(new CompoundTag()));
			}
		});
	}

	public ItemComponent getItemComponent() {
		return itemComponent;
	}

	@Override
	public void tick() {
		Direction direction = getBlockState().getValue(HorizontalDirectionalBlock.FACING);
		int speed = ((Conveyor) getBlockState().getBlock()).getSpeed();

		if (!getItemComponent().isEmpty()) {
			if (across) {
				BlockPos frontPos = getBlockPos().relative(direction);
				BlockPos frontAcrossPos = frontPos.relative(direction);

				if (getLevel().getBlockEntity(frontPos) instanceof ConveyorConveyable && getLevel().getBlockEntity(frontAcrossPos) instanceof ConveyorConveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(frontPos);
					Conveyable acrossConveyable = (Conveyable) getLevel().getBlockEntity(frontAcrossPos);

					handleMovementAcross(conveyable, acrossConveyable, speed, true);
				}
			} else if (front) {
				BlockPos frontPos = getBlockPos().relative(direction);

				if (getLevel().getBlockEntity(frontPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(frontPos);

					handleMovement(conveyable, speed, true);
				}
			} else if (down) {
				BlockPos downPos = getBlockPos().relative(direction).below();

				if (getLevel().getBlockEntity(downPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getLevel().getBlockEntity(downPos);

					handleMovement(conveyable, speed, true);
				}
			} else if (position != 0) {
				setPosition(0);
			}
		} else if (position != 0) {
			setPosition(0);
		}

		getItemComponent().updateListeners();
	}

	public void handleMovement(Conveyable conveyable, int speed, boolean transition) {
		boolean accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted) {
			if (position < speed) {
				setPosition(getPosition() + 1);
			} else if (transition && position == speed) {
				ItemStack given = getItemComponent().getFirst();
				getItemComponent().setFirst(ItemStack.EMPTY);

				conveyable.give(given);
			}
		} else if (conveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;

			if (position < speed && position + 1 < conveyor.getPosition() && conveyor.getPosition() > 1) {
				setPosition(getPosition() + 1);
			} else {
				prevPosition = position;
			}
		} else if (position > 0) {
			setPosition(position - 1);
		} else if (prevPosition != position) {
			prevPosition = position;
		}
	}

	public void handleMovementAcross(Conveyable conveyable, Conveyable acrossConveyable, int speed, boolean transition) {
		boolean accepted = conveyable.accepts(getItemComponent().getFirst());

		if (accepted) {
			if (position < speed) {
				if (conveyable instanceof ConveyorConveyable && acrossConveyable instanceof ConveyorConveyable) {
					ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;
					ConveyorConveyable acrossConveyor = (ConveyorConveyable) acrossConveyable;

					if (position < speed && acrossConveyor.getPosition() == 0) {
						setPosition(getPosition() + 1);
					} else {
						prevPosition = position;
					}
				}
			} else if (transition && position == speed) {
				ItemStack given = getItemComponent().getFirst();
				getItemComponent().setFirst(ItemStack.EMPTY);

				conveyable.give(given);
			}
		} else if (conveyable instanceof ConveyorConveyable && acrossConveyable instanceof ConveyorConveyable) {
			ConveyorConveyable conveyor = (ConveyorConveyable) conveyable;
			ConveyorConveyable acrossConveyor = (ConveyorConveyable) acrossConveyable;

			if (position < speed && acrossConveyor.getPosition() == 0 && position + 1 < conveyor.getPosition() && conveyor.getPosition() > 1) {
				setPosition(getPosition() + 1);
			} else {
				prevPosition = position;
			}
		} else if (position > 0) {
			setPosition(position - 1);
		} else if (prevPosition != position) {
			prevPosition = position;
		}
	}

	@Override
	public ConveyorTypes getConveyorType() {
		return ((Conveyor) getBlockState().getBlock()).getType();
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return getItemComponent().isEmpty();
	}

	@Override
	public boolean canInsert(Direction direction) {
		return direction != getBlockState().getValue(HorizontalDirectionalBlock.FACING) && direction != Direction.UP && direction != Direction.DOWN;
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return getBlockState().getValue(HorizontalDirectionalBlock.FACING) == direction;
	}

	@Override
	public void give(ItemStack stack) {
		if (front || across || down) {
			prevPosition = -1;
		}

		if (!level.isClientSide) {
			Tuple<ItemStack, ItemStack> merge = StackUtilities.merge(stack, getItemComponent().getFirst());
			getItemComponent().setFirst(merge.getB());
		}
	}


	@Override
	public int[] getRenderAttachmentData() {
		return new int[]{ position, prevPosition };
	}

	public boolean hasFront() {
		return front;
	}

	public void setFront(boolean front) {
		this.front = front;

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
		}
	}

	public boolean hasDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
		}
	}

	public boolean hasAcross() {
		return across;
	}

	public void setAcross(boolean across) {
		this.across = across;

		setChanged();

		if (!level.isClientSide) {
			sendPacket((ServerLevel) level, save(new CompoundTag()));
		}
	}

	@Override
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		if (position == 0) {
			this.prevPosition = 0;
		} else {
			this.prevPosition = this.position;
		}

		this.position = position;
	}

	@Override
	public int getPreviousPosition() {
		return prevPosition;
	}

	protected void sendPacket(ServerLevel w, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getKey(getType()).toString());
		sendPacket(w, new ClientboundBlockEntityDataPacket(getBlockPos(), 127, tag));
	}

	protected void sendPacket(ServerLevel world, ClientboundBlockEntityDataPacket packet) {
		world.getPlayers(player -> player.distanceToSqr(Vec3.atLowerCornerOf(getBlockPos())) < 40 * 40).forEach(player -> player.connection.send(packet));
	}

	@Override
	public void load(BlockState state, CompoundTag compoundTag) {
		super.load(state, compoundTag);

		front = compoundTag.getBoolean("front");
		down = compoundTag.getBoolean("down");
		across = compoundTag.getBoolean("across");

		position = compoundTag.getInt("position");
		prevPosition = compoundTag.getInt("prevPosition");

		getItemComponent().setFirst(ItemStack.of(compoundTag.getCompound("stack")));
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		compoundTag.putBoolean("front", front);
		compoundTag.putBoolean("down", down);
		compoundTag.putBoolean("across", across);

		compoundTag.putInt("position", position);
		compoundTag.putInt("prevPosition", prevPosition);

		compoundTag.put("stack", getItemComponent().getFirst().save(new CompoundTag()));

		return super.save(compoundTag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		load(getBlockState(), compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return save(compoundTag);
	}
}
