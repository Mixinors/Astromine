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
import com.github.mixinors.astromine.common.network.type.base.TransferNetworkType;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("UnstableApiUsage")
public class ItemNetworkType extends TransferNetworkType<ItemVariant> {
	@Override
	public Storage<ItemVariant> find(World world, BlockPos pos, @Nullable Direction direction) {
		return ItemStorage.SIDED.find(world, pos, direction);
	}
	
	@Override
	public void tick(Network<Storage<ItemVariant>> network) {
		var world = network.getWorld();
		
		var extractableStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		var bufferStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		var insertableStorages = new Long2ObjectLinkedOpenHashMap<Storage<ItemVariant>>();
		
		var toRemove = new ArrayList<Network.Member>();
		
		for (var member : network.getMembers()) {
			var storage = find(world, member.blockPos(), member.direction());
			
			if (storage == null) {
				toRemove.add(member);
				
				world.getBlockState(member.blockPos()).neighborUpdate(world, member.blockPos(), world.getBlockState(member.blockPos()).getBlock(), member.blockPos(), false);
			} else {
				switch (member.siding()) {
					case INSERT -> {
						if (storage.supportsInsertion()) {
							insertableStorages.put(member.blockPos().asLong(), storage);
						}
					}
					
					case EXTRACT -> {
						if (storage.supportsExtraction()) {
							extractableStorages.put(member.blockPos().asLong(), storage);
						}
					}
					
					case INSERT_EXTRACT -> {
						if (storage.supportsInsertion() && storage.supportsExtraction()) {
							bufferStorages.put(member.blockPos().asLong(), storage);
						} else if (storage.supportsInsertion()) {
							insertableStorages.put(member.blockPos().asLong(), storage);
						} else if (storage.supportsExtraction()) {
							extractableStorages.put(member.blockPos().asLong(), storage);
						}
					}
				}
			}
		}
		
		network.getMembers().removeAll(toRemove);
		
		move(network, extractableStorages, insertableStorages);
		move(network, extractableStorages, bufferStorages);
		move(network, bufferStorages, insertableStorages);
		move(network, bufferStorages, bufferStorages);
	}
	
	@Override
	public long getTransferRate() {
		return AMConfig.get().networks.itemNetwork.transferRate;
	}
	
	@Override
	public boolean hasSiding() {
		return true;
	}
	
	@Override
	public boolean hasFiltering() {
		return true;
	}
}