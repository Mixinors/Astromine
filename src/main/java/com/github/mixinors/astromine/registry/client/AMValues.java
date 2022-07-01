package com.github.mixinors.astromine.registry.client;

import net.minecraft.util.math.MathHelper;

public class AMValues {
	public static float TICK_DELTA = 0.0F;
	
	public static final float[] MOON_LIGHT_LEVELS = computeBrightnessByLightLevel(15.0F);
	
	private static float[] computeBrightnessByLightLevel(float ambientLight) {
		var levels = new float[16];
		
		for (var i = 0; i <= 15; ++i) {
			var f = (float) i / 15.0f;
			var g = f / (4.0f - 3.0f * f);
			
			levels[i] = MathHelper.lerp(ambientLight, g, 1.0f);
		}
		
		return levels;
	}
}
