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

package com.github.chainmailstudios.astromine.technologies.client.rei.fluidmixing;

import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.common.recipe.FluidMixingRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
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
		this.energy = recipe.getEnergy();
		this.firstIngredient = recipe.getFirstIngredient();
		this.secondIngredient = recipe.getSecondIngredient();
		this.output = recipe.getOutputVolume();
		this.id = recipe.getId();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.FLUID_MIXING;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Arrays.asList(Arrays.stream(firstIngredient.getMatchingVolumes()).map(AstromineRoughlyEnoughItemsPlugin::convertA2R).collect(Collectors.toList()), Arrays.stream(secondIngredient.getMatchingVolumes()).map(AstromineRoughlyEnoughItemsPlugin::convertA2R).collect(
			Collectors.toList()));
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(output)));
	}

	public double getEnergy() {
		return energy;
	}
}
