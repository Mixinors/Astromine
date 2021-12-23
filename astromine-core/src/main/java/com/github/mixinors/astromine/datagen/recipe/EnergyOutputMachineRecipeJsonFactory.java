package com.github.mixinors.astromine.datagen.recipe;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public abstract class EnergyOutputMachineRecipeJsonFactory<T extends EnergyConsumingRecipe> extends MachineRecipeJsonFactory<T> {
	protected final int energy;

	protected EnergyOutputMachineRecipeJsonFactory(int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		super(processingTime, serializer);
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public abstract static class EnergyOutputMachineRecipeJsonProvider<T extends EnergyConsumingRecipe> extends MachineRecipeJsonProvider<T> {
		protected final int energy;

		public EnergyOutputMachineRecipeJsonProvider(Identifier recipeId, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, processingTime, serializer);
			this.energy = energy;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("energy_output", energy);
			super.serialize(json);
		}
	}
}
