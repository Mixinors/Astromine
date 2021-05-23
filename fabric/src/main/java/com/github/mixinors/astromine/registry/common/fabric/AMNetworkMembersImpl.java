package com.github.mixinors.astromine.registry.common.fabric;

import com.github.mixinors.astromine.registry.common.AMNetworkMembers;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class AMNetworkMembersImpl {
	public static void postInit() {
		RegistryEntryAddedCallback.event(Registry.BLOCK).register((index, identifier, block) -> AMNetworkMembers.process(RegistryKey.of(Registry.BLOCK_KEY, identifier), block));
	}
}
