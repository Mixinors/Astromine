/*
 * MIT License
 *
 * Copyright (c) 2020, 2021 Mixinors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
