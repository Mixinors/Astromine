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

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.fluid.FluidAttributes;
import alexiil.mc.lib.attributes.fluid.FluidVolumeUtil;
import alexiil.mc.lib.attributes.fluid.GroupedFluidInv;
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.filter.ExactFluidFilter;
import alexiil.mc.lib.attributes.fluid.filter.ReadableFluidFilter;
import alexiil.mc.lib.attributes.fluid.volume.FluidKey;
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume;
import alexiil.mc.lib.attributes.misc.NullVariant;
import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberNode;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.position.WorldPos;
import com.github.chainmailstudios.astromine.registry.AstromineComponents;
import com.google.common.collect.Lists;
import net.minecraft.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
				if (inv == null || inv instanceof NullVariant)
					continue;

				@Nullable
				BlockEntity blockEntity = memberPos.getBlockEntity();
				TransferType[] type = { TransferType.NONE };


				BlockEntityTransferComponent transferComponent = BlockEntityTransferComponent.get(blockEntity);

				if (transferComponent != null) {
					transferComponent.withDirection(AstromineComponents.FLUID_INVENTORY_COMPONENT, memberNode.getDirection(), transferType -> {
						type[0] = transferType;
					});
				}

				if (!type[0].isNone()) {
					if (type[0].canExtract() && (networkMember.isProvider(this) || networkMember.isBuffer(this))) {
						providers.add(inv);
					}

					if (type[0].canInsert() && (networkMember.isRequester(this) || networkMember.isBuffer(this))) {
						requesters.add(inv);
					}
				}
			}
		}

		for (GroupedFluidInv provider : providers) {
			for (FluidKey providerStoredFluid : provider.getStoredFluids()) {
				if (providerStoredFluid.getRawFluid() == null) continue;
				ReadableFluidFilter fluidFilter = ExactFluidFilter.of(providerStoredFluid);

				List<GroupedFluidInv> requestersFiltered = requesters.stream()
					.filter(inv -> {
						FluidVolume fluidVolume = providerStoredFluid.withAmount(provider.getAmount_F(providerStoredFluid));


						return !inv.attemptInsertion(fluidVolume, Simulation.SIMULATE).equals(fluidVolume);

					})
					.sorted(Comparator.comparing(inv -> inv.getAmount_F(providerStoredFluid)))
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
