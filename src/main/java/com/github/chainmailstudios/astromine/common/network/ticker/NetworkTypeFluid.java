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
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkTypeFluid extends NetworkType {
	@Override
	public void simulate(NetworkInstance controller) {
		List<FluidVolume> buffers = Lists.newArrayList();
		List<FluidVolume> requesters = Lists.newArrayList();

		for (NetworkNode memberNode : controller.members) {
			BlockEntity blockEntity = controller.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);

				FluidInventoryComponent fluidComponent = provider.getSidedComponent(memberNode.getDirection(), AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

				NetworkMember member = (NetworkMember) blockEntity;

				if (member.isBuffer()) {
					fluidComponent.getContents().forEach((key, volume) -> {
						buffers.add(volume);
					});
				} else if (member.isRequester()) {
					fluidComponent.getContents().forEach((key, volume) -> {
						requesters.add(volume);
					});
				}
			}
		}

		for (FluidVolume buffer : buffers) {
			for (FluidVolume requester : requesters) {
				if (!buffer.isEmpty() && !requester.isFull() && buffer.getFluid() == requester.getFluid()) {
					requester.pullVolume(buffer, Fraction.BUCKET);
				} else if (buffer.isEmpty()) {
					break;
				}
			}
		}
	}
}
