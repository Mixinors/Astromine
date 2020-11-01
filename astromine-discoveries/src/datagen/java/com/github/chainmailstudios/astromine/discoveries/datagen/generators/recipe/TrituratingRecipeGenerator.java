package com.github.chainmailstudios.astromine.discoveries.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.common.utilities.GeneratorUtilities;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.set.base.EnergyProcessingSetRecipeGenerator;
import com.github.chainmailstudios.astromine.datagen.material.MaterialItemType;
import com.github.chainmailstudios.astromine.datagen.material.MaterialSet;
import com.github.chainmailstudios.astromine.technologies.common.recipe.TrituratingRecipe;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TrituratingRecipeGenerator extends EnergyProcessingSetRecipeGenerator {
	public TrituratingRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energy) {
		super(input, inputCount, output, outputCount, time, energy);
	}

	public TrituratingRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, int time, int energy) {
		this(input, 1, output, outputCount, time, energy);
	}

	public TrituratingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, int energy) {
		this(input, 1, output, 1, time, energy);
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_triturating_" + input.getName();
	}

	@Override
	public void generate(RecipeData recipes, MaterialSet set) {
		recipes.accept(GeneratorUtilities.Providers.createProvider(TrituratingRecipe.Serializer.INSTANCE, getRecipeId(set), json -> {
			JsonElement inputJson = set.getIngredient(input).toJson();
			if (inputJson.isJsonObject()) {
				inputJson.getAsJsonObject().addProperty("count", inputCount);
			}

			json.add("input", inputJson);

			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", set.getItemId(output).toString());
			outputJson.addProperty("count", outputCount);

			json.add("output", outputJson);
			json.addProperty("time", time);
			json.addProperty("energy", energy);
		}));
	}

	@Override
	public String getGeneratorName() {
		return "triturating";
	}
}
