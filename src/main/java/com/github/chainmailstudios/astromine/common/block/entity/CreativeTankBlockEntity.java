package com.github.chainmailstudios.astromine.common.block.entity;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.registry.Registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.packet.PacketHandlerComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;

public class CreativeTankBlockEntity extends DefaultedFluidBlockEntity implements NetworkMember, Tickable {
	public static final Identifier FLUID_CHANGE_PACKET = AstromineCommon.identifier("fluid_change_component");

	private final PacketHandlerComponent packetComponent = ((identifier, buffer) -> {
		if (identifier.equals(FLUID_CHANGE_PACKET)) {
			Identifier fluidIdentifier = buffer.readIdentifier();

			Fluid fluid = Registry.FLUID.get(fluidIdentifier);

			fluidComponent.setVolume(0, new FluidVolume(fluid, new Fraction(Integer.MAX_VALUE, 1)));

			sync();
		}
	});

	@Override
	public <C extends Component> C getComponent(ComponentType<C> type) {
		return type == AstromineComponentTypes.PACKET_HANDLER_COMPONENT ? (C) packetComponent : super.getComponent(type);
	}

	public CreativeTankBlockEntity() {
		super(AstromineBlockEntityTypes.CREATIVE_TANK);

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
