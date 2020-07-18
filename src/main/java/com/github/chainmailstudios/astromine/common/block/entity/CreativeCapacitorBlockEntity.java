package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.util.Tickable;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class CreativeCapacitorBlockEntity extends DefaultedEnergyBlockEntity implements NetworkMember, Tickable {
	public CreativeCapacitorBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_CAPACITOR);
	}

	@Override
	public void tick() {
		setStored(Double.MAX_VALUE);
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}
}
