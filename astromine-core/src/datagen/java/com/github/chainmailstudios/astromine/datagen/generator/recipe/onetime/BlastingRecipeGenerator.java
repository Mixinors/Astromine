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
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.CookingRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class BlastingRecipeGenerator extends CookingRecipeGenerator {
	public BlastingRecipeGenerator(Ingredient input, ItemConvertible output, int time, float experience) {
		super(input, output, time, experience);
	}

	public BlastingRecipeGenerator(Ingredient input, ItemConvertible output, float experience) {
		this(input, output, 100, experience);
	}

	public BlastingRecipeGenerator(Ingredient input, ItemConvertible output) {
		this(input, output, 100, 0.1f);
	}

	@Override
	public Identifier getRecipeId() {
		Identifier id = super.getRecipeId();
		return new Identifier(id.getNamespace(), id.getPath() + "_from_blasting");
	}

	@Override
	public void generate(RecipeData recipes) {
		CookingRecipeJsonFactory
				.createBlasting(
						input,
						output,
						experience,
						time)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(recipes, getRecipeId());
	}
}
