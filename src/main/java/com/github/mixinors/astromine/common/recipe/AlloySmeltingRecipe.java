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

package com.github.mixinors.astromine.common.recipe;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.AMRecipeType;
import com.github.mixinors.astromine.common.recipe.base.input.DoubleItemInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.output.ItemOutputRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.common.recipe.result.ItemResult;
import com.github.mixinors.astromine.common.transfer.storage.SimpleItemVariantStorage;
import com.github.mixinors.astromine.common.util.IntegerUtils;
import com.github.mixinors.astromine.common.util.LongUtils;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import com.github.mixinors.astromine.common.recipe.base.AstromineRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record AlloySmeltingRecipe(
		ResourceLocation id,
		ItemIngredient firstInput,
		ItemIngredient secondInput,
		ItemResult output,
		long energyInput,
		int time
) implements DoubleItemInputRecipe, ItemOutputRecipe {
	private static final Map<Level, AlloySmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();
	
	public static boolean allows(Level world, ItemStack... stacks) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllRecipesFor(Type.INSTANCE).stream().map(holder -> holder.value()).toArray(AlloySmeltingRecipe[]::new));
		}
		
		for (var recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(stacks)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Optional<AlloySmeltingRecipe> matching(Level world, SimpleItemVariantStorage... storages) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllRecipesFor(Type.INSTANCE).stream().map(holder -> holder.value()).toArray(AlloySmeltingRecipe[]::new));
		}
		
		for (var recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(storages)) {
				return Optional.of(recipe);
			}
		}
		
		return Optional.empty();
	}
	
	public boolean matches(SimpleItemVariantStorage... storages) {
		var firstInputStorage = storages[0];
		var secondInputStorage = storages[1];
		
		var outputStorage = storages[2];
		
		if (!((firstInput.test(firstInputStorage) && secondInput.test(secondInputStorage))
				|| (firstInput.test(secondInputStorage) && secondInput.test(firstInputStorage)))) {
			return false;
		}
		
		return output.equalsAndFitsIn(outputStorage);
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}
	
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(AMBlocks.ADVANCED_ALLOY_SMELTER.get());
	}
	
	@Override
	public long getEnergyInput() {
		return energyInput;
	}
	
	@Override
	public int getTime() {
		return time;
	}
	
	public ItemIngredient getFirstInput() {
		return firstInput;
	}
	
	public ItemIngredient getSecondInput() {
		return secondInput;
	}
	
	public ItemResult getItemOutput() {
		return output;
	}
	
	public static final class Serializer implements AstromineRecipeSerializer<AlloySmeltingRecipe> {
		public static final ResourceLocation ID = AMCommon.id("alloy_smelting");
		
		public static final Serializer INSTANCE = new Serializer();
		
		private Serializer() {
		}
		
		@Override
		public AlloySmeltingRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			var format = new Gson().fromJson(object, AlloySmeltingRecipe.Format.class);
			
			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromJson(format.firstInput),
					ItemIngredient.fromJson(format.secondInput),
					ItemResult.fromJson(format.output),
					LongUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}
		
		@Override
		public AlloySmeltingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					ItemIngredient.fromPacket(buffer),
					ItemResult.fromPacket(buffer),
					LongUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}
		
		@Override
		public void write(FriendlyByteBuf buffer, AlloySmeltingRecipe recipe) {
			ItemIngredient.toPacket(buffer, recipe.firstInput);
			ItemIngredient.toPacket(buffer, recipe.secondInput);
			ItemResult.toPacket(buffer, recipe.output);
			LongUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}
	
	public static final class Type implements AMRecipeType<AlloySmeltingRecipe> {
		public static final Type INSTANCE = new Type();
		
		private Type() {
		}
	}
	
	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;
		
		@SerializedName("second_input")
		JsonElement secondInput;
		
		JsonElement output;
		
		@SerializedName("energy_input")
		JsonElement energyInput;
		
		JsonElement time;
	}
}
