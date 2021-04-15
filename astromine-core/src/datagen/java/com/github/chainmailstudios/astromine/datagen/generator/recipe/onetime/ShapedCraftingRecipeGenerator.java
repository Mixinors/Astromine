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

package com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final String[] pattern;

	public ShapedCraftingRecipeGenerator(ItemConvertible output, int outputCount, String... pattern) {
		super(output, outputCount);
		this.ingredients = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingRecipeGenerator(ItemConvertible output, String... pattern) {
		this(output, 1, pattern);
	}

	public ShapedCraftingRecipeGenerator addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
		return this;
	}

	@Override
	public void generate(RecipeData recipes) {
		if (ingredients.size() == 0) throw new IllegalStateException("recipe must have at least one ingredient");
		else if (pattern.length == 0) throw new IllegalStateException("recipe must have a pattern");
		else {
			ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory
					.create(output, outputCount)
					.criterion("impossible", new ImpossibleCriterion.Conditions());
			for (String s : pattern) {
				factory.pattern(s);
			}
			ingredients.forEach(factory::input);
			factory.offerTo(recipes, getRecipeId());
		}
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
