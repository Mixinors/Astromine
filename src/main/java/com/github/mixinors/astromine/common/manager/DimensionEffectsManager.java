package com.github.mixinors.astromine.common.manager;

import com.github.mixinors.astromine.registry.client.AMRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DimensionEffects;

public class DimensionEffectsManager {
	public static void onClientStarted(MinecraftClient client) {
		AMRegistries.DIMENSION_EFFECTS.getEntries().forEach((v) -> DimensionEffects.BY_IDENTIFIER.put(v.getKey(), v.getValue()));
	}
}
