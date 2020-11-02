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

package com.github.chainmailstudios.astromine.technologies.client.rei.refining;

import com.github.chainmailstudios.astromine.common.recipe.ingredient.FluidIngredient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.technologies.client.rei.AstromineTechnologiesRoughlyEnoughItemsPlugin;
import com.github.chainmailstudios.astromine.technologies.common.recipe.RefiningRecipe;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class RefiningDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidIngredient input;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final FluidVolume thirdOutput;
	private final FluidVolume fourthOutput;
	private final FluidVolume fifthOutput;
	private final FluidVolume sixthOutput;
	private final FluidVolume seventhOutput;
	private final Identifier id;

	public RefiningDisplay(double energy, FluidIngredient input, FluidVolume firstOutput, FluidVolume secondOutput, FluidVolume thirdOutput, FluidVolume fourthOutput, FluidVolume fifthOutput, FluidVolume sixthOutput, FluidVolume seventhOutput, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.thirdOutput = thirdOutput;
		this.fourthOutput = fourthOutput;
		this.fifthOutput = fifthOutput;
		this.sixthOutput = sixthOutput;
		this.seventhOutput = seventhOutput;
		this.id = id;
	}

	public RefiningDisplay(RefiningRecipe recipe) {
		this(recipe.getEnergy(), recipe.getIngredient(), recipe.getFirstOutputVolume(), recipe.getSecondOutputVolume(), recipe.getThirdOutputVolume(), recipe.getFourthOutputVolume(), recipe.getFifthOutputVolume(), recipe.getSixthOutputVolume(), recipe.getSeventhOutputVolume(),
			recipe.getId());
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Arrays.stream(input.getMatchingVolumes()).map(AstromineRoughlyEnoughItemsPlugin::convertA2R).collect(Collectors.toList()));
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineTechnologiesRoughlyEnoughItemsPlugin.REFINING;
	}

	@Override
	public List<List<EntryStack>> getResultingEntries() {
		return Arrays.asList(Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(firstOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(secondOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(
			thirdOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(fourthOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(fifthOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(
				sixthOutput)), Collections.singletonList(AstromineRoughlyEnoughItemsPlugin.convertA2R(seventhOutput)));
	}

	public double getEnergy() {
		return energy;
	}
}
