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

import com.github.mixinors.astromine.AMCommon;
import com.github.mixinors.astromine.common.recipe.base.AMRecipeType;
import com.github.mixinors.astromine.registry.common.AMBlocks;
import dev.architectury.core.AbstractRecipeSerializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import com.github.mixinors.astromine.common.recipe.base.EnergyConsumingRecipe;
import com.github.mixinors.astromine.common.recipe.ingredient.ItemIngredient;
import com.github.mixinors.astromine.common.util.DoubleUtils;
import com.github.mixinors.astromine.common.util.IntegerUtils;
import com.github.mixinors.astromine.common.util.StackUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AlloySmeltingRecipe implements Recipe<Inventory> {
	public final Identifier id;
	public final ItemIngredient firstInput;
	public final ItemIngredient secondInput;
	public final ItemStack output;
	public final double energyInput;
	public final int time;

	private static final Map<World, AlloySmeltingRecipe[]> RECIPE_CACHE = new HashMap<>();

	public AlloySmeltingRecipe(Identifier id, ItemIngredient firstInput, ItemIngredient secondInput, ItemStack output, double energyInput, int time) {
		this.id = id;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.output = output;
		this.energyInput = energyInput;
		this.time = time;
	}
	
	public static boolean allows(World world, ItemVariant... variants) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (AlloySmeltingRecipe) it).toArray(AlloySmeltingRecipe[]::new));
		}

		for (AlloySmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.allows(variants)) {
				return true;
			}
		}

		return false;
	}

	public static Optional<AlloySmeltingRecipe> matching(World world, SingleSlotStorage<ItemVariant>... storages) {
		if (RECIPE_CACHE.get(world) == null) {
			RECIPE_CACHE.put(world, world.getRecipeManager().getAllOfType(Type.INSTANCE).values().stream().map(it -> (AlloySmeltingRecipe) it).toArray(AlloySmeltingRecipe[]::new));
		}

		for (AlloySmeltingRecipe recipe : RECIPE_CACHE.get(world)) {
			if (recipe.matches(storages)) {
				return Optional.of(recipe);
			}
		}

		return Optional.empty();
	}

	public boolean matches(SingleSlotStorage<ItemVariant>... storages) {
		var firstInputStorage = storages[0];
		var secondInputStorage = storages[1];
		
		var outputStorage = storages[2];
		
		if (!firstInput.test(firstInputStorage) && !secondInput.test(firstInputStorage)) {
			return false;
		}

		if (!firstInput.test(secondInputStorage) && !secondInput.test(secondInputStorage)) {
			return false;
		}

		return StackUtils.equalsAndFits(output, outputStorage.getResource().toStack((int) outputStorage.getAmount()));
	}

	public boolean allows(ItemVariant... variants) {
		var firstInputVariant = variants[0];
		var secondInputVariant = variants[1];
		
		if (!firstInput.test(firstInputVariant, Long.MAX_VALUE) && !secondInput.test(firstInputVariant, Long.MAX_VALUE)) {
			return false;
		}

		return firstInput.test(secondInputVariant, Long.MAX_VALUE) || secondInput.test(secondInputVariant, Long.MAX_VALUE);
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
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
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
	public ItemStack createIcon() {
		return new ItemStack(AMBlocks.ADVANCED_ALLOY_SMELTER.get());
	}

	public static final class Serializer extends AbstractRecipeSerializer<AlloySmeltingRecipe> {
		public static final Identifier ID = AMCommon.id("alloy_smelting");

		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, JsonObject object) {
			AlloySmeltingRecipe.Format format = new Gson().fromJson(object, AlloySmeltingRecipe.Format.class);

			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromJson(format.firstInput),
					ItemIngredient.fromJson(format.secondInput),
					StackUtils.fromJson(format.output.getAsJsonObject()),
					DoubleUtils.fromJson(format.energyInput),
					IntegerUtils.fromJson(format.time)
			);
		}

		@Override
		public AlloySmeltingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new AlloySmeltingRecipe(
					identifier,
					ItemIngredient.fromPacket(buffer),
					ItemIngredient.fromPacket(buffer),
					StackUtils.fromPacket(buffer),
					DoubleUtils.fromPacket(buffer),
					IntegerUtils.fromPacket(buffer)
			);
		}

		@Override
		public void write(PacketByteBuf buffer, AlloySmeltingRecipe recipe) {
			ItemIngredient.toPacket(buffer, recipe.firstInput);
			ItemIngredient.toPacket(buffer, recipe.secondInput);
			StackUtils.toPacket(buffer, recipe.output);
			DoubleUtils.toPacket(buffer, recipe.energyInput);
			IntegerUtils.toPacket(buffer, recipe.time);
		}
	}

	public static final class Type implements AMRecipeType<AlloySmeltingRecipe> {
		public static final Type INSTANCE = new Type();

		private Type() {}
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
