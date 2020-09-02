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

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.energy.WrappedEnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.handler.EnergyHandler;
import com.github.chainmailstudios.astromine.common.volume.handler.TransferHandler;
import net.minecraft.block.entity.BlockEntity;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.common.collect.Lists;

import java.util.*;

public class EnergyNetworkType extends NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		Map<EnergyVolume, Double> providers = new HashMap<>();
		Map<EnergyVolume, Double> requesters = new HashMap<>();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPos());
			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos);
			BlockEntity blockEntity = memberPos.getBlockEntity();

			WorldPos nodePosition = memberPos.offset(memberNode.getDirection());

			double speedOfMovement = nodePosition.getBlock() instanceof NodeSpeedProvider ? ((NodeSpeedProvider) nodePosition.getBlock()).getNodeSpeed() : 0.0D;

			if (speedOfMovement <= 0) continue;

			if (networkMember.acceptsType(this)) {
				TransferType[] type = { TransferType.NONE };

				TransferHandler.of(blockEntity).ifPresent(handler -> {
					handler.withDirection(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT, memberNode.getDirection(), transferType -> {
						type[0] = transferType;
					});
				});

				Optional<EnergyHandler> optionalHandler = EnergyHandler.ofOptional(blockEntity, memberNode.getDirection());

				if (optionalHandler.isPresent()) {
					optionalHandler.ifPresent(handler -> {
						if (!type[0].isDisabled()) {
							if (type[0].canExtract() || networkMember.isProvider(this)) {
								handler.withFirstExtractable(memberNode.getDirection(), optionalVolume -> {
									optionalVolume.ifPresent(volume -> providers.put(volume, speedOfMovement));
								});
							}

							if (type[0].canInsert() || networkMember.isRequester(this)) {
								handler.withFirstInsertable(memberNode.getDirection(), optionalVolume -> {
									optionalVolume.ifPresent(volume -> requesters.put(volume, speedOfMovement));
								});
							}
						}
					});
				}
			}
		}

		List<EnergyVolume> requesterKeys = Lists.newArrayList(requesters.keySet());
		requesterKeys.sort(Comparator.comparingDouble(EnergyVolume::getAmount));

		for (Map.Entry<EnergyVolume, Double> inputEntry : providers.entrySet()) {
			EnergyVolume input = inputEntry.getKey();

			double inputSpeed = inputEntry.getValue();

			for (int i = requesterKeys.size() - 1; i >= 0; i--) {
				EnergyVolume output = requesterKeys.get(i);

				double outputSpeed = requesters.getOrDefault(output, 0.0D);

				double speed = Collections.min(Arrays.asList(inputSpeed / requesters.size(), outputSpeed / requesters.size(), input.getAmount() / (i + 1), output.getSize() - output.getAmount(), (input instanceof WrappedEnergyVolume ? ((WrappedEnergyVolume) input).getMaximumOutput() : Double.MAX_VALUE), (output instanceof WrappedEnergyVolume ? ((WrappedEnergyVolume) output).getMaximumInput() : Double.MAX_VALUE)));

				input.into(output, speed);
			}
		}
	}

	public interface NodeSpeedProvider {
		double getNodeSpeed();
	}
}
