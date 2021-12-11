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

package com.github.mixinors.astromine.datagen.generator.recipe.onetime;

import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonFactory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import com.github.mixinors.astromine.datagen.generator.recipe.onetime.base.OneTimeRecipeGenerator;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class StonecuttingRecipeGenerator extends OneTimeRecipeGenerator {
	public final Ingredient input;
	public final String suffix;

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, int outputCount, String suffix) {
		super(output, outputCount);
		this.input = input;
		this.suffix = suffix;
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input) {
		this(output, input, 1, "");
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, int outputCount) {
		this(output, input, outputCount, "");
	}

	public StonecuttingRecipeGenerator(ItemConvertible output, Ingredient input, String suffix) {
		this(output, input, 1, suffix);
	}

	@Override
	public String getGeneratorName() {
		return "stonecutting";
	}

	@Override
	public Identifier getRecipeId() {
		Identifier id = super.getRecipeId();
		String path = id.getPath() + "_from_stonecutting";
		if (!suffix.isEmpty()) path = path + "_" + suffix;
		return new Identifier(id.getNamespace(), path);
	}

	@Override
	public void generate(RecipeData data) {
		SingleItemRecipeJsonFactory
				.createStonecutting(input, output, outputCount)
				.criterion("impossible", new ImpossibleCriterion.Conditions())
				.offerTo(data, getRecipeId());
	}
}
