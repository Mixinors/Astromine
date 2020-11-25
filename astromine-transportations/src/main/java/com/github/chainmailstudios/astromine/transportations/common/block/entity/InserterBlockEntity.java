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

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.chainmailstudios.astromine.common.inventory.SingularStackInventory;
import com.github.chainmailstudios.astromine.transportations.common.block.InserterBlock;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

import java.util.List;
import java.util.stream.IntStream;

public class InserterBlockEntity extends ComponentItemBlockEntity implements BlockEntityClientSerializable, RenderAttachmentBlockEntity, Tickable {
	protected int position = 0;
	protected int prevPosition = 0;

	public InserterBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.INSERTER);
	}

	public InserterBlockEntity(BlockEntityType type) {
		super(type);
	}

	@Override
	public ItemComponent createItemComponent() {
		return new SimpleItemComponent(1) {
			@Override
			public ItemStack removeStack(int slot) {
				position = 15;
				prevPosition = 15;

				return super.removeStack(slot);
			}
		}.withListener((inventory) -> {
			if (world != null && !world.isClient) {
				sendPacket((ServerWorld) world, toTag(new CompoundTag()));
			}
		});
	}

	private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
		return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory) inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
	}

	public static ItemStack transfer(Inventory from, Inventory to, ItemStack stack, Direction side) {
		if (to instanceof SidedInventory && side != null) {
			SidedInventory sidedInventory = (SidedInventory) to;
			int[] is = sidedInventory.getAvailableSlots(side);

			for (int i = 0; i < is.length && !stack.isEmpty(); ++i) {
				stack = transfer(from, to, stack, is[i], side);
			}
		} else {
			int j = to.size();

			for (int k = 0; k < j && !stack.isEmpty(); ++k) {
				stack = transfer(from, to, stack, k, side);
			}
		}

		return stack;
	}

	private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, Direction side) {
		if (!inventory.isValid(slot, stack)) {
			return false;
		} else {
			return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canInsert(slot, stack, side);
		}
	}

	private static boolean canMergeItems(ItemStack first, ItemStack second) {
		if (first.getItem() != second.getItem()) {
			return false;
		} else if (first.getDamage() != second.getDamage()) {
			return false;
		} else if (first.getCount() > first.getMaxCount()) {
			return false;
		} else {
			return ItemStack.areTagsEqual(first, second);
		}
	}

	private static boolean canExtract(Inventory inventory, ItemStack stack, int slot, Direction facing) {
		return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canExtract(slot, stack, facing);
	}

	private static boolean extract(SingularStackInventory singularStackInventory, Inventory inventory, int slot, Direction side) {
		ItemStack stack = inventory.getStack(slot);
		if (!stack.isEmpty() && canExtract(inventory, stack, slot, side)) {
			ItemStack stackB = stack.copy();
			ItemStack stackC = transfer(inventory, singularStackInventory, inventory.removeStack(slot, inventory.getStack(slot).getCount()), null);
			if (stackC.isEmpty()) {
				inventory.markDirty();
				return true;
			}

			inventory.setStack(slot, stackB);
		}

		return false;
	}

	private static ItemStack transfer(Inventory from, Inventory to, ItemStack stackA, int slot, Direction direction) {
		ItemStack stackB = to.getStack(slot);
		if (canInsert(to, stackA, slot, direction)) {
			if (stackB.isEmpty()) {
				to.setStack(slot, stackA);
				stackA = ItemStack.EMPTY;
			} else if (canMergeItems(stackB, stackA)) {
				int i = stackA.getMaxCount() - stackB.getCount();
				int j = Math.min(stackA.getCount(), i);
				stackA.decrement(j);
				stackB.increment(j);
			}
		}

		return stackA;
	}

	@Override
	public void tick() {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

		boolean powered = getCachedState().get(Properties.POWERED);

		int speed = ((InserterBlock) getCachedState().getBlock()).getSpeed();

		if (!powered) {
			if (isEmpty()) {
				BlockState behindState = world.getBlockState(getPos().offset(facing.getOpposite()));

				ItemComponent extractableItemComponent = ItemComponent.get(world.getBlockEntity(getPos().offset(facing.getOpposite())));

				if (extractableItemComponent != null && !extractableItemComponent.isEmpty()) {
					ItemStack stack = extractableItemComponent.getFirstExtractableStack(facing);

					if (position == 0 && stack != null && !(behindState.getBlock() instanceof InserterBlock)) {
						extractableItemComponent.into(getItemComponent(), 64, facing);
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(facing.getOpposite());

					List<ChestMinecartEntity> minecartEntities = getWorld().getEntitiesByClass(ChestMinecartEntity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), EntityPredicates.EXCEPT_SPECTATOR);

					if (position == 0 && minecartEntities.size() >= 1) {
						ChestMinecartEntity minecartEntity = minecartEntities.get(0);

						extractableItemComponent = ItemComponent.get(minecartEntity);

						ItemStack stackMinecart = extractableItemComponent.getFirstExtractableStack(facing.getOpposite());
						if (position == 0 && !stackMinecart.isEmpty()) {
							extractableItemComponent.into(getItemComponent(), 64, facing);

							minecartEntity.markDirty();
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (!isEmpty()) {
				BlockState aheadState = getWorld().getBlockState(getPos().offset(facing));

				ItemComponent insertableItemComponent = ItemComponent.get(world.getBlockEntity(getPos().offset(facing)));

				Direction insertionDirection = facing;

				if (aheadState.getBlock() instanceof ComposterBlock) {
					insertionDirection = Direction.DOWN;
				} else if (aheadState.getBlock() instanceof AbstractFurnaceBlock && !AbstractFurnaceBlockEntity.canUseAsFuel(getItemComponent().getFirst())) {
					insertionDirection = Direction.DOWN;
				}

				if (insertableItemComponent != null) {
					ItemStack stack = insertableItemComponent.getFirstInsertableStack(insertionDirection, getItemComponent().getFirst());

					if (stack != null) {
						if (position < speed) {
							setPosition(getPosition() + 1);
						} else if (!getWorld().isClient()) {
							getItemComponent().into(insertableItemComponent, 64, insertionDirection);
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(facing);

					List<ChestMinecartEntity> minecartEntities = getWorld().getEntitiesByClass(ChestMinecartEntity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), EntityPredicates.EXCEPT_SPECTATOR);

					if (minecartEntities.size() >= 1) {
						ChestMinecartEntity minecartEntity = minecartEntities.get(0);

						if (minecartEntity instanceof Inventory) {
							insertableItemComponent = ItemComponent.get(minecartEntity);

							ItemStack stackMinecart = insertableItemComponent.getFirstInsertableStack(insertionDirection, getItemComponent().getFirst());

							if (position < speed && (stackMinecart.isEmpty() || stackMinecart.getCount() != getItemComponent().getFirst().getCount())) {
								setPosition(getPosition() + 1);
							} else if (!getWorld().isClient() && (stackMinecart.isEmpty() || stackMinecart.getCount() != getItemComponent().getFirst().getCount())) {
								getItemComponent().into(insertableItemComponent, 64, insertionDirection);

								((Inventory) minecartEntity).markDirty();
							}
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (position > 0) {
				setPosition(getPosition() - 1);
			}
		} else if (position > 0) {
			setPosition(getPosition() - 1);
		}
	}

	private boolean isInventoryFull(Inventory inventory, Direction direction) {
		return getAvailableSlots(inventory, direction).allMatch((i) -> {
			ItemStack stack = inventory.getStack(i);
			return stack.getCount() >= stack.getMaxCount();
		});
	}

	@Override
	public int[] getRenderAttachmentData() {
		return new int[]{ position, prevPosition };
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		if (position == 0)
			this.prevPosition = 0;
		else this.prevPosition = this.position;
		this.position = position;
	}

	public int getPrevPosition() {
		return prevPosition;
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

		getItemComponent().setFirst(ItemStack.fromTag(compoundTag.getCompound("stack")));

		position = compoundTag.getInt("position");
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(getCachedState(), compoundTag);
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.put("stack", getItemComponent().getFirst().toTag(new CompoundTag()));

		compoundTag.putInt("position", position);

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
