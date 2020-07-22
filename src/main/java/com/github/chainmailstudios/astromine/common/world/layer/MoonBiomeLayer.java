package com.github.chainmailstudios.astromine.common.world.layer;

import com.github.chainmailstudios.astromine.registry.AstromineBiomes;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;

public enum MoonBiomeLayer implements InitLayer, IdentityCoordinateTransformer {
	INSTANCE;

	@Override
	public int sample(LayerRandomnessSource context, int x, int y) {
		switch (context.nextInt(3)) {
			case 0:
				return Registry.BIOME.getRawId(AstromineBiomes.MOON_FLATS);
			case 1:
				return Registry.BIOME.getRawId(AstromineBiomes.MOON_HILLS);
			case 2:
				return Registry.BIOME.getRawId(AstromineBiomes.MOON_LOWLANDS);
		}

		return Registry.BIOME.getRawId(AstromineBiomes.MOON_FLATS);
	}
}
