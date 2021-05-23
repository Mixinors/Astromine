package com.github.mixinors.astromine.mixin.common.common;

import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkManager.class)
public interface ServerChunkManagerAccessor {
	@Accessor
	ChunkGenerator getChunkGenerator();
	
	@Accessor
	void setChunkGenerator(ChunkGenerator chunkGenerator);
}
