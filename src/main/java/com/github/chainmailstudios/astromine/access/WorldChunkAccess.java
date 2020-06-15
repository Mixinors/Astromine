package com.github.chainmailstudios.astromine.access;

import net.minecraft.world.chunk.WorldChunk;

public interface WorldChunkAccess {
    void astromine_addUnloadListener(Runnable runnable);

    void astromine_runUnloadListeners();

    void astromine_attachEast(WorldChunk chunk);

    void astromine_attachWest(WorldChunk chunk);

    void astromine_attachNorth(WorldChunk chunk);

    void astromine_attachSouth(WorldChunk chunk);

    void astromine_yeet(int subchunk);

    WorldChunk astromine_east();

    WorldChunk astromine_west();

    WorldChunk astromine_north();

    WorldChunk astromine_south();
}
