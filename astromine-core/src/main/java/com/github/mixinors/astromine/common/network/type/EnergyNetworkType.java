/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.common.network.type;

import com.github.mixinors.astromine.common.component.block.entity.TransferComponent;
import com.github.mixinors.astromine.registry.common.AMComponents;
import net.minecraft.block.entity.BlockEntity;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.NetworkNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import it.unimi.dsi.fastutil.objects.Reference2DoubleOpenHashMap;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHandler;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A {@link NetworkType} for energy.
 */
public final class EnergyNetworkType implements NetworkType {
	/** Override behavior to handle attached {@link EnergyHandler}s,
	 * transferring energy between them.
	 *
	 * Performance is dubious at best.
	 *
	 * Transfer is done through {@link Energy}. */
	@Override
	public void tick(NetworkInstance instance) {
		Reference2DoubleMap<EnergyHandler> providers = new Reference2DoubleOpenHashMap<>();
		Reference2DoubleMap<EnergyHandler> requesters = new Reference2DoubleOpenHashMap<>();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPosition());
			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos, memberNode.getDirection());
			BlockEntity blockEntity = memberPos.getBlockEntity();

			WorldPos nodePosition = memberPos.offset(memberNode.getDirection());

			double speed = nodePosition.getBlock() instanceof NodeSpeedProvider ? ((NodeSpeedProvider) nodePosition.getBlock()).getNodeSpeed() : 0.0D;

			if (speed <= 0)
				continue;

			if (networkMember.acceptsType(this)) {
				TransferType type = TransferType.NONE;

				TransferComponent transferComponent = TransferComponent.get(blockEntity);

				if (transferComponent != null && transferComponent.get(AMComponents.ENERGY_INVENTORY_COMPONENT) != null) {
					type = transferComponent.getEnergy(memberNode.getDirection());
				}

				EnergyHandler volume = Energy.of(blockEntity).side(memberNode.getDirection());

				if (!type.isNone()) {
					if (type.canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
						providers.put(volume, speed);
					}

					if (type.canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
						requesters.put(volume, speed);
					}
				}
			}
		}

		List<EnergyHandler> requesterKeys = Lists.newArrayList(requesters.keySet());
		requesterKeys.sort(Comparator.comparingDouble(EnergyHandler::getEnergy));

		for (Reference2DoubleMap.Entry<EnergyHandler> inputEntry : providers.reference2DoubleEntrySet()) {
			EnergyHandler input = inputEntry.getKey();

			double inputSpeed = inputEntry.getDoubleValue();

			for (int i = requesterKeys.size() - 1; i >= 0; i--) {
				EnergyHandler output = requesterKeys.get(i);

				double outputSpeed = requesters.getOrDefault(output, 0.0D);

				double a = inputSpeed / requesters.size();
				double b = outputSpeed / requesters.size();
				double c = input.getEnergy() / (i + 1);
				double d = output.getMaxStored() - output.getEnergy();
				double e = input.getMaxOutput();
				double f = output.getMaxInput();

				double speed = Collections.min(Arrays.asList(a, b, c, d, e, f));

				input.into(output).move(speed);
			}
		}
	}

	/** Returns this type's string representation.
	 * It will be "Energy". */
	@Override
	public String toString() {
		return "Energy";
	}

	/**
	 * A speed provider for
	 * attached {@link NetworkNode}s.
	 */
	public interface NodeSpeedProvider {
		/** Returns this node's transfer speed. */
		double getNodeSpeed();
	}
}
