package com.github.mixinors.astromine.common.registry;

import com.github.mixinors.astromine.registry.common.AMNetworkMembers;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AMFNetworkMembers {
	public static void init() {
		RegistryEntryAddedCallback.event(Registry.BLOCK).register((index, identifier, block) -> AMNetworkMembers.process(RegistryKey.of(Registry.BLOCK_KEY, identifier), block));
	}
}
