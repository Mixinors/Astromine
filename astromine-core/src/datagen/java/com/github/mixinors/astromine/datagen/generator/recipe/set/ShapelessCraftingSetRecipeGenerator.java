/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
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

package com.github.mixinors.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

import com.github.mixinors.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShapelessCraftingSetRecipeGenerator extends CraftingSetRecipeGenerator {
	public final List<Ingredient> ingredients;
	public final List<MaterialItemType> types;

	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount) {
		super(input, output, outputCount);
		this.ingredients = new ArrayList<>();
		this.types = new ArrayList<>();
	}

	public ShapelessCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 1);
	}

	public ShapelessCraftingSetRecipeGenerator addIngredients(Ingredient... ingredients) {
		this.ingredients.addAll(Arrays.asList(ingredients));
		return this;
	}

	public ShapelessCraftingSetRecipeGenerator addTypes(MaterialItemType... types) {
		this.types.addAll(Arrays.asList(types));
		return this;
	}

	@Override
	public String getRecipeSuffix() {
		return "_from_" + input.getName();
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		ShapelessRecipeJsonFactory factory = ShapelessRecipeJsonFactory
				.create(set.getItem(output), outputCount)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.input(set.getIngredient(input));
		ingredients.forEach(factory::input);
		types.forEach(type -> factory.input(set.getIngredient(type)));
		factory.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getGeneratorName() {
		return "shapeless";
	}
}
