/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.mixin.common;

import com.github.mixinors.astromine.common.access.WorldChunkAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.chunk.BlendingData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin extends Chunk implements WorldChunkAccessor {
	@Shadow
	@Final World world;

	private WorldChunk astromine_east;
	private WorldChunk astromine_west;
	private WorldChunk astromine_north;
	private WorldChunk astromine_south;

	private Runnable astromine_unload;

	public WorldChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> registry, long l, @Nullable ChunkSection[] chunkSections, @Nullable BlendingData blendingData) {
		super(chunkPos, upgradeData, heightLimitView, registry, l, chunkSections, blendingData);
	}

	@Override
	public void astromine_addUnloadListener(Runnable runnable) {
		if (this.astromine_unload == null) {
			this.astromine_unload = runnable;
		} else {
			var run = this.astromine_unload;
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
		((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
	}

	@Override
	public void astromine_attachWest(WorldChunk chunk) {
		this.astromine_west = chunk;
		((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
	}

	@Override
	public void astromine_attachNorth(WorldChunk chunk) {
		this.astromine_north = chunk;
		((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
	}

	@Override
	public void astromine_attachSouth(WorldChunk chunk) {
		this.astromine_south = chunk;
		((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
	}

	@Override
	public void astromine_removeSubchunk(int subchunk) {
		this.sectionArray[subchunk] = new ChunkSection(world.sectionIndexToCoord(subchunk), new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE),
			sectionArray[subchunk].getBiomeContainer());
	}

	@Override
	public WorldChunk astromine_east() {
		var chunk = this.astromine_east;
		if (chunk == null) {
			var pos = this.pos;
			chunk = this.astromine_east = this.world.getChunk(pos.x + 1, pos.z);
			((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_east = null);
			((WorldChunkAccessor) chunk).astromine_attachWest((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_west() {
		var chunk = this.astromine_west;
		if (chunk == null) {
			var pos = this.pos;
			chunk = this.astromine_west = this.world.getChunk(pos.x - 1, pos.z);
			((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_west = null);
			((WorldChunkAccessor) chunk).astromine_attachEast((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_north() {
		var chunk = this.astromine_north;
		if (chunk == null) {
			var pos = this.pos;
			chunk = this.astromine_north = this.world.getChunk(pos.x, pos.z - 1);
			((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_north = null);
			((WorldChunkAccessor) chunk).astromine_attachSouth((WorldChunk) (Object) this);
		}

		return chunk;
	}

	@Override
	public WorldChunk astromine_south() {
		var chunk = this.astromine_south;
		if (chunk == null) {
			var pos = this.pos;
			chunk = this.astromine_south = this.world.getChunk(pos.x, pos.z + 1);
			((WorldChunkAccessor) chunk).astromine_addUnloadListener(() -> this.astromine_south = null);
			((WorldChunkAccessor) chunk).astromine_attachNorth((WorldChunk) (Object) this);
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
