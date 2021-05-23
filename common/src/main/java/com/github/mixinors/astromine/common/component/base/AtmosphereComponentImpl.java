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

package com.github.mixinors.astromine.common.component.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;

import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.registry.common.AMConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class AtmosphereComponentImpl implements AtmosphereComponent {
	private final Map<BlockPos, FluidVolume> volumes = new ConcurrentHashMap<>();

	private final World world;

	private final Chunk chunk;

	private int tickCount = 0;
	
	AtmosphereComponentImpl(Chunk chunk) {
		if (chunk instanceof WorldChunk) {
			this.world = ((WorldChunk) chunk).getWorld();
			this.chunk = chunk;
		} else {
			this.world = null;
			this.chunk = null;
		}
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public Chunk getChunk() {
		return chunk;
	}
	
	@Override
	public Map<BlockPos, FluidVolume> getVolumes() {
		return volumes;
	}
	
	@Override
	public long getTickCount() {
		return tickCount;
	}
	
	@Override
	public void serverTick() {
		if (world == null)
			return;

		if (tickCount < AMConfig.get().gasTickRate) {
			tickCount++;
		} else {
			tickCount = 0;
		}
		
		AtmosphereComponent.super.serverTick();
	}
}
