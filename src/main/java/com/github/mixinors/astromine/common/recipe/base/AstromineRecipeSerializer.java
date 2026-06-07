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
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.mixinors.astromine.common.recipe.base;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.input.DoubleFluidInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.input.DoubleItemInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.input.EnergyInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.input.FluidInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.input.ItemInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.output.EnergyOutputRecipe;
import com.github.mixinors.astromine.common.recipe.base.output.FluidOutputRecipe;
import com.github.mixinors.astromine.common.recipe.base.output.ItemOutputRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.common.recipe.result.FluidResult;
import com.github.mixinors.astromine.common.recipe.result.ItemResult;
import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.stream.Stream;

public interface AstromineRecipeSerializer<T extends Recipe<?>> extends RecipeSerializer<T> {
	default ResourceLocation serializedRecipeId() {
		return AMCommon.id("serialized_recipe");
	}
	
	T fromJson(ResourceLocation identifier, JsonObject object);
	
	T fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer);
	
	void write(FriendlyByteBuf buffer, T recipe);
	
	@Override
	default MapCodec<T> codec() {
		return new MapCodec<>() {
			@Override
			public <O> Stream<O> keys(DynamicOps<O> ops) {
				return Stream.empty();
			}
			
			@Override
			public <O> DataResult<T> decode(DynamicOps<O> ops, MapLike<O> input) {
				try {
					var json = ops.convertTo(JsonOps.INSTANCE, ops.createMap(input.entries()));
					
					if (!json.isJsonObject()) {
						return DataResult.error(() -> "Expected Astromine recipe object");
					}
					
					return DataResult.success(fromJson(serializedRecipeId(), json.getAsJsonObject()));
				} catch (Exception exception) {
					return DataResult.error(exception::getMessage);
				}
			}
			
			@Override
			public <O> RecordBuilder<O> encode(T input, DynamicOps<O> ops, RecordBuilder<O> prefix) {
				var json = toJson(input);
				
				for (var entry : json.entrySet()) {
					prefix.add(entry.getKey(), JsonOps.INSTANCE.convertTo(ops, entry.getValue()));
				}
				
				return prefix;
			}
		};
	}
	
	@Override
	default StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return StreamCodec.of(this::write, buffer -> fromNetwork(serializedRecipeId(), buffer));
	}
	
	default JsonObject toJson(T recipe) {
		var json = new JsonObject();
		
		if (recipe instanceof DoubleItemInputRecipe doubleItemInputRecipe) {
			json.add("first_input", ItemIngredient.toJson(doubleItemInputRecipe.getFirstInput()));
			json.add("second_input", ItemIngredient.toJson(doubleItemInputRecipe.getSecondInput()));
		} else if (recipe instanceof ItemInputRecipe itemInputRecipe) {
			json.add("input", ItemIngredient.toJson(itemInputRecipe.getInput()));
		}
		
		if (recipe instanceof DoubleFluidInputRecipe doubleFluidInputRecipe) {
			json.add("first_input", FluidIngredient.toJson(doubleFluidInputRecipe.getFirstInput()));
			json.add("second_input", FluidIngredient.toJson(doubleFluidInputRecipe.getSecondInput()));
		} else if (recipe instanceof FluidInputRecipe fluidInputRecipe) {
			json.add("input", FluidIngredient.toJson(fluidInputRecipe.getInput()));
		}
		
		if (recipe instanceof ItemOutputRecipe itemOutputRecipe) {
			json.add("output", ItemResult.toJson(itemOutputRecipe.getItemOutput()));
		}
		
		if (recipe instanceof FluidOutputRecipe fluidOutputRecipe) {
			json.add("output", FluidResult.toJson(fluidOutputRecipe.getFluidOutput()));
		}
		
		if (recipe instanceof EnergyInputRecipe energyInputRecipe) {
			json.addProperty("energy_input", energyInputRecipe.getEnergyInput());
		}
		
		if (recipe instanceof EnergyOutputRecipe energyOutputRecipe) {
			json.addProperty("energy_output", energyOutputRecipe.getEnergyOutput());
		}
		
		if (recipe instanceof AMRecipe amRecipe) {
			json.addProperty("time", amRecipe.getTime());
		}
		
		return json;
	}
}
