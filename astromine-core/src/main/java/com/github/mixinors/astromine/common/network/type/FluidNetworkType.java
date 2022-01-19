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

package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FluidNetworkType implements NetworkType<Storage<FluidVariant>> {
	@Override
	public Storage<FluidVariant> find(WorldPos pos, @Nullable Direction direction) {
		return FluidStorage.SIDED.find(pos.getWorld(), pos.getBlockPos(), direction);
	}
	
	private void move(List<Storage<FluidVariant>> extractableStorages, List<Storage<FluidVariant>> insertableStorages) {
		try (var transaction = Transaction.openOuter()) {
			for (var extractableStorage : extractableStorages) {
				for (var insertableStorage : insertableStorages) {
					StorageUtil.move(extractableStorage, insertableStorage, ($) -> true, getTransferRate(), transaction);
				}
			}
			
			transaction.commit();
		}
	}
	
	@Override
	public void tick(Network instance) {
		var world = instance.getWorld();
		
		var extractableStorages = new ArrayList<Storage<FluidVariant>>();
		var bufferStorages = new ArrayList<Storage<FluidVariant>>();
		var insertableStorages = new ArrayList<Storage<FluidVariant>>();
		
		// First, we extract from extractableStorages into insertableStorages.
		// Then, we extract from extractableStorages into bufferStorages.
		// Then, we extract from bufferStorages into insertableStorages.
		
		// We ignore how much has been moved for the sake of simplicity.
		
		// We must also remove inaccessible members.
		var membersToRemove = new ArrayList<Network.Member>();
		
		for (var memberNode : instance.members) {
			var member = (Network.Member) memberNode;
			
			var storage = find(new WorldPos(world, member.blockPos()), member.direction());
			
			if (storage == null) {
				membersToRemove.add(member);
				
				world.getBlockState(member.blockPos()).neighborUpdate(world, member.blockPos(), world.getBlockState(member.blockPos()).getBlock(), member.blockPos(), false);
				
				continue;
			}
			
			if (storage.supportsInsertion() && storage.supportsExtraction()) {
				bufferStorages.add(storage);
			} else if (storage.supportsInsertion()) {
				insertableStorages.add(storage);
			} if (storage.supportsExtraction()) {
				extractableStorages.add(storage);
			}
		}
		
		membersToRemove.forEach(instance::removeMember);
		
		move(extractableStorages, insertableStorages);
		move(extractableStorages, bufferStorages);
		move(bufferStorages, insertableStorages);
	}
	
	@Override
	public long getTransferRate() {
		return AMConfig.get().fluidNetworkTransferRate;
	}
}