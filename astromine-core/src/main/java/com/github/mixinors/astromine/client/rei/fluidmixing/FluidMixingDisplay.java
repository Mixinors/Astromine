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

package com.github.mixinors.astromine.client.rei.fluidmixing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.client.rei.AMREIPlugin;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.volume.fluid.FluidVolume;
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class FluidMixingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidIngredient firstIngredient;
	private final FluidIngredient secondIngredient;
	private final FluidVolume output;
	private final Identifier id;

	public FluidMixingDisplay(FluidMixingRecipe recipe) {
		this.energy = recipe.getEnergyInput();
		this.firstIngredient = recipe.getFirstInput();
		this.secondIngredient = recipe.getSecondInput();
		this.output = recipe.getFirstOutput();
		this.id = recipe.getId();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AMREIPlugin.FLUID_MIXING;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Arrays.asList(Arrays.stream(firstIngredient.getMatchingVolumes()).map(AMREIPlugin::convertToEntryStack).collect(Collectors.toList()), Arrays.stream(secondIngredient.getMatchingVolumes()).map(AMREIPlugin::convertToEntryStack).collect(
			Collectors.toList()));
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(Collections.singletonList(AMREIPlugin.convertToEntryStack(output)));
	}

	public double getEnergy() {
		return energy;
	}
}
