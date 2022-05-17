/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.client.rei.AMRoughlyEnoughItemsPlugin;
import com.github.mixinors.astromine.client.rei.base.input.EnergyInputDisplay;
import com.github.mixinors.astromine.common.recipe.FluidMixingRecipe;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FluidMixingDisplay extends EnergyInputDisplay {
	public FluidMixingDisplay(FluidMixingRecipe recipe) {
		super(
				List.of(
						EntryIngredients.of(VanillaEntryTypes.FLUID, Arrays.stream(recipe.getFirstInput().getMatchingVariants()).map(variant -> FluidStack.create(variant.getFluid(), recipe.getFirstInput().getAmount())).toList()),
						EntryIngredients.of(VanillaEntryTypes.FLUID, Arrays.stream(recipe.getSecondInput().getMatchingVariants()).map(variant -> FluidStack.create(variant.getFluid(), recipe.getSecondInput().getAmount())).toList())
				),
				List.of(EntryIngredients.of(recipe.getFluidOutput().toStack())),
				recipe.getTime(), recipe.getEnergyInput(), recipe.getId()
		);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AMRoughlyEnoughItemsPlugin.FLUID_MIXING;
	}
}
