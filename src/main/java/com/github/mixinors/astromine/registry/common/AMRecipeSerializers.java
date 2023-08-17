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

package com.github.mixinors.astromine.registry.common;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class AMRecipeSerializers {
	public static final RegistrySupplier<RecipeSerializer<WireCuttingRecipe>> WIRE_CUTTING = register(WireCuttingRecipe.Serializer.ID, WireCuttingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<TrituratingRecipe>> TRITURATING = register(TrituratingRecipe.Serializer.ID, TrituratingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<PressingRecipe>> PRESSING = register(PressingRecipe.Serializer.ID, PressingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<WireMillingRecipe>> WIRE_MILLING = register(WireMillingRecipe.Serializer.ID, WireMillingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<FluidGeneratingRecipe>> LIQUID_GENERATING = register(FluidGeneratingRecipe.Serializer.ID, FluidGeneratingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<ElectrolyzingRecipe>> ELECTROLYZING = register(ElectrolyzingRecipe.Serializer.ID, ElectrolyzingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<RefiningRecipe>> REFINING = register(RefiningRecipe.Serializer.ID, RefiningRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<FluidMixingRecipe>> FLUID_MIXING = register(FluidMixingRecipe.Serializer.ID, FluidMixingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<AlloySmeltingRecipe>> ALLOY_SMELTING = register(AlloySmeltingRecipe.Serializer.ID, AlloySmeltingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<SolidifyingRecipe>> SOLIDIFYING = register(SolidifyingRecipe.Serializer.ID, SolidifyingRecipe.Serializer.INSTANCE);
	
	public static final RegistrySupplier<RecipeSerializer<MeltingRecipe>> MELTING = register(MeltingRecipe.Serializer.ID, MeltingRecipe.Serializer.INSTANCE);
	
	
	public static void init() {
	}
	
	private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> register(Identifier id, RecipeSerializer<T> serializer) {
		return AMCommon.registry(Registry.RECIPE_SERIALIZER_KEY).register(id, () -> serializer);
	}
}
