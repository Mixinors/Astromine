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

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.common.volume.fraction.Fraction;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FluidNetworkType extends NetworkType {
	private Fraction speed = Fraction.bottle();

	@Override
	public void tick(NetworkInstance instance) {
		Map<FluidComponent, Direction> providers = new HashMap<>();
		Map<FluidComponent, Direction> requesters = new HashMap<>();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPos());
			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos, memberNode.getDirection());

			if (networkMember.acceptsType(this)) {
				FluidComponent fluidComponent = FluidComponent.get(memberPos.getBlockEntity());

				if (fluidComponent == null)
					continue;

				@Nullable
				BlockEntity blockEntity = memberPos.getBlockEntity();
				TransferType type = TransferType.NONE;

				BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(blockEntity);

				if (transferComponent != null && transferComponent.get(AstromineComponents.FLUID_INVENTORY_COMPONENT) != null) {
					type = transferComponent.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(memberNode.getDirection());
				}

				if (!type.isNone()) {
					if (type.canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
						providers.put(fluidComponent, memberNode.getDirection());
					}

					if (type.canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
						requesters.put(fluidComponent, memberNode.getDirection());
					}
				}
			}
		}

		for (Map.Entry<FluidComponent, Direction> providerEntry : providers.entrySet()) {
			for (FluidVolume provider : providerEntry.getKey().getExtractableVolumes(providerEntry.getValue())) {
				Fraction provided = Fraction.empty();

				for (Map.Entry<FluidComponent, Direction> requesterEntry : requesters.entrySet()) {
					for (FluidVolume requester : requesterEntry.getKey().getInsertableVolumes(requesterEntry.getValue(), provider.withAmount(Fraction.minimum()))) {
						if (provided.smallerThan(speed)) {
							Fraction then = requester.getAmount();
							requester.moveFrom(provider, speed);
							provided = requester.getAmount().subtract(then);
						} else {
							break;
						}
					}
				}
			}
		}
	}
}

/*
	As per DanielShe's requests,
public class FluidNetworkType extends NetworkType {
	private FluidAmount inputSpeed = FluidAmount.of(1, 20);
	private FluidAmount outputSpeed = FluidAmount.of(1, 20);

	@Override
	public void tick(NetworkInstance instance) {
		List<GroupedFluidInv> providers = Lists.newArrayList();
		List<GroupedFluidInv> requesters = Lists.newArrayList();

		for (NetworkMemberNode memberNode : instance.members) {
			WorldPos memberPos = WorldPos.of(instance.getWorld(), memberNode.getBlockPos());
			NetworkMember networkMember = NetworkMemberRegistry.get(memberPos, memberNode.getDirection());

			if (networkMember.acceptsType(this)) {
				GroupedFluidInv inv = FluidAttributes.GROUPED_INV.get(memberPos.getWorld(), memberPos.getBlockPos(), SearchOptions.inDirection(memberNode.getDirection().getOpposite()));
				if (inv instanceof NullVariant)
					continue;

				@Nullable
				BlockEntity blockEntity = memberPos.getBlockEntity();
				TransferType type = TransferType.NONE;

				BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(blockEntity);

				if (transferComponent != null) {
					type = transferComponent.get(AstromineComponents.FLUID_INVENTORY_COMPONENT).get(memberNode.getDirection());
				}

				if (!type.isNone()) {
					if (type.canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
						providers.add(inv);
					}

					if (type.canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
						requesters.add(inv);
					}
				}
			}
		}

		for (GroupedFluidInv provider : providers) {
			for (FluidKey providerStoredFluid : provider.getStoredFluids()) {
				if (providerStoredFluid.getRawFluid() == null) continue;

				List<GroupedFluidInv> requestersFiltered = requesters.stream()
					.filter(inventory -> {
						FluidVolume fluidVolume = providerStoredFluid.withAmount(provider.getAmount_F(providerStoredFluid));

						return !inventory.attemptInsertion(fluidVolume, Simulation.SIMULATE).equals(fluidVolume);
					})
					.sorted(Comparator.comparing(inventory -> inventory.getAmount_F(providerStoredFluid)))
					.collect(Collectors.toList());

				for (int i = requestersFiltered.size() - 1; i >= 0; i--) {
					GroupedFluidInv inv = requestersFiltered.get(i);

					FluidAmount speed = Collections.min(Arrays.asList(
						inputSpeed.div(requestersFiltered.size()),
						outputSpeed.div(requestersFiltered.size()),
						provider.getAmount_F(providerStoredFluid).div(i + 1),
						inv.getCapacity_F(providerStoredFluid).sub(inv.getAmount_F(providerStoredFluid))
					));

					FluidVolumeUtil.move(provider, inv, speed);
				}
			}
		}
	}
}
 */