/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.block.entity.base.ExtendedBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.BufferBlockEntity;
import com.github.mixinors.astromine.common.block.entity.storage.TankBlockEntity;
import com.github.mixinors.astromine.common.entity.base.ExtendedEntity;
import com.github.mixinors.astromine.common.manager.BodyManager;
import com.github.mixinors.astromine.common.manager.RocketManager;
import com.github.mixinors.astromine.common.manager.StationManager;
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.fluids.FluidStack;

public class AMNetworking {
	public static final ResourceLocation STORAGE_SIDING_UPDATE = AMCommon.id("storage_siding_update");
	
	public static final ResourceLocation REDSTONE_TYPE_UPDATE = AMCommon.id("redstone_type_update");
	
	public static final ResourceLocation TANK_FILTER_UPDATE = AMCommon.id("tank_filter_update");
	
	public static final ResourceLocation BUFFER_CLEAR = AMCommon.id("buffer_clear");
	
	public static final ResourceLocation ROCKET_SPAWN = AMCommon.id("rocket_spawn");
	
	public static final ResourceLocation SYNC_ENTITY = AMCommon.id("sync_entity");
	
	public static final ResourceLocation SYNC_BODIES = AMCommon.id("sync_bodies");
	
	public static final ResourceLocation SYNC_ROCKETS = AMCommon.id("sync_rockets");
	
	public static final ResourceLocation SYNC_STATIONS = AMCommon.id("sync_stations");
	
	public static void init() {
		AMCommon.modEventBus().addListener(AMNetworking::registerPayloads);
	}
	
	private static void registerPayloads(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar("1");
		
		registrar.playToServer(StorageSidingUpdatePayload.TYPE, StorageSidingUpdatePayload.STREAM_CODEC, AMNetworking::handleStorageSidingUpdate);
		registrar.playToServer(RedstoneTypeUpdatePayload.TYPE, RedstoneTypeUpdatePayload.STREAM_CODEC, AMNetworking::handleRedstoneTypeUpdate);
		registrar.playToServer(TankFilterUpdatePayload.TYPE, TankFilterUpdatePayload.STREAM_CODEC, AMNetworking::handleTankFilterUpdate);
		registrar.playToServer(BufferClearPayload.TYPE, BufferClearPayload.STREAM_CODEC, AMNetworking::handleBufferClear);
		registrar.playToClient(SyncEntityPayload.TYPE, SyncEntityPayload.STREAM_CODEC, AMNetworking::handleSyncEntity);
		registrar.playToClient(SyncBodiesPayload.TYPE, SyncBodiesPayload.STREAM_CODEC, AMNetworking::handleSyncBodies);
		registrar.playToClient(SyncRocketsPayload.TYPE, SyncRocketsPayload.STREAM_CODEC, AMNetworking::handleSyncRockets);
		registrar.playToClient(SyncStationsPayload.TYPE, SyncStationsPayload.STREAM_CODEC, AMNetworking::handleSyncStations);
	}
	
	private static void handleStorageSidingUpdate(StorageSidingUpdatePayload payload, IPayloadContext context) {
		var player = context.player();
		var blockEntity = player.level().getBlockEntity(payload.pos());
		
		if (!(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity)) {
			return;
		}
		
		extendedBlockEntity.setStorageSiding(payload.storageType(), payload.direction(), payload.siding());
	}
	
	private static void handleRedstoneTypeUpdate(RedstoneTypeUpdatePayload payload, IPayloadContext context) {
		var player = context.player();
		var blockEntity = player.level().getBlockEntity(payload.pos());
		
		if (blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
			extendedBlockEntity.setRedstoneControl(payload.control());
			extendedBlockEntity.syncData();
		}
	}
	
	private static void handleTankFilterUpdate(TankFilterUpdatePayload payload, IPayloadContext context) {
		var player = context.player();
		var blockEntity = player.level().getBlockEntity(payload.pos());
		
		if (blockEntity instanceof TankBlockEntity tank) {
			var filter = payload.filter();
			tank.setFilter(filter);
			
			if (tank instanceof TankBlockEntity.Creative) {
				var storage = tank.getFluidStorage().getStorage(TankBlockEntity.FLUID_INPUT_SLOT);
				
				if (filter.isEmpty()) {
					storage.setResource(FluidStack.EMPTY);
				} else {
					storage.setResource(filter.copyWithAmount((int) Math.min(tank.getFluidStorageSize(), Integer.MAX_VALUE)));
				}
			}
			
			tank.setChanged();
			tank.syncData();
		}
	}
	
	private static void handleBufferClear(BufferClearPayload payload, IPayloadContext context) {
		var player = context.player();
		var blockEntity = player.level().getBlockEntity(payload.pos());
		
		if (blockEntity instanceof BufferBlockEntity.Creative buffer && buffer.getItemStorage() != null) {
			buffer.getItemStorage().setItem(0, ItemStack.EMPTY);
			buffer.setChanged();
			buffer.syncData();
		}
	}
	
	private static void handleSyncEntity(SyncEntityPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			var entity = context.player().level().getEntity(payload.entityId());
			
			if (entity instanceof ExtendedEntity extendedEntity && payload.tag() != null) {
				extendedEntity.readFromNbt(payload.tag());
			}
		});
	}
	
	private static void handleSyncBodies(SyncBodiesPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (payload.tag() != null) {
				BodyManager.onSync(payload.tag());
			}
		});
	}
	
	private static void handleSyncRockets(SyncRocketsPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (payload.tag() != null) {
				RocketManager.onSync(payload.tag());
			}
		});
	}
	
	private static void handleSyncStations(SyncStationsPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (payload.tag() != null) {
				StationManager.onSync(payload.tag());
			}
		});
	}
	
	public record StorageSidingUpdatePayload(StorageSiding siding, StorageType storageType, Direction direction, BlockPos pos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<StorageSidingUpdatePayload> TYPE = new CustomPacketPayload.Type<>(STORAGE_SIDING_UPDATE);
		public static final StreamCodec<RegistryFriendlyByteBuf, StorageSidingUpdatePayload> STREAM_CODEC = StreamCodec.ofMember(StorageSidingUpdatePayload::write, StorageSidingUpdatePayload::read);
		
		private static StorageSidingUpdatePayload read(RegistryFriendlyByteBuf buf) {
			return new StorageSidingUpdatePayload(buf.readEnum(StorageSiding.class), buf.readEnum(StorageType.class), buf.readEnum(Direction.class), buf.readBlockPos());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeEnum(siding);
			buf.writeEnum(storageType);
			buf.writeEnum(direction);
			buf.writeBlockPos(pos);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record RedstoneTypeUpdatePayload(RedstoneType control, BlockPos pos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<RedstoneTypeUpdatePayload> TYPE = new CustomPacketPayload.Type<>(REDSTONE_TYPE_UPDATE);
		public static final StreamCodec<RegistryFriendlyByteBuf, RedstoneTypeUpdatePayload> STREAM_CODEC = StreamCodec.ofMember(RedstoneTypeUpdatePayload::write, RedstoneTypeUpdatePayload::read);
		
		private static RedstoneTypeUpdatePayload read(RegistryFriendlyByteBuf buf) {
			return new RedstoneTypeUpdatePayload(buf.readEnum(RedstoneType.class), buf.readBlockPos());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeEnum(control);
			buf.writeBlockPos(pos);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record TankFilterUpdatePayload(FluidStack filter, BlockPos pos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<TankFilterUpdatePayload> TYPE = new CustomPacketPayload.Type<>(TANK_FILTER_UPDATE);
		public static final StreamCodec<RegistryFriendlyByteBuf, TankFilterUpdatePayload> STREAM_CODEC = StreamCodec.ofMember(TankFilterUpdatePayload::write, TankFilterUpdatePayload::read);
		
		private static TankFilterUpdatePayload read(RegistryFriendlyByteBuf buf) {
			return new TankFilterUpdatePayload(FluidStack.OPTIONAL_STREAM_CODEC.decode(buf), buf.readBlockPos());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			FluidStack.OPTIONAL_STREAM_CODEC.encode(buf, filter);
			buf.writeBlockPos(pos);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record BufferClearPayload(BlockPos pos) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<BufferClearPayload> TYPE = new CustomPacketPayload.Type<>(BUFFER_CLEAR);
		public static final StreamCodec<RegistryFriendlyByteBuf, BufferClearPayload> STREAM_CODEC = StreamCodec.ofMember(BufferClearPayload::write, BufferClearPayload::read);
		
		private static BufferClearPayload read(RegistryFriendlyByteBuf buf) {
			return new BufferClearPayload(buf.readBlockPos());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeBlockPos(pos);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record SyncEntityPayload(int entityId, CompoundTag tag) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<SyncEntityPayload> TYPE = new CustomPacketPayload.Type<>(SYNC_ENTITY);
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityPayload> STREAM_CODEC = StreamCodec.ofMember(SyncEntityPayload::write, SyncEntityPayload::read);
		
		private static SyncEntityPayload read(RegistryFriendlyByteBuf buf) {
			return new SyncEntityPayload(buf.readVarInt(), buf.readNbt());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeVarInt(entityId);
			buf.writeNbt(tag);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record SyncBodiesPayload(CompoundTag tag) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<SyncBodiesPayload> TYPE = new CustomPacketPayload.Type<>(SYNC_BODIES);
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncBodiesPayload> STREAM_CODEC = StreamCodec.ofMember(SyncBodiesPayload::write, SyncBodiesPayload::read);
		
		private static SyncBodiesPayload read(RegistryFriendlyByteBuf buf) {
			return new SyncBodiesPayload(buf.readNbt());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeNbt(tag);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record SyncRocketsPayload(CompoundTag tag) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<SyncRocketsPayload> TYPE = new CustomPacketPayload.Type<>(SYNC_ROCKETS);
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncRocketsPayload> STREAM_CODEC = StreamCodec.ofMember(SyncRocketsPayload::write, SyncRocketsPayload::read);
		
		private static SyncRocketsPayload read(RegistryFriendlyByteBuf buf) {
			return new SyncRocketsPayload(buf.readNbt());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeNbt(tag);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
	
	public record SyncStationsPayload(CompoundTag tag) implements CustomPacketPayload {
		public static final CustomPacketPayload.Type<SyncStationsPayload> TYPE = new CustomPacketPayload.Type<>(SYNC_STATIONS);
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncStationsPayload> STREAM_CODEC = StreamCodec.ofMember(SyncStationsPayload::write, SyncStationsPayload::read);
		
		private static SyncStationsPayload read(RegistryFriendlyByteBuf buf) {
			return new SyncStationsPayload(buf.readNbt());
		}
		
		private void write(RegistryFriendlyByteBuf buf) {
			buf.writeNbt(tag);
		}
		
		@Override
		public Type<? extends CustomPacketPayload> type() {
			return TYPE;
		}
	}
}
