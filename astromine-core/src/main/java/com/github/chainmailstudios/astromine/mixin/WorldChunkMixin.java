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

import com.github.chainmailstudios.astromine.access.WorldChunkAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public class WorldChunkMixin implements WorldChunkAccess {
	@Shadow
	@Final
	private Level level;
	@Shadow
	@Final
	private ChunkPos chunkPos;
	@Shadow
	@Final
	private LevelChunkSection[] sections;

	private LevelChunk astromine_east;
	private LevelChunk astromine_west;
	private LevelChunk astromine_north;
	private LevelChunk astromine_south;

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
	public void astromine_attachEast(LevelChunk chunk) {
		this.astromine_east = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
	}

	@Override
	public void astromine_attachWest(LevelChunk chunk) {
		this.astromine_west = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
	}

	@Override
	public void astromine_attachNorth(LevelChunk chunk) {
		this.astromine_north = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
	}

	@Override
	public void astromine_attachSouth(LevelChunk chunk) {
		this.astromine_south = chunk;
		((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
	}

	@Override
	public void astromine_removeSubchunk(int subchunk) {
		this.sections[subchunk] = LevelChunk.EMPTY_SECTION;

	}

	@Override
	public LevelChunk astromine_east() {
		LevelChunk chunk = this.astromine_east;
		if (chunk == null) {
			ChunkPos pos = this.chunkPos;
			chunk = this.astromine_east = this.level.getChunk(pos.x + 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
			((WorldChunkAccess) chunk).astromine_attachWest((LevelChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public LevelChunk astromine_west() {
		LevelChunk chunk = this.astromine_west;
		if (chunk == null) {
			ChunkPos pos = this.chunkPos;
			chunk = this.astromine_west = this.level.getChunk(pos.x - 1, pos.z);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
			((WorldChunkAccess) chunk).astromine_attachEast((LevelChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public LevelChunk astromine_north() {
		LevelChunk chunk = this.astromine_north;
		if (chunk == null) {
			ChunkPos pos = this.chunkPos;
			chunk = this.astromine_north = this.level.getChunk(pos.x, pos.z - 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
			((WorldChunkAccess) chunk).astromine_attachSouth((LevelChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public LevelChunk astromine_south() {
		LevelChunk chunk = this.astromine_south;
		if (chunk == null) {
			ChunkPos pos = this.chunkPos;
			chunk = this.astromine_south = this.level.getChunk(pos.x, pos.z + 1);
			((WorldChunkAccess) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
			((WorldChunkAccess) chunk).astromine_attachNorth((LevelChunk) (Object) this);
		}

		return chunk;
	}

	@Inject(method = "setLoaded", at = @At("RETURN"))
	private void astromine_setLoadedToWorld(boolean loaded, CallbackInfo ci) {
		if (!loaded) { // if unloading
			this.astromine_runUnloadListeners();
		}
	}
}
