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

import com.github.chainmailstudios.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.ItemComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleItemComponent;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.chainmailstudios.astromine.common.inventory.SingularStackInventory;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.Conveyor;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorConveyable;
import com.github.chainmailstudios.astromine.transportations.common.conveyor.ConveyorTypes;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

public class ConveyorBlockEntity extends ComponentItemBlockEntity implements ConveyorConveyable, BlockEntityClientSerializable, RenderAttachmentBlockEntity, Tickable {
	protected boolean front = false;
	protected boolean down = false;
	protected boolean across = false;

	protected int position = 0;
	protected int prevPosition = 0;

	public ConveyorBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.CONVEYOR);
	}

	public ConveyorBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(1) {
			@Override
			public ItemStack removeStack(int slot) {
				position = 0;
				prevPosition = 0;

				return super.removeStack(slot);
			}
		}.withListener((inventory) -> {
			if (world != null && !world.isClient) {
				sendPacket((ServerWorld) world, toTag(new CompoundTag()));
			}
		});
	}

	@Override
	public void tick() {
		Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
		int speed = ((Conveyor) getCachedState().getBlock()).getSpeed();

		if (!isEmpty()) {
			if (across) {
				BlockPos frontPos = getPos().offset(direction);
				BlockPos frontAcrossPos = frontPos.offset(direction);

				if (getWorld().getBlockEntity(frontPos) instanceof ConveyorConveyable && getWorld().getBlockEntity(frontAcrossPos) instanceof ConveyorConveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(frontPos);
					Conveyable acrossConveyable = (Conveyable) getWorld().getBlockEntity(frontAcrossPos);

					handleMovementAcross(conveyable, acrossConveyable, speed, true);
				}
			} else if (front) {
				BlockPos frontPos = getPos().offset(direction);

				if (getWorld().getBlockEntity(frontPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(frontPos);

					handleMovement(conveyable, speed, true);
				}
			} else if (down) {
				BlockPos downPos = getPos().offset(direction).down();

				if (getWorld().getBlockEntity(downPos) instanceof Conveyable) {
					Conveyable conveyable = (Conveyable) getWorld().getBlockEntity(downPos);

					handleMovement(conveyable, speed, true);
				}
			} else if (position != 0) {
				setPosition(0);
			}
		} else if (position != 0) {
			setPosition(0);
		}
	}

	public void handleMovement(Conveyable conveyable, int speed, boolean transition) {
		if (conveyable.accepts(getItemComponent().getFirst())) {
			if (position < speed) {
				setPosition(getPosition() + 1);
			} else if (transition && position == speed) {
				conveyable.give(getItemComponent().getFirst());

				getItemComponent().setFirst(ItemStack.EMPTY);
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
		if (conveyable.accepts(getItemComponent().getFirst())) {
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
				conveyable.give(getItemComponent().getFirst());

				getItemComponent().setFirst(ItemStack.EMPTY);
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
		return ((Conveyor) getCachedState().getBlock()).getType();
	}

	@Override
	public boolean accepts(ItemStack stack) {
		return isEmpty();
	}

	@Override
	public boolean canInsert(Direction direction) {
		return direction != getCachedState().get(HorizontalFacingBlock.FACING) && direction != Direction.UP && direction != Direction.DOWN;
	}

	@Override
	public boolean canExtract(Direction direction, ConveyorTypes type) {
		return getCachedState().get(HorizontalFacingBlock.FACING) == direction;
	}

	@Override
	public void give(ItemStack stack) {
		if (front || across || down) {
			prevPosition = -1;
		}

		if (!world.isClient) {
			getItemComponent().setFirst(stack);
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

		markDirty();

		if (!world.isClient) {
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
		}
	}

	public boolean hasDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;

		markDirty();

		if (!world.isClient) {
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
		}
	}

	public boolean hasAcross() {
		return across;
	}

	public void setAcross(boolean across) {
		this.across = across;

		markDirty();

		if (!world.isClient) {
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
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

	protected void sendPacket(ServerWorld w, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getId(getType()).toString());
		sendPacket(w, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
	}

	protected void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet) {
		world.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(getPos())) < 40 * 40).forEach(player -> player.networkHandler.sendPacket(packet));
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compoundTag) {
		super.fromTag(state, compoundTag);

		front = compoundTag.getBoolean("front");
		down = compoundTag.getBoolean("down");
		across = compoundTag.getBoolean("across");

		position = compoundTag.getInt("position");
		prevPosition = compoundTag.getInt("prevPosition");

		getItemComponent().setFirst(ItemStack.fromTag(compoundTag.getCompound("stack")));
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.putBoolean("front", front);
		compoundTag.putBoolean("down", down);
		compoundTag.putBoolean("across", across);

		compoundTag.putInt("position", position);
		compoundTag.putInt("prevPosition", prevPosition);

		compoundTag.put("stack", getItemComponent().getFirst().toTag(new CompoundTag()));

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(getCachedState(), compoundTag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}
}
