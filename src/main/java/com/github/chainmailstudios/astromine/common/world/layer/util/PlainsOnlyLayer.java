package com.github.chainmailstudios.astromine.common.world.layer.util;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum PlainsOnlyLayer implements InitLayer, IdentityCoordinateTransformer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource context, int x, int y) {
		return 1;
	}
}
