package com.github.chainmailstudios.astromine.access;

import net.minecraft.world.chunk.WorldChunk;

public interface WorldChunkAccess {
	void astromine_addUnloadListener(Runnable runnable);
	void astromine_runUnloadListeners();

	WorldChunk astromine_east();
	WorldChunk astromine_west();
	WorldChunk astromine_north();
	WorldChunk astromine_south();
}
