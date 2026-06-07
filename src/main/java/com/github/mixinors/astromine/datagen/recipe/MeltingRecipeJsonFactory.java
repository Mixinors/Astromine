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

package com.github.mixinors.astromine.datagen.recipe;

import com.github.mixinors.astromine.common.recipe.MeltingRecipe;
import com.github.mixinors.astromine.common.recipe.result.FluidResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class MeltingRecipeJsonFactory extends FluidOutputMachineRecipeJsonFactory<MeltingRecipe> {
	private final Ingredient input;
	
	protected MeltingRecipeJsonFactory(Ingredient input, Fluid output, long outputAmount, int processingTime, int energy) {
		super(output, outputAmount, processingTime, energy, MeltingRecipe.Serializer.INSTANCE);
		
		this.input = input;
	}
	
	@Override
	protected MeltingRecipe createRecipe(ResourceLocation recipeId) {
		return new MeltingRecipe(
				recipeId,
				itemIngredient(this.input),
				new FluidResult(new FluidStack(this.output, (int) Math.min(this.outputAmount, Integer.MAX_VALUE))),
				this.energy,
				this.processingTime
		);
	}
	
	@Override
	public String getName() {
		return "melting";
	}
}
