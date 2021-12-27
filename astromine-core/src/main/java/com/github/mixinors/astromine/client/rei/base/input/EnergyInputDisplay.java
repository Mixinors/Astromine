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

package com.github.mixinors.astromine.client.rei.base.input;

import java.util.List;
import java.util.Optional;

import com.github.mixinors.astromine.client.rei.base.AMDisplay;

import net.minecraft.util.Identifier;

import me.shedaniel.rei.api.common.entry.EntryIngredient;

public abstract class EnergyInputDisplay implements AMDisplay {
	private final List<EntryIngredient> inputs;
	private final List<EntryIngredient> outputs;
	private final int timeRequired;
	private final long energyRequired;
	private final Identifier recipeId;

	public EnergyInputDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, int timeRequired, long energyRequired, Identifier recipeId) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.timeRequired = timeRequired;
		this.energyRequired = energyRequired;
		this.recipeId = recipeId;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return inputs;
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return outputs;
	}

	public int getTimeRequired() {
		return timeRequired;
	}

	public long getEnergyRequired() {
		return energyRequired;
	}

	@Override
	public Optional<Identifier> getDisplayLocation() {
		return Optional.ofNullable(this.recipeId);
	}
}
