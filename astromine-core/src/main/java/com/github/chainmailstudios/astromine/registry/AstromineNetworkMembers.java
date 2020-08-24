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

package com.github.chainmailstudios.astromine.registry;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.utilities.EnergyCapacityProvider;
import com.github.chainmailstudios.astromine.common.utilities.WorldPos;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AstromineNetworkMembers {
	protected static final Map<Predicate<Block>, Consumer<Block>> BLOCK_CONSUMER = Maps.newHashMap();

	public static void initialize() {
		Energy.registerHolder(object -> {
			if (object instanceof ItemStack) {
				return !((ItemStack) object).isEmpty() && ((ItemStack) object).getItem() instanceof EnergyCapacityProvider && ((EnergyCapacityProvider) ((ItemStack) object).getItem()).isCreative();
			}
			return false;
		}, object -> new EnergyStorage() {
			@Override
			public double getStored(EnergySide face) {
				return Integer.MAX_VALUE;
			}

			@Override
			public void setStored(double amount) {

			}

			@Override
			public double getMaxStoredPower() {
				return Integer.MAX_VALUE;
			}

			@Override
			public EnergyTier getTier() {
				return EnergyTier.INFINITE;
			}
		});
		NetworkMemberRegistry.INSTANCE.register(AstromineNetworkTypes.ENERGY, new NetworkMemberRegistry.NetworkTypeRegistryImpl<NetworkType>() {
			@Override
			public Collection<NetworkMemberType> get(WorldPos pos) {
				if (!this.types.containsKey(pos.getBlock())) {
					BlockEntity blockEntity = pos.getBlockEntity();
					if (blockEntity instanceof EnergyStorage) {
						return NetworkMember.REQUESTER_PROVIDER;
					}
				}
				return super.get(pos);
			}
		});

		NetworkMemberRegistry.INSTANCE.register(AstromineNetworkTypes.ITEM, new NetworkMemberRegistry.NetworkTypeRegistryImpl<NetworkType>() {
			@Override
			public Collection<NetworkMemberType> get(WorldPos pos) {
				if (!this.types.containsKey(pos.getBlock())) {
					BlockEntity blockEntity = pos.getBlockEntity();
					if (blockEntity instanceof InventoryProvider) {
						return NetworkMember.REQUESTER_PROVIDER;
					}
				}
				return super.get(pos);
			}
		});

		FabricLoader.getInstance().getEntrypoints("astromine-network-members", Runnable.class).forEach(Runnable::run);

		Registry.BLOCK.getEntries().forEach(entry -> acceptBlock(entry.getKey(), entry.getValue()));
		RegistryEntryAddedCallback.event(Registry.BLOCK).register((index, identifier, block) -> acceptBlock(RegistryKey.of(Registry.BLOCK_KEY, identifier), block));
	}

	public static void acceptBlock(RegistryKey<Block> id, Block block) {
		if (id.getValue().getNamespace().equals("astromine")) {
			for (Map.Entry<Predicate<Block>, Consumer<Block>> blockConsumerEntry : BLOCK_CONSUMER.entrySet()) {
				if (blockConsumerEntry.getKey().test(block)) {
					blockConsumerEntry.getValue().accept(block);
					break;
				}
			}
		}
	}
}
