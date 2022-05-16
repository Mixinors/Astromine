/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 Mixinors
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

import com.github.mixinors.astromine.common.recipe.base.input.EnergyInputRecipe;
import com.google.gson.JsonObject;
import dev.architectury.core.AbstractRecipeSerializer;
import net.minecraft.fluid.Fluid;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public abstract class FluidOutputMachineRecipeJsonFactory<T extends EnergyInputRecipe> extends EnergyInputMachineRecipeJsonFactory<T> {
	protected final Fluid output;
	protected final long outputAmount;
	
	protected FluidOutputMachineRecipeJsonFactory(Fluid output, long outputAmount, int processingTime, int energy, AbstractRecipeSerializer<T> serializer) {
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
	
	public abstract static class FluidOutputMachineRecipeJsonProvider<T extends EnergyInputRecipe> extends EnergyInputMachineRecipeJsonProvider<T> {
		private final Fluid output;
		private final long outputAmount;
		
		public FluidOutputMachineRecipeJsonProvider(Identifier recipeId, Fluid output, long outputAmount, int processingTime, int energy, RecipeSerializer<T> serializer) {
			super(recipeId, processingTime, energy, serializer);
			
			this.output = output;
			this.outputAmount = outputAmount;
		}
		
		@Override
		public void serialize(JsonObject json) {
			var outputJson = new JsonObject();
			outputJson.addProperty("fluid", Registry.FLUID.getId(this.output).toString());
			outputJson.addProperty("amount", outputAmount);
			json.add("output", outputJson);
			super.serialize(json);
		}
	}
}
