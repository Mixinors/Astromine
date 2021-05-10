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

import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.SimpleItemComponent;
import com.github.mixinors.astromine.registry.common.AMBlockEntityTypes;
import com.github.mixinors.astromine.registry.common.AMConfig;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import com.github.mixinors.astromine.common.block.InserterBlock;

import java.util.List;
import java.util.stream.Collectors;

public class InserterBlockEntity extends BlockEntity implements BlockEntityExtension, RenderAttachmentBlockEntity, Tickable {
	protected int position = 0;
	protected int prevPosition = 0;

	private final ItemComponent itemComponent = createItemComponent();

	public InserterBlockEntity() {
		super(AMBlockEntityTypes.INSERTER);
	}

	public InserterBlockEntity(BlockEntityType type) {
		super(type);
	}

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

	public ItemComponent getItemComponent() {
		return itemComponent;
	}

	@Override
	public void tick() {
		Direction facing = getCachedState().get(HorizontalFacingBlock.FACING);

		boolean powered = getCachedState().get(Properties.POWERED);

		int speed = ((InserterBlock) getCachedState().getBlock()).getSpeed();

		if (!powered) {
			if (getItemComponent().isEmpty()) {
				BlockState behindState = world.getBlockState(getPos().offset(facing.getOpposite()));

				ItemComponent extractableItemComponent = ItemComponent.get(world.getBlockEntity(getPos().offset(facing.getOpposite())));

				if (extractableItemComponent != null && !extractableItemComponent.isEmpty()) {
					ItemStack stack = extractableItemComponent.getFirstExtractableStack(facing);

					if (position == 0 && stack != null && !(behindState.getBlock() instanceof InserterBlock)) {
						extractableItemComponent.into(getItemComponent(), AMConfig.get().inserterStackSize, facing, facing);
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(facing.getOpposite());

					List<Inventory> entityInventories = getWorld().getEntitiesByClass(Entity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof PlayerEntity) && (entity instanceof Inventory)).stream().map(it -> (Inventory) it).collect(Collectors.toList());

					if (position == 0 && entityInventories.size() >= 1) {
						Inventory entityInventory = entityInventories.get(0);

						extractableItemComponent = ItemComponent.get(entityInventory);

						ItemStack stack = extractableItemComponent.getFirstExtractableStack(facing.getOpposite());

						if (position == 0 && !stack.isEmpty()) {
							extractableItemComponent.into(getItemComponent(), AMConfig.get().inserterStackSize, facing);

							entityInventory.markDirty();
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (!getItemComponent().isEmpty()) {
				BlockState aheadState = getWorld().getBlockState(getPos().offset(facing));

				ItemComponent insertableItemComponent = ItemComponent.get(world.getBlockEntity(getPos().offset(facing)));

				Direction insertionDirection = facing.getOpposite();

				if (aheadState.getBlock() instanceof ComposterBlock) {
					insertionDirection = Direction.DOWN;
				} else if (aheadState.getBlock() instanceof AbstractFurnaceBlock && !AbstractFurnaceBlockEntity.canUseAsFuel(getItemComponent().getFirst())) {
					insertionDirection = Direction.DOWN;
				}

				if (insertableItemComponent != null) {
					ItemStack sampleStack = getItemComponent().getFirst().copy();
					sampleStack.setCount(1);

					ItemStack stack = insertableItemComponent.getFirstInsertableStack(insertionDirection, sampleStack);

					if (stack != null) {
						if (position < speed) {
							setPosition(getPosition() + 1);
						} else if (!world.isClient) {
							getItemComponent().into(insertableItemComponent, AMConfig.get().inserterStackSize, facing, insertionDirection);
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getPos().offset(facing);

					List<Inventory> entityInventories = getWorld().getEntitiesByClass(Entity.class, new Box(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof PlayerEntity) && (entity instanceof Inventory)).stream().map(it -> (Inventory) it).collect(Collectors.toList());

					if (entityInventories.size() >= 1) {
						Inventory entityInventory = entityInventories.get(0);

						insertableItemComponent = ItemComponent.get(entityInventory);

						ItemStack stack = insertableItemComponent.getFirstInsertableStack(insertionDirection, getItemComponent().getFirst());

						if (position < speed && (stack.isEmpty() || stack.getCount() != getItemComponent().getFirst().getCount())) {
							setPosition(getPosition() + 1);
						} else if (!world.isClient && (stack.isEmpty() || stack.getCount() != getItemComponent().getFirst().getCount())) {
							getItemComponent().into(insertableItemComponent, AMConfig.get().inserterStackSize, facing, insertionDirection);

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
	public void loadClientData(BlockState state, CompoundTag compoundTag) {
		fromTag(state, compoundTag);
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
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		return toTag(compoundTag);
	}
}
