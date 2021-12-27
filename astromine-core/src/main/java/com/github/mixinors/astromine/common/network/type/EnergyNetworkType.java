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

public final class EnergyNetworkType implements NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		// TODO: Rewrite this.
		
//		Reference2DoubleMap<EnergyHandler> providers = new Reference2DoubleOpenHashMap<>();
//		Reference2DoubleMap<EnergyHandler> requesters = new Reference2DoubleOpenHashMap<>();
//
//		for (NetworkMemberNode memberNode : instance.members) {
//			var memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPosition());
//			var networkMember = NetworkMemberRegistry.get(memberPos, memberNode.getDirection());
//			var blockEntity = memberPos.getBlockEntity();
//
//			var nodePosition = memberPos.offset(memberNode.getDirection());
//
//			var speed = nodePosition.getBlock() instanceof NodeSpeedProvider ? ((NodeSpeedProvider) nodePosition.getBlock()).getNodeSpeed() : 0.0D;
//
//			if (speed <= 0)
//				continue;
//
//			if (networkMember.acceptsType(this)) {
//				var type = TransferType.NONE;
//
//				var transferComponent = TransferComponent.get(blockEntity);
//
//				if (transferComponent != null && transferComponent.get(AMComponents.ENERGY_INVENTORY_COMPONENT) != null) {
//					type = transferComponent.getEnergy(memberNode.getDirection());
//				}
//
//				var volume = Energy.of(blockEntity).side(memberNode.getDirection());
//
//				if (!type.isNone()) {
//					if (type.canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
//						providers.put(volume, speed);
//					}
//
//					if (type.canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
//						requesters.put(volume, speed);
//					}
//				}
//			}
//		}
//
//		List<EnergyHandler> requesterKeys = Lists.newArrayList(requesters.keySet());
//		requesterKeys.sort(Comparator.comparingDouble(EnergyHandler::getEnergy));
//
//		for (Reference2DoubleMap.Entry<EnergyHandler> inputEntry : providers.reference2DoubleEntrySet()) {
//			var input = inputEntry.getKey();
//
//			var inputSpeed = inputEntry.getDoubleValue();
//
//			for (var i = requesterKeys.size() - 1; i >= 0; i--) {
//				var output = requesterKeys.get(i);
//
//				var outputSpeed = requesters.getOrDefault(output, 0.0D);
//
//				var a = inputSpeed / requesters.size();
//				var b = outputSpeed / requesters.size();
//				var c = input.getEnergy() / (i + 1);
//				var d = output.getMaxStored() - output.getEnergy();
//				var e = input.getMaxOutput();
//				var f = output.getMaxInput();
//
//				var speed = Collections.min(Arrays.asList(a, b, c, d, e, f));
//
//				input.into(output).move(speed);
//			}
//		}
	}
	
	@Override
	public String toString() {
		return "Energy";
	}
	
	public interface NodeSpeedProvider {
		double getNodeSpeed();
	}
}
