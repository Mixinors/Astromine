package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class FluidCableBlock extends AbstractCableBlock {
	public FluidCableBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public <T extends NetworkType> T getNetworkType() {
		return (T) AstromineNetworkTypes.FLUID;
	}
}
