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
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.github.chainmailstudios.astromine.transportations.common.block.InserterBlock;
import com.github.chainmailstudios.astromine.transportations.registry.AstromineTransportationsBlockEntityTypes;

import java.util.List;
import java.util.stream.Collectors;

public class InserterBlockEntity extends BlockEntity implements BlockEntityClientSerializable, RenderAttachmentBlockEntity, TickableBlockEntity {
	protected int position = 0;
	protected int prevPosition = 0;

	private final ItemComponent itemComponent = createItemComponent();

	public InserterBlockEntity() {
		super(AstromineTransportationsBlockEntityTypes.INSERTER);
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
		Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

		boolean powered = getBlockState().getValue(BlockStateProperties.POWERED);

		int speed = ((InserterBlock) getBlockState().getBlock()).getSpeed();

		if (!powered) {
			if (getItemComponent().isEmpty()) {
				BlockState behindState = level.getBlockState(getBlockPos().relative(facing.getOpposite()));

				ItemComponent extractableItemComponent = ItemComponent.get(level.getBlockEntity(getBlockPos().relative(facing.getOpposite())));

				if (extractableItemComponent != null && !extractableItemComponent.isEmpty()) {
					ItemStack stack = extractableItemComponent.getFirstExtractableStack(facing);

					if (position == 0 && stack != null && !(behindState.getBlock() instanceof InserterBlock)) {
						extractableItemComponent.into(getItemComponent(), AstromineConfig.get().inserterStackSize, facing, facing);
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getBlockPos().relative(facing.getOpposite());

					List<Container> entityInventories = getLevel().getEntitiesOfClass(Entity.class, new AABB(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof Player) && (entity instanceof Container)).stream().map(it -> (Container) it).collect(Collectors.toList());

					if (position == 0 && entityInventories.size() >= 1) {
						Container entityInventory = entityInventories.get(0);

						extractableItemComponent = ItemComponent.get(entityInventory);

						ItemStack stack = extractableItemComponent.getFirstExtractableStack(facing.getOpposite());

						if (position == 0 && !stack.isEmpty()) {
							extractableItemComponent.into(getItemComponent(), AstromineConfig.get().inserterStackSize, facing);

							entityInventory.setChanged();
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				}
			} else if (!getItemComponent().isEmpty()) {
				BlockState aheadState = getLevel().getBlockState(getBlockPos().relative(facing));

				ItemComponent insertableItemComponent = ItemComponent.get(level.getBlockEntity(getBlockPos().relative(facing)));

				Direction insertionDirection = facing.getOpposite();

				if (aheadState.getBlock() instanceof ComposterBlock) {
					insertionDirection = Direction.DOWN;
				} else if (aheadState.getBlock() instanceof AbstractFurnaceBlock && !AbstractFurnaceBlockEntity.isFuel(getItemComponent().getFirst())) {
					insertionDirection = Direction.DOWN;
				}

				if (insertableItemComponent != null) {
					ItemStack sampleStack = getItemComponent().getFirst().copy();
					sampleStack.setCount(1);

					ItemStack stack = insertableItemComponent.getFirstInsertableStack(insertionDirection, sampleStack);

					if (stack != null) {
						if (position < speed) {
							setPosition(getPosition() + 1);
						} else if (!level.isClientSide) {
							getItemComponent().into(insertableItemComponent, AstromineConfig.get().inserterStackSize, facing, insertionDirection);
						}
					} else if (position > 0) {
						setPosition(getPosition() - 1);
					}
				} else {
					BlockPos offsetPos = getBlockPos().relative(facing);

					List<Container> entityInventories = getLevel().getEntitiesOfClass(Entity.class, new AABB(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ(), offsetPos.getX() + 1, offsetPos.getY() + 1, offsetPos.getZ() + 1), (entity) -> !(entity instanceof Player) && (entity instanceof Container)).stream().map(it -> (Container) it).collect(Collectors.toList());

					if (entityInventories.size() >= 1) {
						Container entityInventory = entityInventories.get(0);

						insertableItemComponent = ItemComponent.get(entityInventory);

						ItemStack stack = insertableItemComponent.getFirstInsertableStack(insertionDirection, getItemComponent().getFirst());

						if (position < speed && (stack.isEmpty() || stack.getCount() != getItemComponent().getFirst().getCount())) {
							setPosition(getPosition() + 1);
						} else if (!level.isClientSide && (stack.isEmpty() || stack.getCount() != getItemComponent().getFirst().getCount())) {
							getItemComponent().into(insertableItemComponent, AstromineConfig.get().inserterStackSize, facing, insertionDirection);

							entityInventory.setChanged();
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

	protected void sendPacket(ServerLevel w, CompoundTag tag) {
		tag.putString("id", BlockEntityType.getKey(getType()).toString());
		sendPacket(w, new ClientboundBlockEntityDataPacket(getBlockPos(), 127, tag));
	}

	protected void sendPacket(ServerLevel w, ClientboundBlockEntityDataPacket packet) {
		w.getPlayers(player -> player.distanceToSqr(Vec3.atLowerCornerOf(getBlockPos())) < 40 * 40).forEach(player -> player.connection.send(packet));
	}

	@Override
	public void setChanged() {
		super.setChanged();
	}

	@Override
	public void load(BlockState state, CompoundTag compoundTag) {
		super.load(state, compoundTag);

		getItemComponent().setFirst(ItemStack.of(compoundTag.getCompound("stack")));

		position = compoundTag.getInt("position");
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		load(getBlockState(), compoundTag);
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		compoundTag.put("stack", getItemComponent().getFirst().save(new CompoundTag()));

		compoundTag.putInt("position", position);

		return super.save(compoundTag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return save(compoundTag);
	}
}
