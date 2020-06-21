package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class EnergyCableBlock extends AbstractCableBlock {
	public EnergyCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.ENERGY;
	}
}
