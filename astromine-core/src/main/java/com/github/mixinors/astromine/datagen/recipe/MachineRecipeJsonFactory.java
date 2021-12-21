package com.github.mixinors.astromine.datagen.recipe;

import java.util.function.Consumer;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.PressingRecipe;
import com.github.mixinors.astromine.common.recipe.TrituratingRecipe;
import com.github.mixinors.astromine.common.recipe.WireMillingRecipe;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;
import org.jetbrains.annotations.Nullable;

import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class MachineRecipeJsonFactory<T extends EnergyConsumingRecipe<Inventory>> implements CraftingRecipeJsonFactory {
	protected final int processingTime;
	protected final int energy;
	protected final AbstractRecipeSerializer<T> serializer;

	protected MachineRecipeJsonFactory(int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		this.processingTime = processingTime;
		this.energy = energy;
		this.serializer = serializer;
	}

	public int getEnergy() {
		return energy;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter) {
		this.offerTo(exporter, CraftingRecipeJsonFactory.getItemId(this.getOutputItem()) + "_from_" + getName());
	}

	public abstract String getName();

	@Override
	public CraftingRecipeJsonFactory criterion(String name, CriterionConditions conditions) {
		// we don't use recipe advancements here!
		return this;
	}

	@Override
	public CraftingRecipeJsonFactory group(@Nullable String group) {
		// we don't use groups here!
		return this;
	}

	public static PressingRecipeJsonFactory createPressing(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new PressingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static TrituratingRecipeJsonFactory createTriturating(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new TrituratingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static WireMillingRecipeJsonFactory createWireMilling(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new WireMillingRecipeJsonFactory(input, output, outputCount, processingTime, energy);
	}

	public static AlloySmeltingRecipeJsonFactory createAlloySmelting(Ingredient firstInput, int firstCount, Ingredient secondInput, int secondCount, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new AlloySmeltingRecipeJsonFactory(firstInput, firstCount, secondInput,secondCount, output, outputCount, processingTime, energy);
	}

	public abstract static class MachineRecipeJsonProvider<T extends EnergyConsumingRecipe<Inventory>> implements RecipeJsonProvider {
		protected final Identifier recipeId;
		protected final int processingTime;
		protected final int energy;
		protected final RecipeSerializer<T> serializer;

		public MachineRecipeJsonProvider(Identifier recipeId, int processingTime, int energy, RecipeSerializer<T> serializer) {
			this.recipeId = recipeId;
			this.processingTime = processingTime;
			this.energy = energy;
			this.serializer = serializer;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("time", this.processingTime);
			json.addProperty("energy_input", this.energy);
		}

		@Override
		public RecipeSerializer<?> getSerializer() {
			return this.serializer;
		}

		@Nullable
		@Override
		public JsonObject toAdvancementJson() {
			// we don't use recipe advancements here!
			return null;
		}

		@Nullable
		@Override
		public Identifier getAdvancementId() {
			// we don't use recipe advancements here!
			return null;
		}

		@Override
		public Identifier getRecipeId() {
			return this.recipeId;
		}
	}
}
