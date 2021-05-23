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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.component.general.provider.EnergyComponentProvider;
import com.github.mixinors.astromine.common.component.general.provider.FluidComponentProvider;
import com.github.mixinors.astromine.common.component.general.provider.ItemComponentProvider;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.targets.ArchitecturyTarget;
import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import com.github.mixinors.astromine.common.network.NetworkBlock;
import com.github.mixinors.astromine.common.network.NetworkMember;
import com.github.mixinors.astromine.common.network.NetworkMemberType;
import com.github.mixinors.astromine.common.network.type.base.NetworkType;
import com.github.mixinors.astromine.common.registry.NetworkMemberRegistry;
import com.github.mixinors.astromine.common.util.data.position.WorldPos;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AMNetworkMembers {
	protected static final Map<Predicate<Block>, Consumer<Block>> BLOCK_CONSUMER = Maps.newHashMap();

	public static void init() {
		NetworkMemberRegistry.INSTANCE.register(AMNetworkTypes.ENERGY, new NetworkMemberRegistry.NetworkTypeProviderImpl<NetworkType>() {
			@Override
			public Collection<NetworkMemberType> get(WorldPos pos, @Nullable Direction direction) {
				if (!this.types.containsKey(pos.getBlock())) {
					var blockEntity = pos.getBlockEntity();
					
					if (blockEntity instanceof EnergyComponentProvider) {
						return NetworkMember.REQUESTER_PROVIDER;
					}
				}
				
				return super.get(pos, direction);
			}
		});

		NetworkMemberRegistry.INSTANCE.register(AMNetworkTypes.ITEM, new NetworkMemberRegistry.NetworkTypeProviderImpl<NetworkType>() {
			@Override
			public Collection<NetworkMemberType> get(WorldPos pos, @Nullable Direction direction) {
				if (!this.types.containsKey(pos.getBlock())) {
					var blockEntity = pos.getBlockEntity();
					
					if (blockEntity instanceof ItemComponentProvider) {
						return NetworkMember.REQUESTER_PROVIDER;
					}
				}
				
				return super.get(pos, direction);
			}
		});

		NetworkMemberRegistry.INSTANCE.register(AMNetworkTypes.FLUID, new NetworkMemberRegistry.NetworkTypeProviderImpl<NetworkType>() {
			@Override
			public Collection<NetworkMemberType> get(WorldPos pos, @Nullable Direction direction) {
				if (!this.types.containsKey(pos.getBlock())) {
					var blockEntity = pos.getBlockEntity();
					
					if (blockEntity instanceof FluidComponentProvider) {
						return NetworkMember.REQUESTER_PROVIDER;
					}
				}
				
				return super.get(pos, direction);
			}
		});

		NetworkMemberRegistry.NetworkTypeProvider<NetworkType> energy = NetworkMemberRegistry.INSTANCE.get(AMNetworkTypes.ENERGY);
		NetworkMemberRegistry.NetworkTypeProvider<NetworkType> fluid = NetworkMemberRegistry.INSTANCE.get(AMNetworkTypes.FLUID);

		BLOCK_CONSUMER.put(block -> block instanceof NetworkBlock, block -> {
			var networkBlock = (NetworkBlock) block;
			
			if (networkBlock.isMember(AMNetworkTypes.ENERGY)) energy.register(block, networkBlock.getEnergyNetworkMemberType());
			if (networkBlock.isMember(AMNetworkTypes.FLUID)) fluid.register(block, networkBlock.getFluidNetworkMemberType());
		});

		Registry.BLOCK.getEntries().forEach(entry -> process(entry.getKey(), entry.getValue()));
		
		// TODO: Reimplement this on Forge module!
		
		// RegistryEntryAddedCallback.event(Registry.BLOCK).register((index, identifier, block) -> acceptBlock(RegistryKey.of(Registry.BLOCK_KEY, identifier), block));
		
		postInit();
	}
	
	@ExpectPlatform
	public static void postInit() {
		throw new AssertionError();
	}

	public static void process(RegistryKey<Block> id, Block block) {
		if (id.getValue().getNamespace().equals(AMCommon.MOD_ID)) {
			for (var blockConsumerEntry : BLOCK_CONSUMER.entrySet()) {
				if (blockConsumerEntry.getKey().test(block)) {
					blockConsumerEntry.getValue().accept(block);
					break;
				}
			}
		}
	}
}
