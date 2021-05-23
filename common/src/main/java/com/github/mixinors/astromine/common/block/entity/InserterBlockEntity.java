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

import com.github.mixinors.astromine.common.component.base.ItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMConfig;
import me.shedaniel.architectury.extensions.BlockEntityExtension;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.mixinors.astromine.common.block.InserterBlock;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InserterBlockEntity extends BlockEntity implements BlockEntityExtension, Tickable {
	private int position = 0;
	private int prevPosition = 0;
	
	private final ItemComponent items = createItemComponent();

	public InserterBlockEntity() {
		super(AMBlockEntityTypes.INSERTER.get());
	}

	public InserterBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type.get());
	}

	public ItemComponent createItemComponent() {
		return ItemComponent.of(1).withListener((inventory) -> {
			if (world instanceof ServerWorld serverWorld) {
				sendPacket(serverWorld, toTag(new CompoundTag()));
			}
		});
	}

	public ItemComponent getItemComponent() {
		return items;
	}

	@Override
	public void tick() {
		var state = getCachedState();
		var block = state.getBlock();
		
		var facing = state.get(HorizontalFacingBlock.FACING);

		var powered = state.get(Properties.POWERED);

		var speed = ((InserterBlock) block).getSpeed();

		if (!powered) {
			if (items.isEmpty()) {
				var behindState = world.getBlockState(getPos().offset(facing.getOpposite()));

				var extractableItemComponent = ItemComponent.from(world.getBlockEntity(getPos().offset(facing.getOpposite())));

				if (extractableItemComponent != null && !extractableItemComponent.isEmpty()) {
					var stack = extractableItemComponent.getFirstExtractable(facing);

					if (position == 0 && stack != null && !(behindState.getBlock() instanceof InserterBlock)) {
						extractableItemComponent.into(items, AMConfig.get().inserterStackSize, facing, facing);
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					var offsetPos = getPos().offset(facing.getOpposite());

					var entityInventories = world.getEntitiesByClass(Entity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof PlayerEntity) && (entity instanceof Inventory)).stream().map(it -> (Inventory) it).collect(Collectors.toList());

					if (position == 0 && entityInventories.size() >= 1) {
						var entityInventory = entityInventories.get(0);

						extractableItemComponent = ItemComponent.from(entityInventory);

						var stack = extractableItemComponent.getFirstExtractable(facing.getOpposite());

						if (position == 0 && !stack.isEmpty()) {
							extractableItemComponent.into(items, AMConfig.get().inserterStackSize, facing);

							entityInventory.markDirty();
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (!items.isEmpty()) {
				var aheadState = world.getBlockState(getPos().offset(facing));

				var insertableItems = ItemComponent.from(world.getBlockEntity(getPos().offset(facing)));

				var insertionDirection = facing.getOpposite();

				if (aheadState.getBlock() instanceof ComposterBlock) {
					insertionDirection = Direction.DOWN;
				} else if (aheadState.getBlock() instanceof AbstractFurnaceBlock && !AbstractFurnaceBlockEntity.canUseAsFuel(items.getFirst())) {
					insertionDirection = Direction.DOWN;
				}

				if (insertableItems != null) {
					var sampleStack = items.getFirst().copy();
					sampleStack.setCount(1);

					var stack = insertableItems.getFirstInsertable(insertionDirection, sampleStack);

					if (stack != null) {
						if (position < speed) {
							setPosition(getPosition() + 1);
						} else if (!world.isClient) {
							items.into(insertableItems, AMConfig.get().inserterStackSize, facing, insertionDirection);
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					var offsetPos = getPos().offset(facing);

					var entityInventories = world.getEntitiesByClass(Entity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof PlayerEntity) && (entity instanceof Inventory)).stream().map(it -> (Inventory) it).collect(Collectors.toList());

					if (entityInventories.size() >= 1) {
						var entityInventory = entityInventories.get(0);

						insertableItems = ItemComponent.from(entityInventory);

						var stack = insertableItems.getFirstInsertable(insertionDirection, items.getFirst());

						if (position < speed && (stack.isEmpty() || stack.getCount() != items.getFirst().getCount())) {
							setPosition(getPosition() + 1);
						} else if (!world.isClient && (stack.isEmpty() || stack.getCount() != items.getFirst().getCount())) {
							items.into(insertableItems, AMConfig.get().inserterStackSize, facing, insertionDirection);

							entityInventory.markDirty();
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

	protected void sendPacket(ServerWorld world, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getId(getType()).toString());
		
		sendPacket(world, new BlockEntityUpdateS2CPacket(getPos(), 127, tag));
	}

	protected void sendPacket(ServerWorld world, BlockEntityUpdateS2CPacket packet) {
		world.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(getPos())) < 40 * 40).forEach(player -> player.networkHandler.sendPacket(packet));
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	public void fromTag(BlockState state, CompoundTag compoundTag) {
		super.fromTag(state, compoundTag);

		items.setFirst(ItemStack.fromTag(compoundTag.getCompound("Stack")));

		position = compoundTag.getInt("Position");
	}

	@Override
	public void loadClientData(BlockState state, CompoundTag compoundTag) {
		fromTag(state, compoundTag);
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		compoundTag.put("Stack", items.getFirst().toTag(new CompoundTag()));

		compoundTag.putInt("Position", position);

		return super.toTag(compoundTag);
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}
}
