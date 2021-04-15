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

package com.github.chainmailstudios.astromine.datagen.generator.recipe.set;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.CookingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class BlastingSetRecipeGenerator extends CookingSetRecipeGenerator {
	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, float experience) {
		super(input, output, time, experience);
	}

	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output, float experience) {
		this(input, output, 100, experience);
	}

	public BlastingSetRecipeGenerator(MaterialItemType input, MaterialItemType output) {
		this(input, output, 100, 0.1f);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		CookingRecipeJsonFactory
				.createBlasting(
						set.getIngredient(input),
						set.getItem(output),
						experience,
						time)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, getRecipeId(set));
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_blasting_" + input.getName();
	}
}
