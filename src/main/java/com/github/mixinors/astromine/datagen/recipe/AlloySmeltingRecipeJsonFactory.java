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

import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.mixinors.astromine.common.util.IngredientUtils;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AlloySmeltingRecipeJsonFactory extends ItemOutputMachineRecipeJsonFactory<AlloySmeltingRecipe> {
	private static final String FIRST_INPUT_KEY = "first_input";
	private static final String SECOND_INPUT_KEY = "second_input";
	
	private final Ingredient firstInput;
	private final int firstCount;
	
	private final Ingredient secondInput;
	private final int secondCount;
	
	protected AlloySmeltingRecipeJsonFactory(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemConvertible output, int outputCount, int processingTime, int energy) {
		super(output, outputCount, processingTime, energy, AlloySmeltingRecipe.Serializer.INSTANCE);
		
		this.firstInput = firstInput;
		this.firstCount = firstCount;
		
		this.secondInput = secondInput;
		this.secondCount = secondCount;
	}
	
	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new AlloySmeltingRecipeProvider(recipeId, this.firstInput, this.firstCount, this.secondInput, this.secondCount, this.output, this.outputCount, this.processingTime, this.energy, this.serializer));
	}
	
	@Override
	public String getName() {
		return "alloy_smelting";
	}
	
	public static class AlloySmeltingRecipeProvider extends ItemOutputMachineRecipeJsonProvider<AlloySmeltingRecipe> {
		private final Ingredient firstInput;
		private final int firstCount;
		
		private final Ingredient secondInput;
		private final int secondCount;
		
		public AlloySmeltingRecipeProvider(Identifier recipeId, Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, Item output, int outputCount, int processingTime, int energy, RecipeSerializer<AlloySmeltingRecipe> serializer) {
			super(recipeId, output, outputCount, processingTime, energy, serializer);
			
			this.firstInput = firstInput;
			this.firstCount = firstCount;
			this.secondInput = secondInput;
			this.secondCount = secondCount;
		}
		
		@Override
		public void serialize(JsonObject json) {
			json.add(FIRST_INPUT_KEY, IngredientUtils.toJsonWithCount(this.firstInput, firstCount));
			json.add(SECOND_INPUT_KEY, IngredientUtils.toJsonWithCount(this.secondInput, secondCount));
			super.serialize(json);
		}
	}
}
