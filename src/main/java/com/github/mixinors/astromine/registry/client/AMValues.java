package com.github.mixinors.astromine.registry.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class AMValues {
	public static float TICK_DELTA = 0.0F;

	public static void onEnd(WorldRenderContext context) {
		AMValues.TICK_DELTA = context.tickDelta();
	}
}
