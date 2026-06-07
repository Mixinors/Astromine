package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.TransferNetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemNetworkType extends TransferNetworkType<IItemHandler> {
	@Override
	public IItemHandler find(Level world, BlockPos pos, @Nullable Direction direction) {
		return world.getCapability(Capabilities.ItemHandler.BLOCK, pos, direction);
	}
	
	@Override
	public void tick(Network<IItemHandler> network) {
		var extractableStorages = new ArrayList<IItemHandler>();
		var bufferStorages = new ArrayList<IItemHandler>();
		var insertableStorages = new ArrayList<IItemHandler>();
		var world = network.getWorld();
		
		for (var member : network.getMembers()) {
			var storage = find(world, member.blockPos(), member.direction());
			
			if (storage == null) {
				continue;
			} else {
				switch (member.siding()) {
					case INSERT -> insertableStorages.add(storage);
					case EXTRACT -> extractableStorages.add(storage);
					case INSERT_EXTRACT -> bufferStorages.add(storage);
				}
			}
		}
		
		move(extractableStorages, insertableStorages);
		move(extractableStorages, bufferStorages);
		move(bufferStorages, insertableStorages);
		move(bufferStorages, bufferStorages);
	}
	
	private void move(List<IItemHandler> sources, List<IItemHandler> destinations) {
		for (var source : sources) {
			var moved = 0L;
			
			for (var sourceSlot = 0; sourceSlot < source.getSlots() && moved < getTransferRate(); ++sourceSlot) {
				var extracted = source.extractItem(sourceSlot, (int) Math.min(getTransferRate() - moved, Integer.MAX_VALUE), true);
				
				if (extracted.isEmpty()) {
					continue;
				}
				
				for (var destination : destinations) {
					if (source == destination) {
						continue;
					}
					
					var remainder = insert(destination, extracted, true);
					var accepted = extracted.getCount() - remainder.getCount();
					
					if (accepted <= 0) {
						continue;
					}
					
					var actuallyExtracted = source.extractItem(sourceSlot, accepted, false);
					
					if (!actuallyExtracted.isEmpty()) {
						insert(destination, actuallyExtracted, false);
						moved += actuallyExtracted.getCount();
					}
					
					break;
				}
			}
		}
	}
	
	private static ItemStack insert(IItemHandler destination, ItemStack stack, boolean simulate) {
		var remainder = stack.copy();
		
		for (var slot = 0; slot < destination.getSlots() && !remainder.isEmpty(); ++slot) {
			remainder = destination.insertItem(slot, remainder, simulate);
		}
		
		return remainder;
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
