package com.github.chainmailstudios.astromine.common.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

@FunctionalInterface
public interface ServerChunkTickCallback {
	Event<ServerChunkTickCallback> EVENT = EventFactory.createArrayBacked(ServerChunkTickCallback.class, (listeners) -> (world, chunk) -> {
		for (ServerChunkTickCallback listener : listeners) {
			listener.tickChunk(world, chunk);
		}
	});

	void tickChunk(ServerWorld world, WorldChunk chunk);
}
