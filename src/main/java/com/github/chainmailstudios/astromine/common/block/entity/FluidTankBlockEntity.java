package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;

public class FluidTankBlockEntity extends BetaBlockEntity implements NetworkMember, Tickable {
	public FluidTankBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_TANK);
	}

	@Override
	public boolean accepts(Object... objects) {
		return true;
	}

	@Override
	public boolean isBuffer() {
		return true;
	}

	@Override
	public NetworkType getNetworkType() {
		return AstromineNetworkTypes.FLUID;
	}

	@Override
	public void tick() {
		System.out.println(fluidVolume.toInterfaceString());
	}
}
