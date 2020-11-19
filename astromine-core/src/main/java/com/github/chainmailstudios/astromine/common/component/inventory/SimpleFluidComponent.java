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

import com.github.chainmailstudios.astromine.common.utilities.data.predicate.TriPredicate;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleFluidComponent implements FluidComponent {
	private final Int2ObjectOpenHashMap<FluidVolume> contents = new Int2ObjectOpenHashMap<>();

	private final List<Runnable> listeners = new ArrayList<>();

	private TriPredicate<@Nullable Direction, FluidVolume, Integer> insertPredicate = (direction, volume, slot) -> true;

	private TriPredicate<@Nullable Direction, FluidVolume, Integer> extractPredicate = (direction, volume, integer) -> true;

	private final int size;

	/** Instantiates a {@link SimpleFluidComponent} with the given value. */
	protected SimpleFluidComponent(int size) {
		this.size = size;

		for (int i = 0; i < size; ++i) {
			setVolume(i, FluidVolume.ofEmpty(this::updateListeners));
		}

		this.contents.defaultReturnValue(FluidVolume.ofEmpty(this::updateListeners));
	}

	/** Instantiates a {@link SimpleFluidComponent} with the given values. */
	protected SimpleFluidComponent(FluidVolume... volumes) {
		this(volumes.length);

		for (int i = 0; i < volumes.length; ++i) {
			setVolume(i, volumes[i]);
		}
	}

	/** Instantiates a {@link SimpleFluidComponent} with the given value. */
	public static SimpleFluidComponent of(int size) {
		return new SimpleFluidComponent(size);
	}

	/** Instantiates a {@link SimpleFluidComponent} with the given values. */
	public static SimpleFluidComponent of(FluidVolume... volumes) {
		return new SimpleFluidComponent(volumes);
	}

	/** Returns this component with an added insertion predicate. */
	public SimpleFluidComponent withInsertPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.insertPredicate;
		this.insertPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}

	/** Returns this component with an added extraction predicate. */
	public SimpleFluidComponent withExtractPredicate(TriPredicate<@Nullable Direction, FluidVolume, Integer> predicate) {
		TriPredicate<Direction, FluidVolume, Integer> triPredicate = this.extractPredicate;
		this.extractPredicate = (direction, volume, integer) -> triPredicate.test(direction, volume, integer) && predicate.test(direction, volume, integer);
		return this;
	}

	/** Override behavior to take {@link #insertPredicate} into account. */
	@Override
	public boolean canInsert(@Nullable Direction direction, FluidVolume volume, int slot) {
		return insertPredicate.test(direction, volume, slot) && FluidComponent.super.canInsert(direction, volume, slot);
	}

	/** Override behavior to take {@link #extractPredicate} into account. */
	@Override
	public boolean canExtract(@Nullable Direction direction, FluidVolume volume, int slot) {
		return extractPredicate.test(direction, volume, slot) && FluidComponent.super.canExtract(direction, volume, slot);
	}

	/** Returns this component's size. */
	@Override
	public int getSize() {
		return size;
	}

	/** Returns this component's contents. */
	@Override
	public @NotNull Map<Integer, FluidVolume> getContents() {
		return contents;
	}

	/** Returns this component's listeners. */
	@Override
	public @NotNull List<Runnable> getListeners() {
		return listeners;
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (!(object instanceof SimpleFluidComponent)) return false;

		SimpleFluidComponent entries = (SimpleFluidComponent) object;

		return Objects.equals(contents, entries.contents);
	}

	/** Returns the hash for this volume. */
	@Override
	public int hashCode() {
		return Objects.hash(contents);
	}

	/** Returns this inventory's string representation. */
	@Override
	public String toString() {
		return String.format("Listeners: %s\nInsertion predicate: %s\n Extraction predicate: %s\nContents: \n%s", listeners.size(), insertPredicate, extractPredicate, getContents().entrySet().stream().map((entry) -> String.format("%s, %s", entry.getKey(), entry.getValue().toString())).collect(Collectors.joining("\n")));
	}
}
