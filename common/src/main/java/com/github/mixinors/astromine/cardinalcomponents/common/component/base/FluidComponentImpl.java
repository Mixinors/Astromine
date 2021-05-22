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

import net.minecraft.util.math.Direction;

import com.github.mixinors.astromine.techreborn.common.util.data.predicate.TriPredicate;
import com.github.mixinors.astromine.techreborn.common.volume.fluid.FluidVolume;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FluidComponentImpl implements FluidComponent {
	private final Int2ObjectOpenHashMap<FluidVolume> contents = new Int2ObjectOpenHashMap<>();

	private final int size;

	private final List<Runnable> listeners = new ArrayList<>();

	private TriPredicate<@Nullable Direction, FluidVolume, Integer> insertPredicate = (direction, volume, slot) -> true;

	private TriPredicate<@Nullable Direction, FluidVolume, Integer> extractPredicate = (direction, volume, integer) -> true;
	
	FluidComponentImpl(int size) {
		this.size = size;

		for (var i = 0; i < size; ++i) {
			set(i, FluidVolume.ofEmpty(this::updateListeners));
		}

		this.contents.defaultReturnValue(FluidVolume.ofEmpty(this::updateListeners));
	}
	
	FluidComponentImpl(FluidVolume... volumes) {
		this(volumes.length);

		for (var i = 0; i < volumes.length; ++i) {
			set(i, volumes[i]);
		}
	}
	
	public FluidComponentImpl withInsertPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.insertPredicate;
		this.insertPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}
	
	public FluidComponentImpl withExtractPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.extractPredicate;
		this.extractPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}
	
	@Override
	public boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
		return insertPredicate.test(direction, volume, slot) && FluidComponent.super.canInsert(direction, volume, slot);
	}
	
	@Override
	public boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
		return extractPredicate.test(direction, volume, slot) && FluidComponent.super.canExtract(direction, volume, slot);
	}
	
	@Override
	public int getSize() {
		return size;
	}
	
	@Override
	public @NotNull Map<Integer, FluidVolume> getContents() {
		return contents;
	}
	
	@Override
	public @NotNull List<Runnable> getListeners() {
		return listeners;
	}
}
