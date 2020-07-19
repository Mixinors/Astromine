package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.component.block.entity.EnergyEmitter;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.util.Tickable;

public class CreativeCapacitorBlockEntity extends DefaultedEnergyBlockEntity implements NetworkMember, Tickable {
	public CreativeCapacitorBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_CAPACITOR);
	}

	@Override
	public void tick() {
		setStored(Double.MAX_VALUE);

		EnergyEmitter.emit(this, 0);
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
