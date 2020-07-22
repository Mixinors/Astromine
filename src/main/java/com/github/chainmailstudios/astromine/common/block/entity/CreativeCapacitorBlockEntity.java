package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class CreativeCapacitorBlockEntity extends DefaultedEnergyBlockEntity implements NetworkMember, Tickable {
	public CreativeCapacitorBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_CAPACITOR);
	}

	@Override
	protected double getEnergySize() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void tick() {
		super.tick();

		setStored(Double.MAX_VALUE);
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.ENERGY, BUFFER);
	}
}
