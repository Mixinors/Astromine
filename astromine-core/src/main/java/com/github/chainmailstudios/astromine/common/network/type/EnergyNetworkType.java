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

import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import net.minecraft.block.entity.BlockEntity;

import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.ComponentProvider;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.utilities.WorldPos;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;
import team.reborn.energy.EnergyStorage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EnergyNetworkType extends NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		Map<EnergyHandler, Double> inputs = Maps.newLinkedHashMap();
		Map<EnergyHandler, Double> requesters = Maps.newLinkedHashMap();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPos());
			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos);
			BlockEntity blockEntity = memberPos.getBlockEntity();

			double speedOfMovement = 0;
			WorldPos nodePosition = memberPos.offset(memberNode.getDirection());
			if (nodePosition.getBlock() instanceof EnergyNodeSpeedProvider) {
				speedOfMovement = ((EnergyNodeSpeedProvider) nodePosition.getBlock()).getNodeSpeed();
			}

			if (speedOfMovement <= 0)
				continue;

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
						inputs.put(Energy.of(blockEntity).side(memberNode.getDirection()), speedOfMovement);
					}

					if (type.canInsert() || networkMember.isRequester(this)) {
						requesters.put(Energy.of(blockEntity).side(memberNode.getDirection()), speedOfMovement);
					}
				}
			}
		}

		List<EnergyHandler> requesterKeys = Lists.newArrayList(requesters.keySet());
		requesterKeys.sort(Comparator.comparingDouble(EnergyHandler::getEnergy));
		for (Map.Entry<EnergyHandler, Double> inputEntry : inputs.entrySet()) {
			EnergyHandler input = inputEntry.getKey();
			double inputSpeed = inputEntry.getValue();
			for (int i = requesterKeys.size() - 1; i >= 0; i--) {
				EnergyHandler output = requesterKeys.get(i);
				double outputSpeed = requesters.get(output);
				double speed = Collections.min(Arrays.asList(inputSpeed / requesters.size(), outputSpeed / requesters.size(), input.getEnergy() / (i + 1), output.getMaxStored() - output.getEnergy(), input.getMaxOutput(), output.getMaxInput()));
				input.into(output).move(speed);
			}
		}
	}

	public interface EnergyNodeSpeedProvider {
		double getNodeSpeed();
	}
}
