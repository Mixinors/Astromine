package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

import java.util.Arrays;

public class FluidCableBlock extends PipeCableBlock {
	public FluidCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.FLUID;
	}

	@Override
	public boolean accepts(Object... objects) {
		return true;
	}
}
