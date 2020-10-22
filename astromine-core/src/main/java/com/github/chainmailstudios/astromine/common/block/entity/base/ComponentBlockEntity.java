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

package com.github.chainmailstudios.astromine.common.block.entity.base;

import alexiil.mc.lib.attributes.item.ItemInvUtil;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityRedstoneComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import dev.onyxstudios.cca.api.v3.block.BlockComponentProvider;
import dev.onyxstudios.cca.api.v3.component.*;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.PacketContext;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.item.ItemAttributes;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.ItemInsertable;
import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.base.BlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.packet.PacketConsumer;
import com.github.chainmailstudios.astromine.common.utilities.TransportUtilities;
import com.github.chainmailstudios.astromine.common.utilities.capability.inventory.ExtendedComponentSidedInventoryProvider;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.function.BiConsumer;

public abstract class ComponentBlockEntity extends net.minecraft.block.entity.BlockEntity implements PacketConsumer, BlockEntityClientSerializable, Tickable {
	public static final Identifier TRANSFER_UPDATE_PACKET = AstromineCommon.identifier("transfer_update_packet");

	protected final Map<ComponentKey<?>, Component> allComponents = Maps.newHashMap();

	protected final Map<Identifier, BiConsumer<PacketByteBuf, PacketContext>> allHandlers = Maps.newHashMap();

	public boolean isActive = false;
	public boolean[] activity = { false, false, false, false, false };

	protected boolean skipInventory = true;

	protected int redstoneMode = 0;

	public ComponentBlockEntity(BlockEntityType<?> type) {
		super(type);

		addConsumer(TRANSFER_UPDATE_PACKET, ((buffer, context) -> {
			Identifier packetIdentifier = buffer.readIdentifier();
			Direction packetDirection = buffer.readEnumConstant(Direction.class);
			TransferType packetTransferType = buffer.readEnumConstant(TransferType.class);

			getTransferComponent().get(ComponentRegistry.get(packetIdentifier)).set(packetDirection, packetTransferType);
			markDirty();
			sync();
		}));
	}

	public void doNotSkipInventory() {
		this.skipInventory = false;
	}

	public void addComponent(ComponentKey<?> type, Component component) {
		allComponents.put(type, component);
		getTransferComponent().add(type);
	}

	public void addConsumer(Identifier identifier, BiConsumer<PacketByteBuf, PacketContext> consumer) {
		allHandlers.put(identifier, consumer);
	}

	@Override
	public void consumePacket(Identifier identifier, PacketByteBuf buffer, PacketContext context) {
		allHandlers.get(identifier).accept(buffer, context);
	}

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

	@Override
	public void fromTag(BlockState state, @NotNull CompoundTag tag) {
		getTransferComponent().readFromNbt(tag.getCompound("transfer"));
		getRedstoneComponent().readFromNbt(tag.getCompound("redstone"));

		allComponents.forEach((type, component) -> {
			if (tag.contains(type.getId().toString())) {
				component.readFromNbt(tag.getCompound(type.getId().toString()));
			}
		});

		super.fromTag(state, tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		compoundTag = toTag(compoundTag);
		if (skipInventory) {
			compoundTag.remove(AstromineComponents.ITEM_INVENTORY_COMPONENT.getId().toString());
		} else {
			skipInventory = true;
		}
		return compoundTag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		fromTag(null, compoundTag);
	}

	@Override
	public void tick() {
		if (!hasWorld() || world.isClient())
			return;

		FluidComponent fluidComponent = FluidComponent.get(this);

		List<Pair<EnergyHandler, EnergyHandler>> energyTransfers = Lists.newArrayList();

		for (Direction offsetDirection : Direction.values()) {
			BlockPos neighborPos = getPos().offset(offsetDirection);

			BlockEntity neighborBlockEntity = world.getBlockEntity(neighborPos);
			if (neighborBlockEntity instanceof ComponentProvider) {
				Direction neighborDirection = offsetDirection.getOpposite();
				BlockEntityTransferComponent neighborTransferComponent = BlockEntityTransferComponent.get(neighborBlockEntity);

				// Handle Item Siding
				if (this instanceof ExtendedComponentSidedInventoryProvider) {
					if (!getTransferComponent().get(AstromineComponents.ITEM_INVENTORY_COMPONENT).get(offsetDirection).isDefault()) {
						// input
						ItemExtractable neighbor = ItemAttributes.EXTRACTABLE.get(world, neighborPos, SearchOptions.inDirection(offsetDirection));
						ItemInsertable self = ItemAttributes.INSERTABLE.get(world, getPos(), SearchOptions.inDirection(neighborDirection));

						ItemInvUtil.move(neighbor, self, 1);
					}
					if (!getTransferComponent().get(AstromineComponents.ITEM_INVENTORY_COMPONENT).get(offsetDirection).isDefault()) {
						// output
						ItemExtractable self = ItemAttributes.EXTRACTABLE.get(world, getPos(), SearchOptions.inDirection(neighborDirection));
						ItemInsertable neighbor = ItemAttributes.INSERTABLE.get(world, neighborPos, SearchOptions.inDirection(offsetDirection));

						ItemInvUtil.move(self, neighbor, 1);
					}
				}

				// Handle fluid siding
				if (fluidComponent != null && getTransferComponent().get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(offsetDirection).canExtract()) {
					FluidComponent neighborFluidComponent = null;
					if (neighborTransferComponent != null) {
						// Get via astromine siding
						if (neighborTransferComponent.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(neighborDirection).canInsert())
							neighborFluidComponent = FluidComponent.get(neighborBlockEntity);
					}

					if (neighborFluidComponent != null) {
						FluidVolume extractable = fluidComponent.getFirstExtractableVolume(offsetDirection, (volume -> !volume.isEmpty()));

						if (extractable != null && !extractable.isEmpty()) {
							FluidVolume insertable = neighborFluidComponent.getFirstInsertableVolume(neighborDirection, extractable);

							insertable.moveFrom(extractable);
						}
					}
				}

				// Handle energy siding
				if (TransportUtilities.isExtractingEnergy(this, getTransferComponent(), offsetDirection)) {
					if (TransportUtilities.isInsertingEnergy(neighborBlockEntity, neighborTransferComponent, neighborDirection)) {
						energyTransfers.add(new Pair<>(Energy.of(this).side(offsetDirection), Energy.of(neighborBlockEntity).side(neighborDirection)));
					}
				}
			}
		}

		energyTransfers.sort(Comparator.comparing(Pair::getRight, Comparator.comparingDouble(EnergyHandler::getEnergy)));
		for (int i = energyTransfers.size() - 1; i >= 0; i--) {
			Pair<EnergyHandler, EnergyHandler> pair = energyTransfers.get(i);
			EnergyHandler input = pair.getLeft();
			EnergyHandler output = pair.getRight();
			input.into(output).move(Math.max(0, Math.min(input.getMaxOutput() / energyTransfers.size(), Math.min(Math.min(input.getEnergy() / (i + 1), output.getMaxStored() - output.getEnergy()), Math.min(input.getMaxOutput(), output.getMaxInput())))));
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

	public void tickActive() {
		isActive = true;
	}

	public void tickInactive() {
		isActive = false;
	}

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

	public BlockEntityTransferComponent createTransferComponent() {
		return new BlockEntityTransferComponent();
	}

	public BlockEntityRedstoneComponent createRedstoneComponent() {
		return new BlockEntityRedstoneComponent();
	}

	public BlockEntityTransferComponent getTransferComponent() {
		return BlockEntityTransferComponent.get(this);
	}

	public BlockEntityRedstoneComponent getRedstoneComponent() {
		return BlockEntityRedstoneComponent.get(this);
	}
}
