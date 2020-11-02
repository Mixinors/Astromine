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

package com.github.chainmailstudios.astromine.common.component.inventory;

import net.minecraft.util.math.Direction;

import com.github.chainmailstudios.astromine.common.utilities.data.predicate.TriPredicate;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimpleFluidComponent implements FluidComponent {
	private final Int2ObjectOpenHashMap<FluidVolume> contents = new Int2ObjectOpenHashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();
	private final int size;
	private TriPredicate<@Nullable Direction, FluidVolume, Integer> insertPredicate = (direction, volume, slot) -> true;
	private TriPredicate<@Nullable Direction, FluidVolume, Integer> extractPredicate = (direction, volume, integer) -> true;

	protected SimpleFluidComponent(int size) {
		this.size = size;
		for (int i = 0; i < size; ++i) {
			setVolume(i, FluidVolume.empty());
		}
		this.contents.defaultReturnValue(FluidVolume.empty());
	}

	protected SimpleFluidComponent(FluidVolume... volumes) {
		this(volumes.length);
		for (int i = 0; i < volumes.length; ++i) {
			setVolume(i, volumes[i]);
		}
	}

	public static SimpleFluidComponent of(int size) {
		return new SimpleFluidComponent(size);
	}

	public static SimpleFluidComponent of(FluidVolume... volumes) {
		return new SimpleFluidComponent(volumes);
	}

	@Override
	public boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
		return insertPredicate.test(direction, volume, slot) && FluidComponent.super.canInsert(direction, volume, slot);
	}

	@Override
	public boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
		return extractPredicate.test(direction, volume, slot) && FluidComponent.super.canExtract(direction, volume, slot);
	}

	public SimpleFluidComponent withInsertPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.insertPredicate;
		this.insertPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}

	public SimpleFluidComponent withExtractPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.extractPredicate;
		this.extractPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}

	@Override
	public Map<Integer, FluidVolume> getContents() {
		return contents;
	}

	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public boolean equals(Object o) { // used by CCA to tell if two stacks are equal
		if (this == o)
			return true;
		if (!(o instanceof SimpleFluidComponent))
			return false;
		SimpleFluidComponent entries = (SimpleFluidComponent) o;
		return Objects.equals(contents, entries.contents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}
}
