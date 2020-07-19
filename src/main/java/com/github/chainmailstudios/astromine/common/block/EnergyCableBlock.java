package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EnergyCableBlock extends AbstractCableBlock {
	public EnergyCableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public <T extends NetworkType> T getNetworkType() {
		return (T) AstromineNetworkTypes.ENERGY;
	}
}
