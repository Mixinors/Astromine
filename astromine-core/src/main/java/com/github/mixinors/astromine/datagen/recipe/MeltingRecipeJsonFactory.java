package com.github.mixinors.astromine.datagen.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.MeltingRecipe;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class MeltingRecipeJsonFactory extends FluidOutputMachineRecipeJsonFactory<MeltingRecipe> {
	private final Ingredient input;

	protected MeltingRecipeJsonFactory(Ingredient input, Fluid output, int outputAmount, int processingTime, int energy) {
		super(output, outputAmount, processingTime, energy, MeltingRecipe.Serializer.INSTANCE);
		this.input = input;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new MeltingRecipeJsonProvider<>(recipeId, this.input, this.output, this.outputAmount, this.processingTime, this.energy, this.serializer));
	}

	@Override
	public String getName() {
		return "melting";
	}

	public static class MeltingRecipeJsonProvider<T extends EnergyConsumingRecipe> extends FluidOutputMachineRecipeJsonProvider<T> {
		private final Ingredient input;

		public MeltingRecipeJsonProvider(Identifier recipeId, Ingredient input, Fluid output, int outputAmount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, output, outputAmount, processingTime, energy, serializer);
			this.input = input;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("input", this.input.toJson());
			super.serialize(json);
		}
	}
}
