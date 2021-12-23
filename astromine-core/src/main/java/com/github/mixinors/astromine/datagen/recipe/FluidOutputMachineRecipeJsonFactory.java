package com.github.mixinors.astromine.datagen.recipe;

import com.google.gson.JsonObject;

import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import dev.architectury.core.AbstractRecipeSerializer;

import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class FluidOutputMachineRecipeJsonFactory<T extends EnergyConsumingRecipe> extends EnergyInputMachineRecipeJsonFactory<T> {
	protected final Fluid output;
	protected final int outputAmount;

	protected FluidOutputMachineRecipeJsonFactory(Fluid output, int outputAmount, int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
		super(processingTime, energy, serializer);
		this.output = output;
		this.outputAmount = outputAmount;
	}

	@Override
	public Fluid getOutputFluid() {
		return output;
	}

	@Override
	public OutputType getOutputType() {
		return OutputType.FLUID;
	}

	@Override
	public Identifier getOutputId() {
		return getFluidId(getOutputFluid());
	}

	public abstract static class FluidOutputMachineRecipeJsonProvider<T extends EnergyConsumingRecipe> extends EnergyInputMachineRecipeJsonProvider<T> {
		private final Fluid output;
		private final int outputAmount;

		public FluidOutputMachineRecipeJsonProvider(Identifier recipeId, Fluid output, int outputAmount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, processingTime, energy, serializer);
			this.output = output;
			this.outputAmount = outputAmount;
		}

		@Override
		public void serialize(JsonObject json) {
			JsonObject outputJson = new JsonObject();
			outputJson.addProperty("fluid", Registry.FLUID.getId(this.output).toString());
			outputJson.addProperty("amount", outputAmount);
			json.add("output", outputJson);
			super.serialize(json);
		}
	}
}
