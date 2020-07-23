package com.github.chainmailstudios.astromine.common.world.layer;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum MarsRiverLayer implements CrossSamplingLayer {
	INSTANCE;

	public static final int RIVER_ID = Registry.BIOME.getRawId(AstromineBiomes.MARS_RIVERBED);

	public int sample(LayerRandomnessSource context, int n, int e, int s, int w, int center) {
		int i = isValidForRiver(center);
		return i == isValidForRiver(w) && i == isValidForRiver(n) && i == isValidForRiver(e) && i == isValidForRiver(s) ? -1 : RIVER_ID;
	}

	private static int isValidForRiver(int value) {
		return value >= 2 ? 2 + (value & 1) : value;
	}
}