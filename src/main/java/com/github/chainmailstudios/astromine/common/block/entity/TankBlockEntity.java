package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class TankBlockEntity extends DefaultedFluidBlockEntity implements NetworkMember {
	public TankBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_TANK);

		fluidComponent.getVolume(0).setSize(new Fraction(16, 1));
	}

	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID;
	}
}
