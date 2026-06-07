package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.common.config.AMConfig;
import com.github.mixinors.astromine.common.network.Network;
import com.github.mixinors.astromine.common.network.type.base.TransferNetworkType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidNetworkType extends TransferNetworkType<IFluidHandler> {
	@Override
	public IFluidHandler find(Level world, BlockPos pos, @Nullable Direction direction) {
		return world.getCapability(Capabilities.FluidHandler.BLOCK, pos, direction);
	}
	
	@Override
	public void tick(Network<IFluidHandler> network) {
		var extractableStorages = new ArrayList<IFluidHandler>();
		var bufferStorages = new ArrayList<IFluidHandler>();
		var insertableStorages = new ArrayList<IFluidHandler>();
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
	
	private void move(List<IFluidHandler> sources, List<IFluidHandler> destinations) {
		for (var source : sources) {
			var moved = 0L;
			
			for (var sourceTank = 0; sourceTank < source.getTanks() && moved < getTransferRate(); ++sourceTank) {
				var stored = source.getFluidInTank(sourceTank);
				
				if (stored.isEmpty()) {
					continue;
				}
				
				var offered = stored.copyWithAmount((int) Math.min(Math.min(stored.getAmount(), getTransferRate() - moved), Integer.MAX_VALUE));
				var extracted = source.drain(offered, IFluidHandler.FluidAction.SIMULATE);
				
				if (extracted.isEmpty()) {
					continue;
				}
				
				for (var destination : destinations) {
					if (source == destination) {
						continue;
					}
					
					var accepted = destination.fill(extracted, IFluidHandler.FluidAction.SIMULATE);
					
					if (accepted <= 0) {
						continue;
					}
					
					var actuallyExtracted = source.drain(extracted.copyWithAmount(accepted), IFluidHandler.FluidAction.EXECUTE);
					
					if (!actuallyExtracted.isEmpty()) {
						destination.fill(actuallyExtracted, IFluidHandler.FluidAction.EXECUTE);
						moved += actuallyExtracted.getAmount();
					}
					
					break;
				}
			}
		}
	}
	
	@Override
	public long getTransferRate() {
		return AMConfig.get().networks.fluidNetwork.transferRate;
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
