package com.github.chainmailstudios.astromine.common.network.ticker;

import net.minecraft.block.entity.BlockEntity;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.common.collect.Lists;
import java.util.List;

public class NetworkTypeEnergy extends NetworkType {
	@Override
	public void simulate(NetworkInstance controller) {
		List<EnergyVolume> bufferMap = Lists.newArrayList();
		List<EnergyVolume> requesterMap = Lists.newArrayList();

		for (NetworkNode memberNode : controller.members) {
			BlockEntity blockEntity = controller.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {

				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);

				EnergyInventoryComponent energyComponent = provider.getSidedComponent(memberNode.getDirection(), AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

				if (energyComponent != null) {
					NetworkMember member = (NetworkMember) blockEntity;

					if (member.isBuffer(this)) {
						energyComponent.getContents().forEach((key, volume) -> {
							bufferMap.add(volume);
						});
					} else if (member.isRequester(this)) {
						energyComponent.getContents().forEach((key, volume) -> {
							requesterMap.add(volume);
						});
					}
				}
			}
		}

		for (EnergyVolume buffer : bufferMap) {
			for (EnergyVolume requester : requesterMap) {
				if (!buffer.isEmpty() && !requester.isFull()) {
					buffer.pushVolume(requester, requester.getSize());
				} else if (buffer.isEmpty()) {
					break;
				}
			}
		}
	}
}
