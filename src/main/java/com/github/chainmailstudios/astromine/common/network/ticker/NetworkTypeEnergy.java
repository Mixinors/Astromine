package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyStorage;

import java.util.Comparator;
import java.util.List;

public class NetworkTypeEnergy extends NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		int speed = 1024;
		List<EnergyHandler> inputs = Lists.newArrayList();
		List<EnergyHandler> requesters = Lists.newArrayList();

		for (NetworkMemberNode memberNode : instance.members) {
			BlockEntity blockEntity = instance.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof EnergyStorage && blockEntity instanceof NetworkMember) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);
				BlockEntityTransferComponent transferComponent = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

				before:
				if (transferComponent != null) {
					Property<Direction> property = blockEntity.getCachedState().contains(HorizontalFacingBlock.FACING) ? HorizontalFacingBlock.FACING : blockEntity.getCachedState().contains(FacingBlock.FACING) ? FacingBlock.FACING : null;

					if (!blockEntity.getCachedState().contains(property)) break before;

					TransferType type = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(memberNode.getDirection(), blockEntity.getCachedState().get(property));
					boolean areAllNone = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).areAllNone();

					if (type != TransferType.DISABLED) {
						if (type.canExtract() || ((NetworkMember) blockEntity).isProvider(this)) {
							inputs.add(Energy.of(blockEntity).side(memberNode.getDirection()));
						}

						if (type.canInsert() || ((NetworkMember) blockEntity).isRequester(this)) {
							requesters.add(Energy.of(blockEntity).side(memberNode.getDirection()));
						}
					}
				}
			}
		}

		requesters.sort(Comparator.comparingDouble(EnergyHandler::getEnergy));
		for (EnergyHandler input : inputs) {
			for (int i = requesters.size() - 1; i >= 0; i--) {
				EnergyHandler output = requesters.get(i);
				input.into(output).move(Math.max(0, Math.min(speed / requesters.size(), Math.min(Math.min(input.getEnergy() / (i + 1), output.getMaxStored() - output.getEnergy()), Math.min(input.getMaxOutput(), output.getMaxInput())))));
			}
		}
	}
}
