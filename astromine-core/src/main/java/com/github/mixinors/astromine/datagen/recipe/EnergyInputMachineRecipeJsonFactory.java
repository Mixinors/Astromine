package com.github.mixinors.astromine.datagen.recipe;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public abstract class EnergyInputMachineRecipeJsonFactory<T extends EnergyConsumingRecipe<Inventory>> extends MachineRecipeJsonFactory<T> {
	protected final int energy;

	protected EnergyInputMachineRecipeJsonFactory(int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		super(processingTime, serializer);
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public abstract static class EnergyInputMachineRecipeJsonProvider<T extends EnergyConsumingRecipe<Inventory>> extends MachineRecipeJsonProvider<T> {
		protected final int energy;

		public EnergyInputMachineRecipeJsonProvider(Identifier recipeId, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, processingTime, serializer);
			this.energy = energy;
		}

		@Override
		public void serialize(JsonObject json) {
			json.addProperty("energy_input", energy);
			super.serialize(json);
		}
	}
}
