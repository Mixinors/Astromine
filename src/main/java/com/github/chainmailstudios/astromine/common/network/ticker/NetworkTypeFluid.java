package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;

import java.util.List;

public class NetworkTypeFluid extends NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		List<FluidVolume> inputs = Lists.newArrayList();
		List<FluidVolume> outputs = Lists.newArrayList();

		for (NetworkMemberNode memberNode : instance.members) {
			BlockEntity blockEntity = instance.getWorld().getBlockEntity(memberNode.getBlockPos());

			if (blockEntity instanceof ComponentProvider && blockEntity instanceof NetworkMember) {
				ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);

				FluidInventoryComponent fluidComponent = provider.getSidedComponent(memberNode.getDirection(), AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

				BlockEntityTransferComponent transferComponent = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

				before:
				if (fluidComponent != null && transferComponent != null) {
					Property<Direction> property = blockEntity.getCachedState().contains(HorizontalFacingBlock.FACING) ? HorizontalFacingBlock.FACING : blockEntity.getCachedState().contains(FacingBlock.FACING) ? FacingBlock.FACING : null;

					if (!blockEntity.getCachedState().contains(property)) break before;

					TransferType type = transferComponent.get(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).get(memberNode.getDirection());

					if (type != TransferType.DISABLED) {
						if (type.canExtract() || ((NetworkMember) blockEntity).isProvider(this)) {
							fluidComponent.getContents().forEach((key, volume) -> {
								if (fluidComponent.canExtract(memberNode.getDirection(), volume, key)) {
									inputs.add(volume);
								}
							});
						}

						if (type.canInsert() || ((NetworkMember) blockEntity).isRequester(this)) {
							fluidComponent.getContents().forEach((key, volume) -> {
								if (fluidComponent.canExtract(memberNode.getDirection(), volume, key)) {
									outputs.add(volume);
								}
							});
						}
					}
				}
			}
		}

		for (FluidVolume input : inputs) {
			for (FluidVolume output : outputs) {
				if (!input.isEmpty() && !output.isFull() && (input.getFluid() == output.getFluid() || output.isEmpty())) {
					output.pullVolume(input, Fraction.BUCKET);
				} else if (input.isEmpty()) {
					break;
				}
			}
		}
	}
}
