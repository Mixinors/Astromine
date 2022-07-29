package com.github.mixinors.astromine.registry.client;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.client.render.effects.MoonDimensionEffects;
import com.github.mixinors.astromine.client.render.effects.SpaceDimensionEffects;
import com.github.mixinors.astromine.common.registry.base.RegistryEntry;
import com.github.mixinors.astromine.registry.client.AMRegistries;
import net.minecraft.client.render.DimensionEffects;

public class AMDimensionEffects {
    public static final RegistryEntry<DimensionEffects> SPACE = AMRegistries.DIMENSION_EFFECTS.register(AMCommon.id("space"), new SpaceDimensionEffects());
	public static final RegistryEntry<DimensionEffects> MOON = AMRegistries.DIMENSION_EFFECTS.register(AMCommon.id("moon"), new MoonDimensionEffects());
}
