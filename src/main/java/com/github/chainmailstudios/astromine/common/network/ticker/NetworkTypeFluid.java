package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class NetworkTypeFluid extends NetworkType {
	@Override
	public void simulate(NetworkInstance controller) {
		Multimap<BlockPos, FluidVolume> bufferMap = HashMultimap.create();
		Multimap<BlockPos, FluidVolume> requesterMap = HashMultimap.create();

		for (NetworkNode memberNode : controller.members) {
			BlockEntity blockEntity = controller.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);

				FluidInventoryComponent fluidComponent = provider.getSidedComponent(null, AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

				NetworkMember member = (NetworkMember) blockEntity;

				if (member.isBuffer(this)) {
					fluidComponent.getContents().forEach((key, volume) -> {
						bufferMap.put(blockEntity.getPos(), volume);
					});
				} else if (member.isRequester(this)) {
					fluidComponent.getContents().forEach((key, volume) -> {
						requesterMap.put(blockEntity.getPos(), volume);
					});
				}
			}
		}

		for (Map.Entry<BlockPos, FluidVolume> buffer : bufferMap.entries()) {
			for (Map.Entry<BlockPos, FluidVolume> requester : requesterMap.entries()) {
				if (!buffer.getValue().isEmpty() && !requester.getValue().isFull() && buffer.getValue().getFluid() == requester.getValue().getFluid()) {
					requester.getValue().pullVolume(buffer.getValue(), Fraction.BUCKET);
				} else if (buffer.getValue().isEmpty()) {
					break;
				}
			}
		}
	}
}
