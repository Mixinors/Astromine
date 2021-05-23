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
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.recipe.Ingredient;

import com.github.mixinors.astromine.datagen.generator.recipe.set.base.CraftingSetRecipeGenerator;
import com.github.mixinors.astromine.datagen.material.MaterialItemType;
import com.github.mixinors.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import java.util.HashMap;
import java.util.Map;

public class ShapedCraftingSetRecipeGenerator extends CraftingSetRecipeGenerator {
	public final Map<Character, Ingredient> ingredients;
	public final Map<Character, MaterialItemType> types;
	public final String[] pattern;

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, String... pattern) {
		super(input, output, outputCount);
		this.ingredients = new HashMap<>();
		this.types = new HashMap<>();
		this.pattern = pattern;
	}

	public ShapedCraftingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, String... pattern) {
		this(input, output, 1, pattern);
	}

	public ShapedCraftingSetRecipeGenerator addIngredient(char c, Ingredient ingredient) {
		this.ingredients.put(c, ingredient);
		return this;
	}

	public ShapedCraftingSetRecipeGenerator addType(char c, MaterialItemType type) {
		this.types.put(c, type);
		return this;
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		if (pattern.length == 0) throw new IllegalStateException("recipe must have a pattern");
		else {
			ShapedRecipeJsonFactory factory = ShapedRecipeJsonFactory
					.create(set.getItem(output), outputCount)
					.criterion("impossible", new ImpossibleCriterion.Conditions());
			for (String s : pattern) {
				factory.pattern(s);
			}
			if (patternContains('#')) factory.input('#', set.getIngredient(input));
			if (patternContains('I')) factory.input('I', set.getItem(input));
			ingredients.forEach(factory::input);
			types.forEach((c, type) -> factory.input(c, set.getIngredient(type)));
			factory.offerTo(recipes, getRecipeId(set));
		}
	}

	public boolean patternContains(char c) {
		for (String s : pattern) {
			if (s.contains(String.valueOf(c))) return true;
		}
		return false;
	}

	@Override
	public String getGeneratorName() {
		return "shaped";
	}
}
