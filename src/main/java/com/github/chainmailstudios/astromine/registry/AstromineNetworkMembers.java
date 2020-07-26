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

import com.github.chainmailstudios.astromine.client.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.client.registry.NetworkMemberRegistry.NetworkTypeRegistry;
import com.github.chainmailstudios.astromine.common.block.*;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.github.chainmailstudios.astromine.common.network.NetworkMemberType.*;
import static com.github.chainmailstudios.astromine.registry.AstromineBlocks.ENERGY_CABLE;
import static com.github.chainmailstudios.astromine.registry.AstromineBlocks.FLUID_CABLE;

public class AstromineNetworkMembers {
	public static void initialize() {
		NetworkTypeRegistry<NetworkType> energy = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.ENERGY);
		NetworkTypeRegistry<NetworkType> fluid = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.FLUID);

		energy.register(ENERGY_CABLE, NODE);
		fluid.register(FLUID_CABLE, NODE);

		Map<Predicate<Block>, Consumer<Block>> blockConsumer = Maps.newHashMap();
		blockConsumer.put(block -> block instanceof AlloySmelterBlock, block -> {
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof BlockBreakerBlock || block instanceof BlockPlacerBlock, block -> {
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof ElectricSmelterBlock, block -> {
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof ElectrolyzerBlock, block -> {
			fluid.register(block, BUFFER);
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof FluidExtractorBlock, block -> {
			fluid.register(block, PROVIDER);
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof FluidInserterBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof FluidMixerBlock, block -> {
			fluid.register(block, BUFFER);
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof LiquidGeneratorBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, PROVIDER);
		});
		blockConsumer.put(block -> block instanceof SolidGeneratorBlock, block -> {
			energy.register(block, PROVIDER);
		});
		blockConsumer.put(block -> block instanceof TankBlock, block -> {
			energy.register(block, BUFFER);
		});
		blockConsumer.put(block -> block instanceof TrituratorBlock, block -> {
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof PresserBlock, block -> {
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof VentBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, REQUESTER);
		});
		blockConsumer.put(block -> block instanceof CreativeCapacitorBlock, block -> {
			energy.register(block, BUFFER);
		});

		for (Map.Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			if (entry.getKey().getValue().getNamespace().equals("astromine")) {
				for (Map.Entry<Predicate<Block>, Consumer<Block>> blockConsumerEntry : blockConsumer.entrySet()) {
					if (blockConsumerEntry.getKey().test(entry.getValue())) {
						blockConsumerEntry.getValue().accept(entry.getValue());
						break;
					}
				}
			}
		}
	}
}
