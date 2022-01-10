/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;

public final class EnergyNetworkType implements NetworkType {
	@Override
	public void tick(NetworkInstance instance) {
		// TODO: Rewrite this.
		
//		var providers = new Reference2DoubleOpenHashMap<>();
//		var requesters = new Reference2DoubleOpenHashMap<>();
//
//		for (var memberNode : instance.members) {
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
//		var requesterKeys = Lists.newArrayList(requesters.keySet());
//		requesterKeys.sort(Comparator.comparingDouble(EnergyHandler::getEnergy));
//
//		for (var inputEntry : providers.reference2DoubleEntrySet()) {
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
