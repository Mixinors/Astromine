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

package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.client.rei.AstromineREIPlugin;
import com.github.chainmailstudios.astromine.common.recipe.ElectrolyzingRecipe;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ElectrolyzingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidVolume input;
	private final FluidVolume firstOutput;
	private final FluidVolume secondOutput;
	private final Identifier id;

	public ElectrolyzingDisplay(double energy, FluidVolume input, FluidVolume firstOutput, FluidVolume secondOutput, Identifier id) {
		this.energy = energy;
		this.input = input;
		this.firstOutput = firstOutput;
		this.secondOutput = secondOutput;
		this.id = id;
	}

	public ElectrolyzingDisplay(ElectrolyzingRecipe recipe) {
		this(recipe.getEnergyConsumed(), new FluidVolume(recipe.getInputFluid(), recipe.getInputAmount()), new FluidVolume(recipe.getFirstOutputFluid(), recipe.getFirstOutputAmount()), new FluidVolume(recipe.getSecondOutputFluid(), recipe.getSecondOutputAmount()), recipe
			.getId());
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Collections.singletonList(Collections.singletonList(AstromineREIPlugin.convertA2R(input)));
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public Identifier getRecipeCategory() {
		return AstromineREIPlugin.ELECTROLYZING;
	}

	@Override
	public List<List<EntryStack>> getResultingEntries() {
		return Arrays.asList(Collections.singletonList(AstromineREIPlugin.convertA2R(firstOutput)), Collections.singletonList(AstromineREIPlugin.convertA2R(secondOutput)));
	}

	public double getEnergy() {
		return energy;
	}
}
