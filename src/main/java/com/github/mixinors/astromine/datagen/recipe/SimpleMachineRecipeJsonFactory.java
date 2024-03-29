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

import com.github.mixinors.astromine.common.recipe.base.input.EnergyInputRecipe;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public abstract class SimpleMachineRecipeJsonFactory<T extends EnergyInputRecipe> extends ItemOutputMachineRecipeJsonFactory<T> {
	private static final String INPUT_KEY = "input";
	
	private final Ingredient input;
	
	protected SimpleMachineRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy, RecipeSerializer<T> serializer) {
		super(output, outputCount, processingTime, energy, serializer);
		
		this.input = input;
	}
	
	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new SimpleMachineRecipeJsonProvider<>(recipeId, this.input, this.output, this.outputCount, this.processingTime, this.energy, this.serializer));
	}
	
	public static class SimpleMachineRecipeJsonProvider<T extends EnergyInputRecipe> extends ItemOutputMachineRecipeJsonProvider<T> {
		private final Ingredient input;
		
		public SimpleMachineRecipeJsonProvider(Identifier recipeId, Ingredient input, Item output, int outputCount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, output, outputCount, processingTime, energy, serializer);
			
			this.input = input;
		}
		
		@Override
		public void serialize(JsonObject json) {
			json.add(INPUT_KEY, this.input.toJson());
			
			super.serialize(json);
		}
	}
}
