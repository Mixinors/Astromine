package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class NetworkTypeEnergy extends NetworkType {
	@Override
	public void simulate(NetworkInstance controller) {
		Map<BlockPos, EnergyVolume> bufferMap = new HashMap<>();
		Map<BlockPos, EnergyVolume> requesterMap = new HashMap<>();

		for (NetworkNode memberNode : controller.members) {
			BlockEntity blockEntity = controller.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {

				ComponentProvider provider = (ComponentProvider) blockEntity;

				EnergyInventoryComponent energyComponent = provider.getComponent(null, EnergyInventoryComponent.class);

				NetworkMember member = (NetworkMember) blockEntity;

				if (member.isBuffer()) {
					energyComponent.getContents().forEach((key, volume) -> {
						bufferMap.put(blockEntity.getPos(), volume);
					});
				} else if (member.isRequester()) {
					energyComponent.getContents().forEach((key, volume) -> {
						requesterMap.put(blockEntity.getPos(), volume);
					});
				}
			}
		}

		for (Map.Entry<BlockPos, EnergyVolume> buffer : bufferMap.entrySet()) {
			for (Map.Entry<BlockPos, EnergyVolume> requester : requesterMap.entrySet()) {
				if (!buffer.getValue().isEmpty() && !requester.getValue().isFull()) {
					buffer.getValue().pushVolume(requester.getValue(), requester.getValue().getSize());
				} else if (buffer.getValue().isEmpty()) {
					break;
				}
			}
		}
	}
}
