package com.github.chainmailstudios.astromine.common.atmosphere;

import com.github.chainmailstudios.astromine.common.registry.UniRegistry;
import com.github.chainmailstudios.astromine.common.registry.base.MultiRegistry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;
import java.util.Optional;

public class AtmosphereRegistry extends UniRegistry<RegistryKey<DimensionType>, Boolean> {
	public static final AtmosphereRegistry INSTANCE = new AtmosphereRegistry();

	private AtmosphereRegistry() {
	}

	@Override
	public Boolean get(RegistryKey<DimensionType> dimensionTypeRegistryKey) {
		return Optional.ofNullable(super.get(dimensionTypeRegistryKey)).orElse(false);
	}
}
