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

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import alexiil.mc.lib.attributes.item.compat.FixedInventoryVanillaWrapper;
import alexiil.mc.lib.attributes.item.impl.EmptyItemExtractable;
import alexiil.mc.lib.attributes.item.impl.RejectingItemInsertable;
import com.github.chainmailstudios.astromine.common.inventory.SingularStackInventory;
import com.github.chainmailstudios.astromine.transportations.common.block.InserterBlock;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

import java.util.List;
import java.util.stream.IntStream;

public class InserterBlockEntity extends BlockEntity implements SingularStackInventory, BlockEntityClientSerializable, RenderAttachmentBlockEntity, Tickable {
	protected int position = 0;
	protected int prevPosition = 0;
	private DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public InserterBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.INSERTER);
	}

	public InserterBlockEntity(BlockEntityType type) {
		super(type);
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
		Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
		boolean powered = getCachedState().get(Properties.POWERED);
		int speed = ((InserterBlock) getCachedState().getBlock()).getSpeed();

		if (!powered) {
			if (isEmpty()) {
				BlockState behindState = world.getBlockState(getPos().offset(direction.getOpposite()));
				ItemExtractable extractable = ItemAttributes.EXTRACTABLE.get(world, getPos().offset(direction.getOpposite()), SearchOptions.inDirection(direction.getOpposite()));

				if (behindState.getBlock() instanceof AbstractFurnaceBlock) {
					extractable = ItemAttributes.EXTRACTABLE.get(world, getPos().offset(direction.getOpposite()), SearchOptions.inDirection(Direction.UP));
				}

				if (extractable != EmptyItemExtractable.NULL) {
					ItemStack stack = extractable.attemptAnyExtraction(64, Simulation.SIMULATE);
					if (position == 0 && !stack.isEmpty() && !(behindState.getBlock() instanceof InserterBlock)) {
						stack = extractable.attemptAnyExtraction(64, Simulation.ACTION);
						setStack(stack);
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(direction.getOpposite());
					List<ChestMinecartEntity> minecartEntities = getWorld().getEntitiesByClass(ChestMinecartEntity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1),
						EntityPredicates.EXCEPT_SPECTATOR);
					if (position == 0 && minecartEntities.size() >= 1) {
						ChestMinecartEntity minecartEntity = minecartEntities.get(0);
						FixedInventoryVanillaWrapper wrapper = new FixedInventoryVanillaWrapper(minecartEntity);
						ItemExtractable extractableMinecart = wrapper.getExtractable();

						ItemStack stackMinecart = extractableMinecart.attemptAnyExtraction(64, Simulation.SIMULATE);
						if (position == 0 && !stackMinecart.isEmpty()) {
							stackMinecart = extractableMinecart.attemptAnyExtraction(64, Simulation.ACTION);
							setStack(stackMinecart);
							minecartEntity.markDirty();
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (!isEmpty()) {
				BlockState aheadState = getWorld().getBlockState(getPos().offset(direction));

				ItemInsertable insertable = ItemAttributes.INSERTABLE.get(world, getPos().offset(direction), SearchOptions.inDirection(direction));

				if (aheadState.getBlock() instanceof ComposterBlock) {
					insertable = ItemAttributes.INSERTABLE.get(world, getPos().offset(direction), SearchOptions.inDirection(Direction.DOWN));
				} else if (aheadState.getBlock() instanceof AbstractFurnaceBlock && !AbstractFurnaceBlockEntity.canUseAsFuel(getStack())) {
					insertable = ItemAttributes.INSERTABLE.get(world, getPos().offset(direction), SearchOptions.inDirection(Direction.DOWN));
				}

				ItemStack stack = insertable.attemptInsertion(getStack(), Simulation.SIMULATE);
				if (insertable != RejectingItemInsertable.NULL) {
					if (stack.isEmpty() || stack.getCount() != getStack().getCount()) {
						if (position < speed) {
							setPosition(getPosition() + 1);
						} else if (!getWorld().isClient()) {
							stack = insertable.attemptInsertion(getStack(), Simulation.ACTION);
							setStack(stack);
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(direction);
					List<ChestMinecartEntity> minecartEntities = getWorld().getEntitiesByClass(ChestMinecartEntity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1),
						EntityPredicates.EXCEPT_SPECTATOR);
					if (minecartEntities.size() >= 1) {
						ChestMinecartEntity minecartEntity = minecartEntities.get(0);
						if (minecartEntity instanceof Inventory) {
							FixedInventoryVanillaWrapper wrapper = new FixedInventoryVanillaWrapper(minecartEntity);
							ItemInsertable insertableMinecart = wrapper.getInsertable();

							ItemStack stackMinecart = insertableMinecart.attemptInsertion(getStack(), Simulation.SIMULATE);
							if (position < speed && (stackMinecart.isEmpty() || stackMinecart.getCount() != getStack().getCount())) {
								setPosition(getPosition() + 1);
							} else if (!getWorld().isClient() && (stackMinecart.isEmpty() || stackMinecart.getCount() != getStack().getCount())) {
								stackMinecart = insertableMinecart.attemptInsertion(getStack(), Simulation.ACTION);
								setStack(stackMinecart);
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
	public DefaultedList<ItemStack> getItems() {
		return stacks;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		SingularStackInventory.super.setStack(slot, stack);
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = SingularStackInventory.super.removeStack(slot);
		position = 15;
		prevPosition = 15;
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
		return stack;
	}

	@Override
	public void clear() {
		SingularStackInventory.super.clear();
		if (!world.isClient())
			sendPacket((ServerWorld) world, toTag(new CompoundTag()));
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
		getItems().set(0, ItemStack.fromTag(compoundTag.getCompound("stack")));
		position = compoundTag.getInt("position");
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(getCachedState(), compoundTag);
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.put("stack", getStack().toTag(new CompoundTag()));
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
