package com.github.chainmailstudios.astromine.technologies.datagen.generators.recipe.onetime;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.utilities.GeneratorUtilities;
import com.github.chainmailstudios.astromine.datagen.generator.recipe.onetime.base.EnergyProcessingRecipeGenerator;
import com.github.chainmailstudios.astromine.technologies.common.recipe.TrituratingRecipe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.shedaniel.cloth.api.datagen.v1.RecipeData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class TrituratingRecipeGenerator extends EnergyProcessingRecipeGenerator {
	private final String name;

	public TrituratingRecipeGenerator(String name, Ingredient input, int inputCount, ItemLike output, int outputCount, int time, int energy) {
		super(input, inputCount, output, outputCount, time, energy);
		this.name = name;
	}

	public TrituratingRecipeGenerator(String name, Ingredient input, ItemLike output, int outputCount, int time, int energy) {
		this(name, input, 1, output, outputCount, time, energy);
	}

	public TrituratingRecipeGenerator(String name, Ingredient input, ItemLike output, int time, int energy) {
		this(name, input, 1, output, 1, time, energy);
	}

	@Override
	public ResourceLocation getRecipeId() {
		return AstromineCommon.identifier(name);
	}

	@Override
	public void generate(RecipeData recipes) {
		recipes.accept(GeneratorUtilities.Providers.createProvider(TrituratingRecipe.Serializer.INSTANCE, getRecipeId(), json -> {
			JsonElement inputJson = input.toJson();
			if (inputJson.isJsonObject()) {
				inputJson.getAsJsonObject().addProperty("count", inputCount);
			}

			json.add("input", inputJson);

			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", Registry.ITEM.getKey(output.asItem()).toString());
			outputJson.addProperty("count", outputCount);

			json.add("output", outputJson);
			json.addProperty("time", time);
			json.addProperty("energy_input", energy);
		}));
	}

	@Override
	public String getGeneratorName() {
		return "triturating";
	}
}
