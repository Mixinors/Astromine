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
import com.github.mixinors.astromine.common.recipe.result.ItemResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class AlloySmeltingRecipeJsonFactory extends ItemOutputMachineRecipeJsonFactory<AlloySmeltingRecipe> {
	private final Ingredient firstInput;
	private final int firstCount;
	
	private final Ingredient secondInput;
	private final int secondCount;
	
	protected AlloySmeltingRecipeJsonFactory(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemLike output, int outputCount, int processingTime, int energy) {
		super(output, outputCount, processingTime, energy, AlloySmeltingRecipe.Serializer.INSTANCE);
		
		this.firstInput = firstInput;
		this.firstCount = firstCount;
		
		this.secondInput = secondInput;
		this.secondCount = secondCount;
	}
	
	@Override
	protected AlloySmeltingRecipe createRecipe(ResourceLocation recipeId) {
		return new AlloySmeltingRecipe(
				recipeId,
				itemIngredient(this.firstInput, this.firstCount),
				itemIngredient(this.secondInput, this.secondCount),
				new ItemResult(new ItemStack(this.output, this.outputCount)),
				this.energy,
				this.processingTime
		);
	}
	
	@Override
	public String getName() {
		return "alloy_smelting";
	}
}
