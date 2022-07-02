package com.github.mixinors.astromine.common.event;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;

// TODO: Fix Skybox rendering without Z.
public interface BackgroundEvents {
	Event<Calculate> FOG = EventFactory.createCompoundEventResult(Calculate.class);
	
	Event<Render> RENDER = EventFactory.createEventResult(Render.class);
	
	@FunctionalInterface
	interface Calculate {
		CompoundEventResult<Integer> calculate(Camera camera, BackgroundRenderer.FogType type);
	}
	
	@FunctionalInterface
	interface Render {
		EventResult render(Camera camera, float tickDelta, ClientWorld world, int viewDistnace, float skyDarkness);
	}
}
