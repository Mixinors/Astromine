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
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AMRecipeSerializers {
	private static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, AMCommon.MOD_ID);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WireCuttingRecipe>> WIRE_CUTTING = register(WireCuttingRecipe.Serializer.ID, WireCuttingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TrituratingRecipe>> TRITURATING = register(TrituratingRecipe.Serializer.ID, TrituratingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PressingRecipe>> PRESSING = register(PressingRecipe.Serializer.ID, PressingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<WireMillingRecipe>> WIRE_MILLING = register(WireMillingRecipe.Serializer.ID, WireMillingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidGeneratingRecipe>> LIQUID_GENERATING = register(FluidGeneratingRecipe.Serializer.ID, FluidGeneratingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ElectrolyzingRecipe>> ELECTROLYZING = register(ElectrolyzingRecipe.Serializer.ID, ElectrolyzingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RefiningRecipe>> REFINING = register(RefiningRecipe.Serializer.ID, RefiningRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FluidMixingRecipe>> FLUID_MIXING = register(FluidMixingRecipe.Serializer.ID, FluidMixingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AlloySmeltingRecipe>> ALLOY_SMELTING = register(AlloySmeltingRecipe.Serializer.ID, AlloySmeltingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SolidifyingRecipe>> SOLIDIFYING = register(SolidifyingRecipe.Serializer.ID, SolidifyingRecipe.Serializer.INSTANCE);
	
	public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MeltingRecipe>> MELTING = register(MeltingRecipe.Serializer.ID, MeltingRecipe.Serializer.INSTANCE);
	
	
	public static void init() {
		REGISTRY.register(AMCommon.modEventBus());
	}
	
	private static <T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> register(ResourceLocation id, RecipeSerializer<T> serializer) {
		return REGISTRY.register(id.getPath(), () -> serializer);
	}
}
