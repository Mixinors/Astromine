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
import com.github.mixinors.astromine.common.recipe.base.input.FluidInputRecipe;
import com.github.mixinors.astromine.common.recipe.base.output.EnergyOutputRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.transfer.storage.SimpleFluidVariantStorage;
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
import net.neoforged.neoforge.fluids.FluidStack;
import java.util.Optional;

public record FluidGeneratingRecipe(
		ResourceLocation id,
		FluidIngredient input,
		long energyOutput,
		int time
) implements FluidInputRecipe, EnergyOutputRecipe {
	public static boolean allows(Level world, FluidStack... stacks) {
		for (var holder : world.getRecipeManager().getAllRecipesFor(Type.INSTANCE)) {
			var recipe = holder.value();
			
			if (recipe.allows(stacks)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Optional<FluidGeneratingRecipe> matching(Level world, SimpleFluidVariantStorage... storages) {
		for (var holder : world.getRecipeManager().getAllRecipesFor(Type.INSTANCE)) {
			var recipe = holder.value();
			
			if (recipe.matches(storages)) {
				return Optional.of(recipe);
			}
		}
		
		return Optional.empty();
	}
	
	public boolean matches(SimpleFluidVariantStorage... storages) {
		var inputStorage = storages[0];
		
		return input.test(inputStorage);
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
		return new ItemStack(AMBlocks.ADVANCED_FLUID_GENERATOR.get());
	}
	
	@Override
	public long getEnergyOutput() {
		return energyOutput;
	}
	
	@Override
	public int getTime() {
		return time;
	}
	
	public FluidIngredient getInput() {
		return input;
	}
	
	public static final class Serializer implements AstromineRecipeSerializer<FluidGeneratingRecipe> {
		public static final ResourceLocation ID = AMCommon.id("fluid_generating");
		
		public static final Serializer INSTANCE = new Serializer();
		
		private Serializer() {
		}
		
		@Override
		public FluidGeneratingRecipe fromJson(ResourceLocation identifier, JsonObject object) {
			var format = new Gson().fromJson(object, FluidGeneratingRecipe.Format.class);
			
			return new FluidGeneratingRecipe(
					identifier,
					FluidIngredient.fromJson(format.input),
					LongUtils.fromJson(format.energyOutput),
					IntegerUtils.fromJson(format.time)
			);
		}
		
		@Override
		public FluidGeneratingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf buffer) {
			return new FluidGeneratingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					LongUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}
		
		@Override
		public void write(FriendlyByteBuf buffer, FluidGeneratingRecipe recipe) {
			FluidIngredient.toPacket(buffer, recipe.input);
			LongUtils.toPacket(buffer, recipe.energyOutput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}
	
	public static final class Type implements AMRecipeType<FluidGeneratingRecipe> {
		public static final Type INSTANCE = new Type();
		
		private Type() {
		}
	}
	
	public static final class Format {
		JsonElement input;
		
		@SerializedName("energy_output")
		JsonElement energyOutput;
		
		JsonElement time;
	}
}
