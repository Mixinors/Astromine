package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.network.ticker.NetworkTypeEnergy;
import com.github.chainmailstudios.astromine.common.network.ticker.NetworkTypeFluid;
import com.github.chainmailstudios.astromine.common.registry.NetworkTypeRegistry;

public class AstromineNetworkTypes {
	public static final NetworkType ENERGY = NetworkTypeRegistry.INSTANCE.register(AstromineCommon.identifier("energy_network"), new NetworkTypeEnergy());
	public static final NetworkType FLUID = NetworkTypeRegistry.INSTANCE.register(AstromineCommon.identifier("fluid_network"), new NetworkTypeFluid());

	public static void initialize() {
		// Unused.
	}
}

