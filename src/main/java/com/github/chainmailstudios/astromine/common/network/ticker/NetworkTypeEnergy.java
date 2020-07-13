package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import net.minecraft.block.HorizontalFacingBlock;
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
		List<EnergyVolume> inputs = Lists.newArrayList();
		List<EnergyVolume> requesterMap = Lists.newArrayList();

		for (NetworkNode memberNode : controller.members) {
			BlockEntity blockEntity = controller.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {

				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);

				EnergyInventoryComponent energyComponent = provider.getSidedComponent(memberNode.getDirection(), AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

				BlockEntityTransferComponent transferComponent = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

				if (energyComponent != null && transferComponent != null) {
					TransferType type = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(memberNode.getDirection(), blockEntity.getCachedState().get(HorizontalFacingBlock.FACING));

					if (type.canExtract()) {
						energyComponent.getContents().forEach((key, volume) -> {
							if (energyComponent.canExtract(memberNode.getDirection(), volume, key)) {
								inputs.add(volume);
							}
						});
					}

					if (type.canInsert()) {
						energyComponent.getContents().forEach((key, volume) -> {
							if (energyComponent.canExtract(memberNode.getDirection(), volume, key)) {
								requesterMap.add(volume);
							}
						});
					}
				}
			}
		}

		for (EnergyVolume input : inputs) {
			for (EnergyVolume output : requesterMap) {
				if (!input.isEmpty() && !output.isFull()) {
					input.pushVolume(output, output.getSize());
				} else if (input.isEmpty()) {
					break;
				}
			}
		}
	}
}
