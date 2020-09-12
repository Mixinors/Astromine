/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
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

package com.github.chainmailstudios.astromine.common.network.type;

import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.SidedComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.common.collect.Lists;
import java.util.List;

public class FluidNetworkType extends NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		List<FluidVolume> inputs = Lists.newArrayList();
		List<Pair<FluidInventoryComponent, Direction>> outputs = Lists.newArrayList();

		for (NetworkMemberNode memberNode : instance.members) {
			BlockEntity blockEntity = instance.getWorld().getBlockEntity(memberNode.getBlockPos());
			NetworkMember networkMember = NetworkMemberRegistry.get(blockEntity);

			if (blockEntity instanceof SidedComponentProvider && networkMember.acceptsType(this)) {
				SidedComponentProvider provider = SidedComponentProvider.fromBlockEntity(blockEntity);

				FluidInventoryComponent fluidComponent = provider.getSidedComponent(memberNode.getDirection(), AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

				BlockEntityTransferComponent transferComponent = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);

				before:
				if (fluidComponent != null && transferComponent != null) {
					Property<Direction> property = blockEntity.getCachedState().contains(HorizontalFacingBlock.FACING) ? HorizontalFacingBlock.FACING : blockEntity.getCachedState().contains(FacingBlock.FACING) ? FacingBlock.FACING : null;

					if (!blockEntity.getCachedState().contains(property))
						break before;

					TransferType type = transferComponent.get(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT).get(memberNode.getDirection());

					if (!type.isDisabled()) {
						if (type.canExtract() || networkMember.isProvider(this)) {
							inputs.addAll(fluidComponent.getExtractableContentsMatching(memberNode.getDirection(), (volume -> !volume.isEmpty())));
						}

						if (type.canInsert() || networkMember.isRequester(this)) {
							outputs.add(new Pair<>(fluidComponent, memberNode.getDirection()));
						}
					}
				}
			}
		}

		for (FluidVolume input : inputs) {
			for (Pair<FluidInventoryComponent, Direction> outputPair : outputs) {
				FluidInventoryComponent component = outputPair.getLeft();
				Direction direction = outputPair.getRight();

				component.getContents().forEach((slot, output) -> {
					if (!input.isEmpty() && !output.isFull() && (input.getFluid() == output.getFluid() || output.isEmpty())) {
						if (component.canInsert(direction, input, slot)) {
							output.moveFrom(input, Fraction.bottle());
						}
					}
				});
			}
		}
	}
}
