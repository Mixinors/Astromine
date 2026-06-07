package com.github.mixinors.astromine.registry.common;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;

public class AMTrackedDataHandlers {
	public static final EntityDataSerializer<Long> LONG = EntityDataSerializer.forValueType(ByteBufCodecs.VAR_LONG);
}
