/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

package com.github.mixinors.astromine.common.component.world;

import com.github.mixinors.astromine.registry.common.AMComponents;
import dev.onyxstudios.cca.api.v3.component.Component;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class HoloBridgesComponent implements Component {
	private static final String POSITION_KEY = "Positions";
	private static final String VECTORS_KEY = "Vectors";
	private static final String DATA_KEY = "Data";
	
	private final Long2ObjectArrayMap<VoxelShape> shapes = new Long2ObjectArrayMap<>();
	
	private final World world;

	@Nullable
	public static <V> HoloBridgesComponent get(V v) {
		try {
			return AMComponents.HOLO_BRIDGES.get(v);
		} catch (Exception justShutUpAlready) {
			return null;
		}
	}
	
	public HoloBridgesComponent(World world) {
		this.world = world;
	}
	
	public VoxelShape getShape(BlockPos pos) {
		var shape = shapes.get(pos.asLong());
		
		if (shape == null) {
			return VoxelShapes.empty();
		}
		
		return shape;
	}
	
	public void setShape(BlockPos pos, VoxelShape shape) {
		if (shape == null) {
			shapes.remove(pos.asLong());
		} else {
			shapes.put(pos.asLong(), shape);
		}
	}
	
	public World getWorld() {
		return world;
	}
	
	@Override
	public void writeToNbt(NbtCompound tag) {
	
	}
	
	@Override
	public void readFromNbt(NbtCompound tag) {
	
	}
}
