package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.network.NetworkInstance;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.network.ticker.NetworkTypeEnergy;
import com.github.chainmailstudios.astromine.common.network.ticker.NetworkTypeFluid;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;

public class AstromineNetworkTypes {
	public static final NetworkType ENERGY = register("energy_network", new NetworkTypeEnergy());
	public static final NetworkType FLUID = register("fluid_network", new NetworkTypeFluid());
	public static final NetworkType ITEM = register("item_network", new NetworkType() {
		@Override
		public void tick(NetworkInstance instance) {
			// TODO: item network
		}
	});

	public static void initialize() {
		// Unused.
	}

	public static <T extends NetworkType> T register(String name, T type) {
		return (T) NetworkTypeRegistry.INSTANCE.register(AstromineCommon.identifier(name), type);
	}
}

