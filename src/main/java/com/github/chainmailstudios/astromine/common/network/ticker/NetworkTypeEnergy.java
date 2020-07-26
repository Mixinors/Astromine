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
package com.github.chainmailstudios.astromine.common.network.ticker;

import com.github.chainmailstudios.astromine.client.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.google.common.collect.Lists;
import net.minecraft.block.entity.BlockEntity;
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
			NetworkMember networkMember = NetworkMemberRegistry.get(blockEntity);

			if (blockEntity instanceof EnergyStorage && networkMember.acceptsType(this)) {
				TransferType type = TransferType.NONE;

				if (blockEntity instanceof ComponentProvider) {
					ComponentProvider provider = ComponentProvider.fromBlockEntity(blockEntity);
					BlockEntityTransferComponent transferComponent = provider.getComponent(AstromineComponentTypes.BLOCK_ENTITY_TRANSFER_COMPONENT);
					if (transferComponent != null) {
						type = transferComponent.get(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT).get(memberNode.getDirection());
					}
				}

				if (!type.isDisabled()) {
					if (type.canExtract() || networkMember.isProvider(this)) {
						inputs.add(Energy.of(blockEntity).side(memberNode.getDirection()));
					}

					if (type.canInsert() || networkMember.isRequester(this)) {
						requesters.add(Energy.of(blockEntity).side(memberNode.getDirection()));
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
