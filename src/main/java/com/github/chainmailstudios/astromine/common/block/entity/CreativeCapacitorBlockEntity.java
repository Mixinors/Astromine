package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.util.Tickable;

public class CreativeCapacitorBlockEntity extends DefaultedEnergyBlockEntity implements NetworkMember, Tickable {
	public CreativeCapacitorBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_CAPACITOR);

		energyComponent.getVolume(0).setSize(new Fraction(Integer.MAX_VALUE, 1));
	}

	@Override
	public void tick() {
		energyComponent.getVolume(0).setFraction(new Fraction(Integer.MAX_VALUE, 1));
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY;
	}

	@Override
	public boolean isBuffer() {
		return true;
	}
}
