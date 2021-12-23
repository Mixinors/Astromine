package com.github.mixinors.astromine.datagen.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.AlloySmeltingRecipe;
import com.github.mixinors.astromine.common.util.IngredientUtils;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class AlloySmeltingRecipeJsonFactory extends ItemOutputMachineRecipeJsonFactory<AlloySmeltingRecipe> {
	private final Ingredient firstInput;
	private final int firstCount;
	private final Ingredient secondInput;
	private final int secondCount;

	protected AlloySmeltingRecipeJsonFactory(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemConvertible output, int outputCount, int processingTime, int energy) {
		super(output, outputCount, processingTime, energy, AlloySmeltingRecipe.Serializer.INSTANCE);
		this.firstInput = firstInput;
		this.firstCount = firstCount;
		this.secondInput = secondInput;
		this.secondCount = secondCount;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new AlloySmeltingRecipeProvider(recipeId, this.firstInput, this.firstCount, this.secondInput, this.secondCount, this.output, this.outputCount, this.processingTime, this.energy, this.serializer));
	}

	@Override
	public String getName() {
		return "alloy_smelting";
	}

	public static class AlloySmeltingRecipeProvider extends ItemOutputMachineRecipeJsonProvider<AlloySmeltingRecipe> {
		private final Ingredient firstInput;
		private final int firstCount;
		private final Ingredient secondInput;
		private final int secondCount;

		public AlloySmeltingRecipeProvider(Identifier recipeId, Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, Item output, int outputCount, int processingTime, int energy, RecipeSerializer<AlloySmeltingRecipe> serializer) {
			super(recipeId, output, outputCount, processingTime, energy, serializer);
			this.firstInput = firstInput;
			this.firstCount = firstCount;
			this.secondInput = secondInput;
			this.secondCount = secondCount;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("first_input", IngredientUtils.toJsonWithCount(this.firstInput, firstCount));
			json.add("second_input", IngredientUtils.toJsonWithCount(this.secondInput, secondCount));
			super.serialize(json);
		}
	}
}
