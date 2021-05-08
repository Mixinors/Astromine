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

package com.github.mixinors.astromine.registry;

import com.github.mixinors.astromine.common.recipe.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AMRecipeSerializers {
	public static final RecipeSerializer<AltarRecipe> INFUSING = Registry.register(Registry.RECIPE_SERIALIZER, AltarRecipe.Serializer.ID, AltarRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<WireCuttingRecipe> WIRE_CUTTING = Registry.register(Registry.RECIPE_SERIALIZER, WireCuttingRecipe.Serializer.ID, WireCuttingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<TrituratingRecipe> TRITURATING = Registry.register(Registry.RECIPE_SERIALIZER, TrituratingRecipe.Serializer.ID, TrituratingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<PressingRecipe> PRESSING = Registry.register(Registry.RECIPE_SERIALIZER, PressingRecipe.Serializer.ID, PressingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<WireMillingRecipe> WIREMILLING = Registry.register(Registry.RECIPE_SERIALIZER, WireMillingRecipe.Serializer.ID, WireMillingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<FluidGeneratingRecipe> LIQUID_GENERATING = Registry.register(Registry.RECIPE_SERIALIZER, FluidGeneratingRecipe.Serializer.ID, FluidGeneratingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<ElectrolyzingRecipe> ELECTROLYZING = Registry.register(Registry.RECIPE_SERIALIZER, ElectrolyzingRecipe.Serializer.ID, ElectrolyzingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<RefiningRecipe> REFINING = Registry.register(Registry.RECIPE_SERIALIZER, RefiningRecipe.Serializer.ID, RefiningRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<FluidMixingRecipe> FLUID_MIXING = Registry.register(Registry.RECIPE_SERIALIZER, FluidMixingRecipe.Serializer.ID, FluidMixingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<AlloySmeltingRecipe> ALLOY_SMELTING = Registry.register(Registry.RECIPE_SERIALIZER, AlloySmeltingRecipe.Serializer.ID, AlloySmeltingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<SolidifyingRecipe> SOLIDIFYING = Registry.register(Registry.RECIPE_SERIALIZER, SolidifyingRecipe.Serializer.ID, SolidifyingRecipe.Serializer.INSTANCE);
	
	public static final RecipeSerializer<MeltingRecipe> MELTING = Registry.register(Registry.RECIPE_SERIALIZER, MeltingRecipe.Serializer.ID, MeltingRecipe.Serializer.INSTANCE);
	
	
	public static void init() {

	}
}
