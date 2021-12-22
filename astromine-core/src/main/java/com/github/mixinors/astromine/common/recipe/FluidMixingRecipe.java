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

package com.github.mixinors.astromine.common.recipe;

import com.github.mixinors.astromine.common.recipe.base.AMRecipeType;
import com.github.mixinors.astromine.common.recipe.ingredient.FluidIngredient;
import com.github.mixinors.astromine.common.recipe.result.FluidResult;
import dev.architectury.core.AbstractRecipeSerializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.util.DoubleUtils;
import com.github.mixinors.astromine.common.util.IntegerUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FluidMixingRecipe implements Recipe<Inventory> {
	public final Identifier id;
	public final FluidIngredient firstInput;
	public final FluidIngredient secondInput;
	public final FluidResult output;
	public final double energyInput;
	public final int time;

	private static final Map<World, FluidMixingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public FluidMixingRecipe(Identifier id, FluidIngredient firstInput, FluidIngredient secondIngredient, FluidResult output, double energyInput, int time) {
		this.id = id;
		this.firstInput = firstInput;
		this.secondInput = secondIngredient;
		this.output = output;
		this.energyInput = energyInput;
		this.time = time;
	}

	public static boolean allows(World world, FluidVariant... variants) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (FluidMixingRecipe) it).toArray(FluidMixingRecipe[]::new));
		}

		for (FluidMixingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(variants)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<FluidMixingRecipe> matching(World world, SingleSlotStorage<FluidVariant>... storages) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (FluidMixingRecipe) it).toArray(FluidMixingRecipe[]::new));
		}

		for (FluidMixingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(storages)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(SingleSlotStorage<FluidVariant>... storages) {
		var firstInputStorage = storages[0];
		var secondInputStorage = storages[1];
		
		var outputStorage = storages[2];
		
		if (!firstInput.test(firstInputStorage)) {
			return false;
		}
		
		if (!secondInput.test(secondInputStorage)) {
			return false;
		}
		
		return output.equalsAndFitsIn(outputStorage);
	}

	public boolean allows(FluidVariant... variants) {
		var firstInputVariant = variants[0];
		var secondInputVariant = variants[1];
		
		if (!firstInput.test(firstInputVariant, Long.MAX_VALUE) && !secondInput.test(firstInputVariant, Long.MAX_VALUE)) {
			return false;
		}
		
		return secondInput.test(secondInputVariant, Long.MAX_VALUE) || firstInput.test(secondInputVariant, Long.MAX_VALUE);
	}

	@Override
	public Identifier getId() {
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
	public boolean matches(Inventory inventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	public static final class Serializer extends AbstractRecipeSerializer<FluidMixingRecipe> {
		public static final Identifier ID = AMCommon.id("fluid_mixing");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public FluidMixingRecipe read(Identifier identifier, JsonObject object) {
			FluidMixingRecipe.Format format = new Gson().fromJson(object, FluidMixingRecipe.Format.class);

			return new FluidMixingRecipe(
					identifier,
					FluidIngredient.fromJson(format.firstInput),
					FluidIngredient.fromJson(format.secondInput),
					FluidResult.fromJson(format.output),
					DoubleUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public FluidMixingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new FluidMixingRecipe(
					identifier,
					FluidIngredient.fromPacket(buffer),
					FluidIngredient.fromPacket(buffer),
					FluidResult.fromPacket(buffer),
					DoubleUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, FluidMixingRecipe recipe) {
			FluidIngredient.toPacket(buffer, recipe.firstInput);
			FluidIngredient.toPacket(buffer, recipe.secondInput);
			FluidResult.toPacket(buffer, recipe.output);
			DoubleUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<FluidMixingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
	}

	public static final class Format {
		@SerializedName("first_input")
		JsonElement firstInput;

		@SerializedName("second_input")
		JsonElement secondInput;

		@SerializedName("output")
		JsonElement output;

		@SerializedName("energy_input")
		JsonElement energyInput;

		JsonElement time;
	}
}
