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

package com.github.mixinors.astromine.client.rei.electrolyzing;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class ElectrolyzingDisplay implements Display {
	private final double energy;
	private final FluidIngredient input;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final Identifier id;

	public ElectrolyzingDisplay(double energy, FluidIngredient input, FluidVolume firstOutput, FluidVolume secondOutput, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.id = id;
	}

	public ElectrolyzingDisplay(ElectrolyzingRecipe recipe) {
		this(recipe.getEnergyInput(), recipe.getInput(), recipe.getFirstOutput(), recipe.getSecondOutput(), recipe.getId());
	}

	@Override
	public Optional<Identifier> getDisplayLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return Collections.singletonList(EntryIngredient.of(Arrays.stream(input.getMatchingVolumes()).map(AMRoughlyEnoughItemsPlugin::convertToEntryStack).collect(Collectors.toList())));
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Arrays.asList(EntryIngredient.of(AMRoughlyEnoughItemsPlugin.convertToEntryStack(firstOutput)), EntryIngredient.of(AMRoughlyEnoughItemsPlugin.convertToEntryStack(secondOutput)));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AMRoughlyEnoughItemsPlugin.ELECTROLYZING;
	}

	public double getEnergy() {
		return energy;
	}
}
