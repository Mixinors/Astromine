package com.github.mixinors.astromine.datagen.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public abstract class SimpleMachineRecipeJsonFactory<T extends EnergyConsumingRecipe> extends ItemOutputMachineRecipeJsonFactory<T> {
	private final Ingredient input;

	protected SimpleMachineRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		super(output, outputCount, processingTime, energy, serializer);
		this.input = input;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new SimpleMachineRecipeJsonProvider<>(recipeId, this.input, this.output, this.outputCount, this.processingTime, this.energy, this.serializer));
	}

	public static class SimpleMachineRecipeJsonProvider<T extends EnergyConsumingRecipe> extends ItemOutputMachineRecipeJsonProvider<T> {
		private final Ingredient input;

		public SimpleMachineRecipeJsonProvider(Identifier recipeId, Ingredient input, Item output, int outputCount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, output, outputCount, processingTime, energy, serializer);
			this.input = input;
		}

		@Override
		public void serialize(JsonObject json) {
			json.add("input", this.input.toJson());
			super.serialize(json);
		}
	}
}
