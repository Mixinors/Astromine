package com.github.mixinors.astromine.common.event;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;

// TODO: Improve MoonChunkGenerator code readability.
// TODO: Add Moon Biomes and move checks to them.
// TODO: Add transition into weaker/stronger fog.
// TODO: Reduce sky light in dark moon areas.
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
