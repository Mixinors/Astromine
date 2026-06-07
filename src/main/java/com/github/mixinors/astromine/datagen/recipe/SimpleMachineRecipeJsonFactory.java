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
import com.github.mixinors.astromine.common.recipe.result.ItemResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public abstract class SimpleMachineRecipeJsonFactory<T extends EnergyInputRecipe> extends ItemOutputMachineRecipeJsonFactory<T> {
	private static final String INPUT_KEY = "input";
	
	private final Ingredient input;
	
	protected SimpleMachineRecipeJsonFactory(Ingredient input, ItemLike output, int outputCount, int processingTime, int energy, RecipeSerializer<T> serializer) {
		super(output, outputCount, processingTime, energy, serializer);
		
		this.input = input;
	}
	
	protected com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient input() {
		return itemIngredient(this.input);
	}
	
	protected ItemResult output() {
		return new ItemResult(new ItemStack(this.output, this.outputCount));
	}
}
