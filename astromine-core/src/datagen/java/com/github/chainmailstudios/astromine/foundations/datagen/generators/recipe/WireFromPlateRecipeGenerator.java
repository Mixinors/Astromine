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

package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.common.utilities.GeneratorUtilities;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.SetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.foundations.common.recipe.WireCuttingRecipe;
import com.google.gson.JsonObject;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;

public class WireFromPlateRecipeGenerator implements SetRecipeGenerator {
	private final MaterialItemType input;
	private final Ingredient tool;
	private final MaterialItemType output;
	private final int outputCount;

	public WireFromPlateRecipeGenerator(MaterialItemType input, Ingredient tool, MaterialItemType output, int outputCount) {
		this.input = input;
		this.tool = tool;
		this.output = output;
		this.outputCount = outputCount;
	}

	@Override
	public String getGeneratorName() {
		return "plate_to_wire";
	}

	@Override
	public boolean shouldGenerate(MaterialSet set) {
		return set.hasType(input) && set.hasType(output);
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		recipes.accept(GeneratorUtilities.Providers.createProvider(WireCuttingRecipe.Serializer.INSTANCE, getRecipeId(set), json -> {
			json.add("input", set.getIngredient(input).toJson());
			json.add("tool", tool.toJson());

			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", Registry.ITEM.getId(set.getItem(output).asItem()).toString());
			outputJson.addProperty("count", outputCount);

			json.add("output", outputJson);
		}));
	}

	public String getRecipeSuffix() {
		return "_from_plate";
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + getRecipeSuffix();
	}

	@Override
	public MaterialItemType getOutput() {
		return output;
	}
}
