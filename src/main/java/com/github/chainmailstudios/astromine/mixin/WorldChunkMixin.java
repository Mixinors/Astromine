package com.github.chainmailstudios.astromine.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;

@Mixin (WorldChunk.class)
public class WorldChunkMixin implements WorldChunkAccess {
	@Shadow @Final private World world;
	@Shadow @Final private ChunkPos pos;
	@Shadow @Final private ChunkSection[] sections;
	private WorldChunk east, west, north, south;
	private Runnable unload;

	@Override
	public void astromine_addUnloadListener(Runnable runnable) {
		if (this.unload == null) {
			this.unload = runnable;
		} else {
			Runnable run = this.unload;
			this.unload = () -> {
				run.run();
				runnable.run();
			};
		}
	}

	@Override
	public void astromine_runUnloadListeners() {
		if (this.unload != null) {
			this.unload.run();
		}
	}

	@Override
	public void astromine_attachEast(WorldChunk chunk) {
		this.east = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.east = null);
	}

	@Override
	public void astromine_attachWest(WorldChunk chunk) {
		this.west = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.west = null);
	}

	@Override
	public void astromine_attachNorth(WorldChunk chunk) {
		this.north = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.north = null);
	}

	@Override
	public void astromine_attachSouth(WorldChunk chunk) {
		this.south = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.south = null);
	}

	@Override
	public void astromine_yeet(int subchunk) {
		this.sections[subchunk] = WorldChunk.EMPTY_SECTION;

	}

	@Override
	public WorldChunk astromine_east() {
		WorldChunk chunk = this.east;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.east = this.world.getChunk(pos.x + 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.east = null);
			((WorldChunkAccess) chunk).astromine_attachWest((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_west() {
		WorldChunk chunk = this.west;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.west = this.world.getChunk(pos.x - 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.west = null);
			((WorldChunkAccess) chunk).astromine_attachEast((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_north() {
		WorldChunk chunk = this.north;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.north = this.world.getChunk(pos.x, pos.z - 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.north = null);
			((WorldChunkAccess) chunk).astromine_attachSouth((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_south() {
		WorldChunk chunk = this.south;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.south = this.world.getChunk(pos.x, pos.z + 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.south = null);
			((WorldChunkAccess) chunk).astromine_attachNorth((WorldChunk) (Object) this);
		}

		return chunk;
	}


	@Inject (method = "setLoadedToWorld", at = @At ("RETURN"))
	private void serialize(boolean loaded, CallbackInfo ci) {
		if (!loaded) { // if unloading
			this.astromine_runUnloadListeners();
		}
	}
}
