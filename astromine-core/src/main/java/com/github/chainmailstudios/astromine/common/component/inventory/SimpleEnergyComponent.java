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

import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

/**
 * A simple implementation of {@link EnergyComponent}.
 */
public class SimpleEnergyComponent implements EnergyComponent {
	private final EnergyVolume content;

	private final List<Runnable> listeners = new ArrayList<>();

	/** Instantiates a {@link SimpleEnergyComponent}. */
	protected SimpleEnergyComponent(double size) {
		this.content = EnergyVolume.of(size, this::updateListeners);
	}

	/** Instantiates a {@link SimpleEnergyComponent}. */
	protected SimpleEnergyComponent(EnergyVolume volume) {
		this.content = volume;
		this.content.setRunnable(this::updateListeners);
	}

	/** Instantiates a {@link SimpleEnergyComponent}. */
	public static SimpleEnergyComponent of(double size) {
		return new SimpleEnergyComponent(size);
	}

	/** Instantiates a {@link SimpleEnergyComponent}. */
	public static SimpleEnergyComponent of(EnergyVolume volume) {
		return new SimpleEnergyComponent(volume);
	}

	/** Returns this component's volume. */
	@Override
	public EnergyVolume getVolume() {
		return content;
	}

	/** Returns this component's listeners. */
	@Override
	public List<Runnable> getListeners() {
		return listeners;
	}

	/** Asserts the equality of the objects. */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;

		if (object == null || getClass() != object.getClass()) return false;

		SimpleEnergyComponent that = (SimpleEnergyComponent) object;

		return Objects.equal(content, that.content);
	}

	/** Returns the hash for this volume. */
	@Override
	public int hashCode() {
		return Objects.hashCode(content);
	}

	/** Returns this inventory's string representation. */
	@Override
	public String toString() {
		return String.format("Listeners: %s\nContent: %s", listeners.size(), content);
	}
}
