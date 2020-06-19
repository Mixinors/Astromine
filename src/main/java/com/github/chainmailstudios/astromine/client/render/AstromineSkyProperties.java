package com.github.chainmailstudios.astromine.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

@Environment (EnvType.CLIENT)
public class AstromineSkyProperties extends SkyProperties {

	// float = cloud height

	// alternatveSkyColor
	public AstromineSkyProperties() {
		super(Float.NaN, false, SkyType.NONE,  true, true);
	}

	@Override
	public Vec3d adjustSkyColor(Vec3d color, float sunHeight) {
		return color.multiply(0.15000000596046448D);
	}

	@Override
	public boolean useThickFog(int camX, int camY) {
		return false;
	}
}
