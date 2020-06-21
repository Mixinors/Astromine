package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.util.Tickable;

public class FluidTankBlockEntity extends BetaBlockEntity implements NetworkMember {
	public FluidTankBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_TANK);
	}

	@Override
	public boolean isBuffer() {
		return true;
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.FLUID;
	}
}
