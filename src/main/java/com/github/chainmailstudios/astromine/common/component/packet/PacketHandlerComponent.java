package com.github.chainmailstudios.astromine.common.component.packet;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface PacketHandlerComponent extends Component {
	@Override
	@Deprecated
	default void fromTag(CompoundTag tag) {
	}

	@Override
	@Deprecated
	default CompoundTag toTag(CompoundTag tag) {
		return new CompoundTag();
	}

	void handle(Identifier identifier, PacketByteBuf buffer);
}
