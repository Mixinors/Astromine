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
import net.minecraft.util.registry.Registry;

public abstract class ItemOutputMachineRecipeJsonFactory<T extends EnergyConsumingRecipe<Inventory>> extends MachineRecipeJsonFactory<T> {
	protected final Item output;
	protected final int outputCount;

	protected ItemOutputMachineRecipeJsonFactory(ItemConvertible output, int outputCount, int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		super(processingTime, energy, serializer);
		this.output = output.asItem();
		this.outputCount = outputCount;
	}

	@Override
	public Item getOutputItem() {
		return output;
	}

	public abstract static class ItemOutputMachineRecipeJsonProvider<T extends EnergyConsumingRecipe<Inventory>> extends MachineRecipeJsonProvider<T> implements RecipeJsonProvider {
		private final Item output;
		private final int outputCount;

		public ItemOutputMachineRecipeJsonProvider(Identifier recipeId, Item output, int outputCount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, processingTime, energy, serializer);
			this.output = output;
			this.outputCount = outputCount;
		}

		@Override
		public void serialize(JsonObject json) {
			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("item", Registry.ITEM.getId(this.output).toString());
			outputJson.addProperty("count", outputCount);
			json.add("output", outputJson);
			super.serialize(json);
		}
	}
}
