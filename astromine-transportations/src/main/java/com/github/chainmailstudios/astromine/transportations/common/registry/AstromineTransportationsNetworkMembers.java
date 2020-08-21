package com.github.chainmailstudios.astromine.transportations.common.registry;

import com.github.chainmailstudios.astromine.client.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.common.network.type.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkMembers;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import com.github.chainmailstudios.astromine.transportations.common.block.EnergyCableBlock;
import com.github.chainmailstudios.astromine.transportations.common.block.FluidCableBlock;

import static com.github.chainmailstudios.astromine.common.network.NetworkMemberType.NODE;

public class AstromineTransportationsNetworkMembers extends AstromineNetworkMembers {
	public static void initialize() {
		NetworkMemberRegistry.NetworkTypeRegistry<NetworkType> energy = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.ENERGY);
		NetworkMemberRegistry.NetworkTypeRegistry<NetworkType> fluid = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.FLUID);

		BLOCK_CONSUMER.put(block -> block instanceof EnergyCableBlock, block -> {
			energy.register(block, NODE);
		});
		BLOCK_CONSUMER.put(block -> block instanceof FluidCableBlock, block -> {
			fluid.register(block, NODE);
		});
	}
}
