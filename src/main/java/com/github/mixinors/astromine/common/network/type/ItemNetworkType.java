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

package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("UnstableApiUsage")
public final class ItemNetworkType implements TransferNetworkType<ItemVariant> {
	@Override
	public Storage<ItemVariant> find(WorldPos pos, @Nullable Direction direction) {
		return ItemStorage.SIDED.find(pos.getWorld(), pos.getBlockPos(), direction);
	}
	
	@Override
	public void tick(Network instance) {
		var world = instance.getWorld();
		
		var extractableStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		var bufferStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		var insertableStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		
		// First, we extract from extractableStorages into insertableStorages.
		// Then, we extract from extractableStorages into bufferStorages.
		// Then, we extract from bufferStorages into insertableStorages.
		
		// We ignore how much has been moved for the sake of simplicity.
		
		// We must also remove inaccessible members.
		var membersToRemove = new ArrayList<Network.Member>();
		
		for (var memberNode : instance.members) {
			var member = (Network.Member) memberNode;
			
			var storage = find(new WorldPos(world, member.getBlockPos()), member.getDirection());
			
			if (storage == null) {
				membersToRemove.add(member);
				
				world.getBlockState(member.getBlockPos()).neighborUpdate(world, member.getBlockPos(), world.getBlockState(member.getBlockPos()).getBlock(), member.getBlockPos(), false);
				
				continue;
			}
			
			switch (member.getSiding()) {
				case INSERT -> {
					if (storage.supportsInsertion()) {
						insertableStorages.put(member.getBlockPos().asLong(), storage);
					}
				}
				
				case EXTRACT -> {
					if (storage.supportsExtraction()) {
						extractableStorages.put(member.getBlockPos().asLong(), storage);
					}
				}
				
				case INSERT_EXTRACT -> {
					if (storage.supportsInsertion() && storage.supportsExtraction()) {
						bufferStorages.put(member.getBlockPos().asLong(), storage);
					} else if (storage.supportsInsertion()) {
						insertableStorages.put(member.getBlockPos().asLong(), storage);
					} else if (storage.supportsExtraction()) {
						extractableStorages.put(member.getBlockPos().asLong(), storage);
					}
				}
			}
		}
		
		membersToRemove.forEach(instance::removeMember);
		
		move(instance, extractableStorages, insertableStorages);
		move(instance, extractableStorages, bufferStorages);
		move(instance, bufferStorages, insertableStorages);
		move(instance, bufferStorages, bufferStorages);
	}
	
	@Override
	public long getTransferRate() {
		return AMConfig.get().networks.itemNetwork.transferRate;
	}
	
	@Override
	public boolean hasSiding() {
		return true;
	}
}