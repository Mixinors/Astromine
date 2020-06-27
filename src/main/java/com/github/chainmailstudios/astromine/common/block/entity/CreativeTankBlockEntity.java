package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;

public class CreativeTankBlockEntity extends DefaultedFluidBlockEntity implements NetworkMember, Tickable {
	public static final Identifier FLUID_CHANGE_PACKET = AstromineCommon.identifier("fluid_change_component");

	public CreativeTankBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_TANK);

		addConsumer(FLUID_CHANGE_PACKET, (buffer, context) -> {
			Identifier fluidIdentifier = buffer.readIdentifier();

			Fluid fluid = Registry.FLUID.get(fluidIdentifier);

			fluidComponent.setVolume(0, new FluidVolume(fluid, new Fraction(Integer.MAX_VALUE, 1)));

			sync();
		});

		fluidComponent.getVolume(0).setSize(new Fraction(Integer.MAX_VALUE, 1));
	}

	@Override
	public void tick() {
		fluidComponent.getVolume(0).setFraction(new Fraction(Integer.MAX_VALUE, 1));
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.FLUID;
	}

	@Override
	public <T extends NetworkType> boolean isBuffer(T type) {
		return true;
	}
}
