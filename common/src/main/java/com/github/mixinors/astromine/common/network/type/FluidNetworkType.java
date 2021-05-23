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

import com.github.mixinors.astromine.common.component.base.TransferComponent;
import com.github.mixinors.astromine.common.component.base.FluidComponent;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import net.minecraft.block.entity.BlockEntity;

import com.github.mixinors.astromine.common.block.transfer.TransferType;
import com.github.mixinors.astromine.common.network.NetworkInstance;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.NetworkMemberNode;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A {@link NetworkType} for fluids.
 */
public final class FluidNetworkType implements NetworkType {
	/** Override behavior to handle attached fluid inventories,
	 * transferring energy between them. */
	@Override
	public void tick(NetworkInstance instance) {
		List<Pair<FluidComponent, Direction>> providers = Lists.newArrayList();
		List<Pair<FluidComponent, Direction>> requesters = Lists.newArrayList();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPosition());

			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos, memberNode.getDirection());

			if (networkMember.acceptsType(this)) {
				BlockEntity blockEntity = memberPos.getBlockEntity();

				FluidComponent fluidComponent = FluidComponent.from(blockEntity);

				TransferType type = TransferType.NONE;

				TransferComponent transferComponent = TransferComponent.from(blockEntity);

				if (transferComponent != null) {
					type = transferComponent.getFluid(memberNode.getDirection());
				}

				if (!type.isNone()) {
					if (type.canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
						providers.add(new Pair<>(fluidComponent, memberNode.getDirection()));
					}

					if (type.canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
						requesters.add(new Pair<>(fluidComponent, memberNode.getDirection()));
					}
				}
			}
		}

		for (Pair<FluidComponent, Direction> provider : providers) {
			for (Pair<FluidComponent, Direction> requester : requesters) {
				provider.getLeft().into(requester.getLeft(), FluidVolume.getTransfer(), requester.getRight().getOpposite());
			}
		}
	}

	/** Returns this type's string representation.
	 * It will be "Fluid". */
	@Override
	public String toString() {
		return "Fluid";
	}
}