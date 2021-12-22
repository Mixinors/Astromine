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

package com.github.mixinors.astromine.client.rei.refining;

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.common.recipe.RefiningRecipe;
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
public class RefiningDisplay implements Display {
	private final double energy;
	private final FluidIngredient input;
	private final FluidVolume output;
	private final Identifier id;

	public RefiningDisplay(double energy, FluidIngredient input, FluidVolume output, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.output = output;
		this.id = id;
	}

	public RefiningDisplay(RefiningRecipe recipe) {
		this(recipe.getEnergyInput(), recipe.getIngredient(), recipe.getFluidOutput(), recipe.getId());
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
		return Collections.singletonList(EntryIngredient.of(AMRoughlyEnoughItemsPlugin.convertToEntryStack(output)));
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AMRoughlyEnoughItemsPlugin.REFINING;
	}

	public double getEnergy() {
		return energy;
	}
}
