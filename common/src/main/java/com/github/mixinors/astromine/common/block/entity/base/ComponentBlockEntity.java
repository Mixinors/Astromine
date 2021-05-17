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

package com.github.mixinors.astromine.common.block.entity.base;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.common.component.general.base.EnergyComponent;
import com.github.mixinors.astromine.common.component.general.base.FluidComponent;
import com.github.mixinors.astromine.common.component.general.base.ItemComponent;
import com.github.mixinors.astromine.common.component.general.provider.RedstoneComponentProvider;
import com.github.mixinors.astromine.common.component.general.provider.TransferComponentProvider;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMComponents;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.common.block.base.BlockWithEntity;
import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.component.block.entity.RedstoneComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A {@link BlockEntity} which is synchronized to the client
 * through {@link BlockEntityExtension}, which is
 * {@link Tickable}, updates its {@link BlockState} based on
 * its activity, and handles redstone behavior.
 */
public abstract class ComponentBlockEntity extends BlockEntity implements BlockEntityExtension, Tickable, TransferComponentProvider, RedstoneComponentProvider {
	public static final Identifier TRANSFER_UPDATE_PACKET = AMCommon.id("transfer_update_packet");

	private final TransferComponent transferComponent = createTransferComponent();

	private final RedstoneComponent redstoneComponent = createRedstoneComponent();

	protected final Map<ComponentKey<?>, Component> allComponents = Maps.newHashMap();

	protected final Map<Identifier, Consumer<PacketByteBuf>> allHandlers = Maps.newHashMap();

	private boolean isActive = false;

	private final boolean[] activity = { false, false, false, false, false };

	protected boolean skipInventory = true;

	/** Instantiates a {@link ComponentBlockEntity}. */
	public ComponentBlockEntity(Supplier<? extends BlockEntityType<?>> type) {
		super(type.get());

		addPacketConsumer(TRANSFER_UPDATE_PACKET, ((buf) -> {
			Identifier packetIdentifier = buf.readIdentifier();
			Direction packetDirection = buf.readEnumConstant(Direction.class);
			TransferType packetTransferType = buf.readEnumConstant(TransferType.class);

			getTransferComponent().get(ComponentRegistry.get(packetIdentifier)).set(packetDirection, packetTransferType);
			markDirty();
			syncData();
		}));
	}

	/** Returns the {@link TransferComponent} to be attached. */
	public TransferComponent createTransferComponent() {
		return new TransferComponent();
	}

	/** Returns the attached {@link TransferComponent}. */
	@Override
	public TransferComponent getTransferComponent() {
		return transferComponent;
	}

	/** Returns the {@link RedstoneComponent} to be attached. */
	public RedstoneComponent createRedstoneComponent() {
		return new RedstoneComponent();
	}

	/** Returns the attached {@link RedstoneComponent}. */
	@Override
	public RedstoneComponent getRedstoneComponent() {
		return redstoneComponent;
	}

	/** Signals that this {@link ComponentBlockEntity} should synchronize
	 * its full inventory contents on the next {@link #tick()}. */
	public void doNotSkipInventory() {
		this.skipInventory = false;
	}

	/** Adds a {@link Component} to this {@link ComponentBlockEntity},
	 * appropriately adding an entry to {@link #getTransferComponent()}. */
	public void addComponent(ComponentKey<?> type, Component component) {
		allComponents.put(type, component);
		getTransferComponent().add(type);
	}

	/** Adds a {@link BiConsumer} that handles a {@link PacketByteBuf}
	 * whose header is the given {@link Identifier}. */
	public void addPacketConsumer(Identifier identifier, Consumer<PacketByteBuf> consumer) {
		allHandlers.put(identifier, consumer);
	}

	/** Consumes a {@link PacketByteBuf}, with the read header {@link Identifier},
	 * repassing it to the matching {@link BiConsumer}. */
	public void consumePacket(Identifier identifier, PacketByteBuf buf) {
		allHandlers.get(identifier).accept(buf);
	}

	/** Sets this machine as active. */
	public void tickActive() {
		isActive = true;
	}

	/** Sets this machine as inactive. */
	public void tickInactive() {
		isActive = false;
	}

	/** Ticks this {@link ComponentBlockEntity}'s redstone behavior,
	 * returning whether it should work or not. */
	public boolean tickRedstone() {
		boolean powered = world.getReceivedRedstonePower(getPos()) > 0;

		switch (getRedstoneComponent().getType()) {
			case WORK_WHEN_ON: {
				if (powered) tickActive(); else tickInactive();
				return powered;
			}

			case WORK_WHEN_OFF: {
				if (!powered) tickActive(); else tickInactive();
				return !powered;
			}

			default: {
				tickActive();
				return true;
			}
		}
	}

	/** Ticks this {@link ComponentBlockEntity},
	 * handling transfer between adjacent {@link BlockEntity}-ies
	 * and updating the machine's {@link BlockState}
	 * based on its activity, or lack thereof. */
	@Override
	public void tick() {
		if (!hasWorld() || world.isClient)
			return;

		List<Pair<EnergyHandler, EnergyHandler>> energyTransfers = Lists.newArrayList();

		for (Direction offsetDirection : Direction.values()) {
			Direction neighborDirection = offsetDirection.getOpposite();

			BlockPos neighborPos = getPos().offset(offsetDirection);

			BlockEntity neighborBlockEntity = world.getBlockEntity(neighborPos);

			if (getTransferComponent().hasItem()) {
				ItemComponent ourComponent = ItemComponent.get(this);

				if (ourComponent != null) {
					ItemComponent theirComponent = ItemComponent.get(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, 1, neighborDirection, offsetDirection);
						ourComponent.into(theirComponent, 1, offsetDirection, neighborDirection);
					}
				}
			}

			if (getTransferComponent().hasFluid()) {
				FluidComponent ourComponent = FluidComponent.get(this);

				if (ourComponent != null) {
					FluidComponent theirComponent = FluidComponent.get(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, FluidVolume.getTransfer(), neighborDirection, offsetDirection);
						ourComponent.into(theirComponent, FluidVolume.getTransfer(), offsetDirection, neighborDirection);
					}
				}
			}

			if (getTransferComponent().hasEnergy()) {
				EnergyComponent ourComponent = EnergyComponent.get(this);

				if (ourComponent != null) {
					EnergyComponent theirComponent = EnergyComponent.get(neighborBlockEntity);

					if (theirComponent != null) {
						theirComponent.into(ourComponent, 1024D);
						ourComponent.into(theirComponent, 1024D);
					}
				}
			}
		}

		if (world.getBlockState(getPos()).contains(BlockWithEntity.ACTIVE)) {
			if (activity.length - 1 >= 0)
				System.arraycopy(activity, 1, activity, 0, activity.length - 1);

			activity[4] = isActive;

			if (isActive && !activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, true));
			} else if (!isActive && activity[0]) {
				world.setBlockState(getPos(), world.getBlockState(getPos()).with(BlockWithEntity.ACTIVE, false));
			}
		}
	}

	/** Serializes this {@link ComponentBlockEntity} to a {@link CompoundTag}. */
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag transferTag = new CompoundTag();
		getTransferComponent().writeToNbt(transferTag);

		CompoundTag redstoneTag = new CompoundTag();
		getRedstoneComponent().writeToNbt(redstoneTag);

		tag.put("transfer", transferTag);
		tag.put("redstone", redstoneTag);

		allComponents.forEach((type, component) -> {
			CompoundTag componentTag = new CompoundTag();
			component.writeToNbt(componentTag);

			tag.put(type.getId().toString(), componentTag);
		});

		return super.toTag(tag);
	}

	/** Deserializes this {@link ComponentBlockEntity} from a {@link CompoundTag}. */
	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		getTransferComponent().readFromNbt(tag.getCompound("transfer"));
		getRedstoneComponent().readFromNbt(tag.getCompound("redstone"));

		allComponents.forEach((type, component) -> {
			if (tag.contains(type.getId().toString())) {
				component.readFromNbt(tag.getCompound(type.getId().toString()));
			}
		});

		this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
	}

	/** Serializes this {@link ComponentBlockEntity} to a {@link CompoundTag},
	 * for synchronization usage. */
	@Override
	public CompoundTag saveClientData(CompoundTag compoundTag) {
		compoundTag = toTag(compoundTag);
		if (skipInventory) {
			compoundTag.remove(AMComponents.ITEM_INVENTORY_COMPONENT.getId().toString());
		} else {
			skipInventory = true;
		}
		return compoundTag;
	}

	/** Deserializes this {@link ComponentBlockEntity} from a {@link CompoundTag},
	 * for synchronization usage. */
	@Override
	public void loadClientData(BlockState state, CompoundTag compoundTag) {
		fromTag(state, compoundTag);
	}
}
