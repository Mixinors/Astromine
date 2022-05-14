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
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MeltingRecipeJsonFactory extends FluidOutputMachineRecipeJsonFactory<MeltingRecipe> {
	private final Ingredient input;
	
	protected MeltingRecipeJsonFactory(Ingredient input, Fluid output, long outputAmount, int processingTime, int energy) {
		super(output, outputAmount, processingTime, energy, MeltingRecipe.Serializer.INSTANCE);
		this.input = input;
	}
	
	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new MeltingRecipeJsonProvider<>(recipeId, this.input, this.output, this.outputAmount, this.processingTime, this.energy, this.serializer));
	}
	
	@Override
	public String getName() {
		return "melting";
	}
	
	public static class MeltingRecipeJsonProvider<T extends EnergyConsumingRecipe> extends FluidOutputMachineRecipeJsonProvider<T> {
		private final Ingredient input;
		
		public MeltingRecipeJsonProvider(Identifier recipeId, Ingredient input, Fluid output, long outputAmount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, output, outputAmount, processingTime, energy, serializer);
			this.input = input;
		}
		
		@Override
		public void serialize(JsonObject json) {
			json.add("input", this.input.toJson());
			super.serialize(json);
		}
	}
}
