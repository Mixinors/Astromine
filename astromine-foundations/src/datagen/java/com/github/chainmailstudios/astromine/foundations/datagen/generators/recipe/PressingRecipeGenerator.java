package com.github.chainmailstudios.astromine.foundations.datagen.generators.recipe;

import com.github.chainmailstudios.astromine.common.recipe.PressingRecipe;
import com.github.chainmailstudios.astromine.foundations.datagen.AstromineFoundationsDatagen;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialItemType;
import com.github.chainmailstudios.astromine.foundations.datagen.MaterialSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;

public class PressingRecipeGenerator extends EnergyProcessingRecipeGenerator {
	public PressingRecipeGenerator(MaterialItemType input, int inputCount, MaterialItemType output, int outputCount, int time, int energyConsumed) {
		super(input, inputCount, output, outputCount, time, energyConsumed);
	}

	public PressingRecipeGenerator(MaterialItemType input, MaterialItemType output, int outputCount, int time, int energyConsumed) {
		this(input, 1, output, outputCount, time, energyConsumed);
	}

	public PressingRecipeGenerator(MaterialItemType input, MaterialItemType output, int time, int energyConsumed) {
		this(input, 1, output, 1, time, energyConsumed);
	}

	@Override
	public String getRecipeName(MaterialSet set) {
		return set.getItemIdPath(output) + "_from_pressing_" + input.getName();
	}

	@Override
	public void generateRecipe(RecipeData recipes, MaterialSet set) {
		recipes.accept(AstromineFoundationsDatagen.Providers.createProvider(PressingRecipe.Serializer.INSTANCE, getRecipeId(set), json -> {
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
			json.addProperty("energy_consumed", energyConsumed);
		}));
	}

	@Override
	public String getGeneratorName() {
		return "pressing";
	}
}
