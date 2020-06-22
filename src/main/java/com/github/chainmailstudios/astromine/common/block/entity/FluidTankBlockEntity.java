package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class FluidTankBlockEntity extends BetaBlockEntity implements NetworkMember {
	public FluidTankBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_TANK);

		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	public boolean isBuffer() {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID;
	}
}
