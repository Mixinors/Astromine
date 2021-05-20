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

import com.github.mixinors.astromine.common.block.entity.base.ComponentItemBlockEntity;
import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.common.util.StackUtils;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMConfig;
import me.shedaniel.architectury.extensions.BlockEntityExtension;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.mixinors.astromine.common.conveyor.Conveyable;
import com.github.mixinors.astromine.common.conveyor.Conveyor;
import com.github.mixinors.astromine.common.conveyor.ConveyorConveyable;
import com.github.mixinors.astromine.common.conveyor.ConveyorTypes;

import java.util.function.Supplier;

public class ConveyorBlockEntity extends ComponentItemBlockEntity implements ConveyorConveyable, BlockEntityExtension, Tickable {
	protected boolean front = false;
	protected boolean down = false;
	protected boolean across = false;

	protected int position = 0;
	protected int prevPosition = 0;

	public ConveyorBlockEntity() {
		super(AMBlockEntityTypes.CONVEYOR);
	}

	public ConveyorBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return ItemComponent.of(1).withListener((inventory) -> {
			if (world instanceof ServerWorld serverWorld) {
				sendPacket(serverWorld, toTag(new CompoundTag()));
			}
		});
	}

	@Override
	public void tick() {
		var direction = getCachedState().get(HorizontalFacingBlock.FACING);
		
		var speed = ((Conveyor) getCachedState().getBlock()).getSpeed();

		if (!isEmpty()) {
			if (across) {
				var frontPos = getPos().offset(direction);
				var frontAcrossPos = frontPos.offset(direction);

				if (world.getBlockEntity(frontPos) instanceof ConveyorConveyable conveyable && world.getBlockEntity(frontAcrossPos) instanceof ConveyorConveyable acrossConveyable) {
					handleMovementAcross(conveyable, acrossConveyable, speed, true);
				}
			} else if (front) {
				var frontPos = getPos().offset(direction);

				if (world.getBlockEntity(frontPos) instanceof Conveyable conveyable) {
					handleMovement(conveyable, speed, true);
				}
			} else if (down) {
				var downPos = getPos().offset(direction).down();

				if (world.getBlockEntity(downPos) instanceof Conveyable conveyable) {
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
		var accepted = conveyable.accepts(items.getFirst());

		if (accepted > 0) {
			if (position < speed) {
				setPosition(getPosition() + 1);
			} else if (transition && position == speed) {
				var split = items.getFirst().copy();
				split.setCount(Math.min(accepted, split.getCount()));
				
				items.getFirst().decrement(accepted);
				items.updateListeners();

				conveyable.give(split);
			}
		} else if (conveyable instanceof ConveyorConveyable conveyor) {
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
		var accepted = conveyable.accepts(items.getFirst());

		if (accepted > 0) {
			if (position < speed) {
				if (conveyable instanceof ConveyorConveyable && acrossConveyable instanceof ConveyorConveyable acrossConveyor) {
					if (acrossConveyor.getPosition() == 0) {
						setPosition(getPosition() + 1);
					} else {
						prevPosition = position;
					}
				}
			} else if (transition && position == speed) {
				var split = items.getFirst().copy();
				split.setCount(Math.min(accepted, split.getCount()));

				items.getFirst().decrement(accepted);
				items.updateListeners();

				conveyable.give(split);
			}
		} else if (conveyable instanceof ConveyorConveyable conveyor && acrossConveyable instanceof ConveyorConveyable acrossConveyor) {
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
	public int accepts(ItemStack stack) {
		var firstStack = items.getFirst();
		
		if (firstStack.isEmpty() || (AMConfig.get().conveyorsMergeStacks && StackUtils.areItemsAndTagsEqual(stack, firstStack))) {
			return firstStack.getMaxCount() - firstStack.getCount();
		} else {
			return 0;
		}
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
			var merge = StackUtils.merge(stack, getItemComponent().getFirst());
			
			items.setFirst(merge.getRight());
		}
	}


	public int[] getRenderAttachmentData() {
		return new int[]{ position, prevPosition };
	}

	public boolean hasFront() {
		return front;
	}

	public void setFront(boolean front) {
		this.front = front;

		markDirty();

		if (world instanceof ServerWorld serverWorld) {
			sendPacket(serverWorld, toTag(new CompoundTag()));
		}
	}

	public boolean hasDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;

		markDirty();
		
		if (world instanceof ServerWorld serverWorld) {
			sendPacket(serverWorld, toTag(new CompoundTag()));
		}
	}

	public boolean hasAcross() {
		return across;
	}

	public void setAcross(boolean across) {
		this.across = across;

		markDirty();
		
		if (world instanceof ServerWorld serverWorld) {
			sendPacket(serverWorld, toTag(new CompoundTag()));
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

		front = compoundTag.getBoolean("Front");
		down = compoundTag.getBoolean("Down");
		across = compoundTag.getBoolean("Across");

		position = compoundTag.getInt("Position");
		prevPosition = compoundTag.getInt("PreviousPosition");

		getItemComponent().setFirst(ItemStack.fromTag(compoundTag.getCompound("stack")));
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.putBoolean("Front", front);
		compoundTag.putBoolean("Down", down);
		compoundTag.putBoolean("Across", across);

		compoundTag.putInt("Position", position);
		compoundTag.putInt("PreviousPosition", prevPosition);

		compoundTag.put("Stack", getItemComponent().getFirst().toTag(new CompoundTag()));

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public void loadClientData(BlockState state, CompoundTag compoundTag) {
		fromTag(state, compoundTag);
	}

	@Override
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}
}
