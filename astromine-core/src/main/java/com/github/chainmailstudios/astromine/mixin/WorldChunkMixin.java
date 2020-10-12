/*
 * MIT License
 *
 * Copyright (c) 2020 Chainmail Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements WorldChunkAccess {
	@Shadow
	@Final
	private World world;
	@Shadow
	@Final
	private ChunkPos pos;
	@Shadow
	@Final
	private ChunkSection[] sections;

	private WorldChunk astromine_east;
	private WorldChunk astromine_west;
	private WorldChunk astromine_north;
	private WorldChunk astromine_south;

	private Runnable astromine_unload;

	@Override
	public void astromine_addUnloadListener(Runnable runnable) {
		if (this.astromine_unload == null) {
			this.astromine_unload = runnable;
		} else {
			Runnable run = this.astromine_unload;
			this.astromine_unload = () -> {
				run.run();
				runnable.run();
			};
		}
	}

	@Override
	public void astromine_runUnloadListeners() {
		if (this.astromine_unload != null) {
			this.astromine_unload.run();
		}
	}

	@Override
	public void astromine_attachEast(WorldChunk chunk) {
		this.astromine_east = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
	}

	@Override
	public void astromine_attachWest(WorldChunk chunk) {
		this.astromine_west = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
	}

	@Override
	public void astromine_attachNorth(WorldChunk chunk) {
		this.astromine_north = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
	}

	@Override
	public void astromine_attachSouth(WorldChunk chunk) {
		this.astromine_south = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
	}

	@Override
	public void astromine_removeSubchunk(int subchunk) {
		this.sections[subchunk] = WorldChunk.EMPTY_SECTION;

	}

	@Override
	public WorldChunk astromine_east() {
		WorldChunk chunk = this.astromine_east;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.astromine_east = this.world.getChunk(pos.x + 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
			((WorldChunkAccess) chunk).astromine_attachWest((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_west() {
		WorldChunk chunk = this.astromine_west;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.astromine_west = this.world.getChunk(pos.x - 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
			((WorldChunkAccess) chunk).astromine_attachEast((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_north() {
		WorldChunk chunk = this.astromine_north;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.astromine_north = this.world.getChunk(pos.x, pos.z - 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
			((WorldChunkAccess) chunk).astromine_attachSouth((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_south() {
		WorldChunk chunk = this.astromine_south;
		if (chunk == null) {
			ChunkPos pos = this.pos;
			chunk = this.astromine_south = this.world.getChunk(pos.x, pos.z + 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
			((WorldChunkAccess) chunk).astromine_attachNorth((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Inject(method = "setLoadedToWorld", at = @At("RETURN"))
	private void astromine_setLoadedToWorld(boolean loaded, CallbackInfo ci) {
		if (!loaded) { // if unloading
			this.astromine_runUnloadListeners();
		}
	}
}
