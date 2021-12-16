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
	private final Item output;
	private final int outputCount;
	private final Ingredient input;
	private final int processingTime;
	private final int energy;
	@Nullable
	private String group;
	private final AbstractRecipeSerializer<T> serializer;

	protected MachineRecipeJsonFactory(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		this.output = output.asItem();
		this.outputCount = outputCount;
		this.input = input;
		this.processingTime = processingTime;
		this.energy = energy;
		this.serializer = serializer;
	}

	public static PressingRecipeJsonFactory createPressing(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new PressingRecipeJsonFactory(input, output, outputCount, processingTime, energy, PressingRecipe.Serializer.INSTANCE);
	}

	public static TrituratingRecipeJsonFactory createTriturating(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new TrituratingRecipeJsonFactory(input, output, outputCount, processingTime, energy, TrituratingRecipe.Serializer.INSTANCE);
	}

	public static WireMillingRecipeJsonFactory createWireMilling(Ingredient input, ItemConvertible output, int outputCount, int processingTime, int energy) {
		return new WireMillingRecipeJsonFactory(input, output, outputCount, processingTime, energy, WireMillingRecipe.Serializer.INSTANCE);
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
		this.group = group;
		return this;
	}

	@Override
	public Item getOutputItem() {
		return output;
	}

	public int getEnergy() {
		return energy;
	}

	@Override
	public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
		exporter.accept(new MachineRecipeJsonProvider<T>(recipeId, this.group == null ? "" : this.group, this.input, this.output, this.outputCount, this.processingTime, this.energy, this.serializer));
	}

	public record MachineRecipeJsonProvider<T extends EnergyConsumingRecipe<Inventory>>(Identifier recipeId,
																						String group,
																						Ingredient input, Item output,
																						int outputCount,
																						int processingTime, int energy,
																						RecipeSerializer<T> serializer)
			implements RecipeJsonProvider {

		@Override
		public void serialize(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}
			json.add("input", this.input.toJson());
			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", Registry.ITEM.getId(this.output).toString());
			outputJson.addProperty("count", outputCount);
			json.add("output", outputJson);
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
			return null;
		}

		@Nullable
		@Override
		public Identifier getAdvancementId() {
			return null;
		}

		@Override
		public Identifier getRecipeId() {
			return this.recipeId;
		}
	}
}
