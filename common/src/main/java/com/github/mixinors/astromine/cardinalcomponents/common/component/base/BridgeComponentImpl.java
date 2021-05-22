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

package com.github.mixinors.astromine.cardinalcomponents.common.component.base;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import java.util.Set;


public class BridgeComponentImpl implements BridgeComponent {
	private final Long2ObjectArrayMap<Set<Vec3i>> entries = new Long2ObjectArrayMap<>();

	private final Long2ObjectArrayMap<VoxelShape> cache = new Long2ObjectArrayMap<>();

	private final World world;
	
	BridgeComponentImpl(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Long2ObjectArrayMap<Set<Vec3i>> getEntries() {
		return entries;
	}
	
	public Long2ObjectArrayMap<VoxelShape> getCache() {
		return cache;
	}
}
