package com.github.chainmailstudios.astromine.client.registry;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import com.github.chainmailstudios.astromine.client.render.sky.skybox.AbstractSkybox;
import com.github.chainmailstudios.astromine.common.registry.base.BiRegistry;

public class SkyboxRegistry extends BiRegistry<RegistryKey<DimensionType>, AbstractSkybox> {
	public static final SkyboxRegistry INSTANCE = new SkyboxRegistry();

	private SkyboxRegistry() {
		// Locked.
	}
}
