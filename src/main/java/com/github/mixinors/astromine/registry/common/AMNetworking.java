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
import com.github.mixinors.astromine.common.transfer.RedstoneType;
import com.github.mixinors.astromine.common.transfer.StorageSiding;
import com.github.mixinors.astromine.common.transfer.StorageType;
import dev.architectury.networking.NetworkManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class AMNetworking {
	public static final Identifier STORAGE_SIDING_UPDATE = AMCommon.id("storage_siding_update");
	
	public static final Identifier REDSTONE_TYPE_UPDATE = AMCommon.id("redstone_type_update");
	
	public static final Identifier ROCKET_SPAWN = AMCommon.id("rocket_spawn");
	
	public static final Identifier SYNC_ENTITY = AMCommon.id("sync_entity");
	
	public static final Identifier SYNC_BODIES = AMCommon.id("sync_bodies");
	
	public static void init() {
		NetworkManager.registerReceiver(NetworkManager.c2s(), STORAGE_SIDING_UPDATE, ((buf, context) -> {
			buf.retain();
			
			var siding = buf.readEnumConstant(StorageSiding.class);
			var type = buf.readEnumConstant(StorageType.class);
			var direction = buf.readEnumConstant(Direction.class);
			var pos = buf.readBlockPos();
			
			context.queue(() -> {
				var blockEntity = (ExtendedBlockEntity) context.getPlayer().world.getBlockEntity(pos);
				
				var sidings = (StorageSiding[]) null;
				
				if (type == StorageType.ITEM) {
					sidings = blockEntity.getItemStorage().getSidings();
				}
				
				if (type == StorageType.FLUID) {
					sidings = blockEntity.getFluidStorage().getSidings();
				}
				
				sidings[direction.ordinal()] = siding;
				
				blockEntity.syncData();
			});
		}));
		
		NetworkManager.registerReceiver(NetworkManager.c2s(), REDSTONE_TYPE_UPDATE, ((buf, context) -> {
			buf.retain();
			
			var control = buf.readEnumConstant(RedstoneType.class);
			var pos = buf.readBlockPos();
			
			context.queue(() -> {
				var blockEntity = (ExtendedBlockEntity) context.getPlayer().world.getBlockEntity(pos);
				
				blockEntity.setRedstoneControl(control);
				
				blockEntity.syncData();
			});
		}));
	}
}
