package com.github.chainmailstudios.astromine.technologies.registry;

import com.github.chainmailstudios.astromine.common.network.type.base.NetworkType;
import com.github.chainmailstudios.astromine.common.registry.NetworkMemberRegistry;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkMembers;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import com.github.chainmailstudios.astromine.technologies.common.block.*;

import static com.github.chainmailstudios.astromine.common.network.NetworkMemberType.*;
import static com.github.chainmailstudios.astromine.common.network.NetworkMemberType.PROVIDER;

public class AstromineTechnologiesNetworkMembers extends AstromineNetworkMembers {
	public static void initialize() {
		NetworkMemberRegistry.NetworkTypeRegistry<NetworkType> energy = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.ENERGY);
		NetworkMemberRegistry.NetworkTypeRegistry<NetworkType> fluid = NetworkMemberRegistry.INSTANCE.get(AstromineNetworkTypes.FLUID);

		BLOCK_CONSUMER.put(block -> block instanceof AlloySmelterBlock, block -> {
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof BlockBreakerBlock || block instanceof BlockPlacerBlock, block -> {
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof ElectricSmelterBlock, block -> {
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof ElectrolyzerBlock, block -> {
			fluid.register(block, BUFFER);
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof FluidExtractorBlock, block -> {
			fluid.register(block, PROVIDER);
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof FluidInserterBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof FluidMixerBlock, block -> {
			fluid.register(block, BUFFER);
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof LiquidGeneratorBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, PROVIDER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof SolidGeneratorBlock, block -> {
			energy.register(block, PROVIDER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof TankBlock, block -> {
			energy.register(block, BUFFER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof TrituratorBlock, block -> {
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof PresserBlock, block -> {
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof VentBlock, block -> {
			fluid.register(block, REQUESTER);
			energy.register(block, REQUESTER);
		});
		BLOCK_CONSUMER.put(block -> block instanceof CapacitorBlock && block != AstromineTechnologiesBlocks.CREATIVE_CAPACITOR, block -> {
			energy.register(block, BUFFER);
		});

		energy.register(AstromineTechnologiesBlocks.CREATIVE_CAPACITOR, PROVIDER);
	}
}
